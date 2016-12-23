package apps.steve.fire.randomchat.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import apps.steve.fire.randomchat.UploadImge;

/**
 * Created by Steve on 20/12/2016.
 */

public class FirebaseStorageHelper {

    private static String REF_CHAT_PICTURES = "chat_pictures";

    private FirebaseApp app;
    private FirebaseStorage storage;
    //private StorageReference storageRef;
    private StorageReference userStorageRef;
    private String androidId;

    public FirebaseStorageHelper(String androidId) {
        this.androidId = androidId;
        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);
        // Get a reference to the location where we'll store our photos
        userStorageRef = storage.getReference(REF_CHAT_PICTURES).child(androidId);
    }

    public void uploadFile(Uri uri, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnProgressListener<UploadTask.TaskSnapshot> onProgressListener, OnFailureListener onFailureListener){
        // Get a reference to store file at chat_photos/<FILENAME>
        StorageReference pictureRef = userStorageRef.child(uri.getLastPathSegment());
        // Upload file to Firebase Storage
        pictureRef.putFile(uri)
                .addOnSuccessListener(onSuccessListener)
                .addOnProgressListener(onProgressListener)
                .addOnFailureListener(onFailureListener);
    }

}
