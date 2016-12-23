package apps.steve.fire.randomchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.activities.FullImageActivity;
import apps.steve.fire.randomchat.firebase.FirebaseStorageHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class UploadImge extends AppCompatActivity {


    static final int RC_PHOTO_PICKER = 1;
    private static final String TAG = UploadImge.class.getSimpleName();


    /*
    private FirebaseApp app;
    private FirebaseStorage storage;
    private StorageReference storageRef;*/

    FloatingActionButton fab;

    @BindView(R.id.textView)
    TextView textview;

    @BindView(R.id.imageView)
    ImageView imageView;

    private double progress = 0.0;

    @BindView(R.id.progressLoading)
    ProgressBar progressBar;

    private Uri selectedImageUri;
    FirebaseStorageHelper fireStorage;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_imge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ButterKnife.bind(this);
        // Get the Firebase app and all primitives we'll use
        /*
        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);


        // Get a reference to the location where we'll store our photos
        storageRef = storage.getReference("chat_photos");*/

        textview.setText("getCurrentTimeStamp() : " + getCurrentTimeStamp());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pick image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                intentPickerImage();
            }
        });
        setProgressInvisible();

    }

    @Override
    protected void onResume() {
        fireStorage = new FirebaseStorageHelper(androidID);
        super.onResume();
    }

    private void setProgressVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void setProgressInvisible(){
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void intentPickerImage(){
        EasyImage.openGallery(this, 0);
        /*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(getActivity(), "Exception : "+ e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //onPhotosReturned(imageFiles);
                Toast.makeText(getActivity(), "onImagePicked : "+ imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(imageFile) // Uri of the picture
                        .into(imageView);

                 File compressedImageFile = Compressor.getDefault(getActivity()).compressToFile(imageFile);

                fireStorage.uploadFile(Uri.fromFile(compressedImageFile), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                // When the image has successfully uploaded, we get its download URL
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                // Set the download URL to the message box, so that the user can send it to the database
                                textview.setText("URL IMAGE : \n" + downloadUrl.toString()
                                        + "\nLastPathSegment : " + downloadUrl.getLastPathSegment());
                                //begintoDownload(downloadUrl);
                                progressBar.setVisibility(View.INVISIBLE);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), FullImageActivity.class);
                                        intent.setData(downloadUrl);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }, new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                Log.d(Constants.TAG, "getBytesTransferred : " + taskSnapshot.getBytesTransferred());
                                Log.d(Constants.TAG, "getTotalByteCount : " + taskSnapshot.getTotalByteCount());
                                progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                                textview.setText(""+progress);
                                progressBar.setProgress((int) progress);
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getActivity(), "EXCEPTION : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled

                Toast.makeText(getActivity(), "CANCELED", Toast.LENGTH_LONG).show();
                /*if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }*/
            }
        });
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            progressBar.setVisibility(View.VISIBLE);

            Glide.with(getActivity())
                    .load(selectedImageUri) // Uri of the picture
                    .into(imageView);
            // Get a reference to store file at chat_photos/<FILENAME>
            fireStorage.uploadFile(selectedImageUri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            // Set the download URL to the message box, so that the user can send it to the database
                            textview.setText("URL IMAGE : \n" + downloadUrl.toString()
                                    + "\nLastPathSegment : " + downloadUrl.getLastPathSegment());
                            //begintoDownload(downloadUrl);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d(Constants.TAG, "getBytesTransferred : " + taskSnapshot.getBytesTransferred());
                            Log.d(Constants.TAG, "getTotalByteCount : " + taskSnapshot.getTotalByteCount());
                            progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                            textview.setText(""+progress);
                            progressBar.setProgress((int) progress);
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getActivity(), "EXCEPTION : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }*/



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        /*if (storageRef != null) {
            outState.putString("reference", storageRef.toString());
            outState.putDouble("progress", progress);
            outState.putParcelable("uri", selectedImageUri);
        }*/
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        progress = savedInstanceState.getDouble("progress");
        selectedImageUri = savedInstanceState.getParcelable("uri");
        if (stringRef == null) {
            return;
        }
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);
        textview.setText(""+progress);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = storageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            progressBar.setProgress((int)progress);
            Glide.with(getActivity())
                    .load(selectedImageUri) // Uri of the picture
                    .into(imageView);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // When the image has successfully uploaded, we get its download URL
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    // Set the download URL to the message box, so that the user can send it to the database
                    textview.setText("URL IMAGE : \n" + downloadUrl.toString()
                            + "\nLastPathSegment : " + downloadUrl.getLastPathSegment());
                    progressBar.setVisibility(View.INVISIBLE);
                    begintoDownload(downloadUrl);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    Log.d(Constants.TAG, "getBytesTransferred : " + taskSnapshot.getBytesTransferred());
                    Log.d(Constants.TAG, "getTotalByteCount : " + taskSnapshot.getTotalByteCount());
                    progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int)progress);
                    textview.setText(""+progress);
                    Snackbar.make(fab, "Upload is " + progress  "% done", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "EXCEPTION : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void begintoDownload(Uri downloadUri) {
        File localFile = null;
        try {
            localFile = File.createTempFile(getCurrentTimeStamp(), "jpg");

            StorageReference referenceUriDownload = storage.getReference().child(downloadUri.getLastPathSegment());

            final File finalLocalFile = localFile;
            referenceUriDownload.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Glide.with(getActivity())
                        .load(finalLocalFile.getPath()) // Uri of the picture
                .into(imageView);
                Toast.makeText(getActivity(), "FILE SUCCESS SAVED.", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getActivity(), "OnFailureListener : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "ERROR AL DESCARGAR.", Toast.LENGTH_LONG).show();
        }
    }*/

    private AppCompatActivity getActivity()
    {
        return this;
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return "XXDDXX_XDXDXD";
        }
    }

}
