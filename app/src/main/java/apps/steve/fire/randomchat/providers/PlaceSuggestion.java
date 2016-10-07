package apps.steve.fire.randomchat.providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steve on 4/10/2016.
 */

public class PlaceSuggestion extends ContentProvider implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = PlaceSuggestion.class.getSimpleName();
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> mResultList;

    /**
     * Handles autocomplete requests.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter mPlaceFilter;


    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private static final String[] SEARCH_SUGGEST_COLUMNS = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
    };

    public static final String AUTHORITY = "apps.steve.fire.randomchat.providers.PlaceSuggestion";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");

    // UriMatcher constant for search suggestions
    private static final int SEARCH_SUGGEST = 1;

    private static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }


    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                //.enableAutoManage(new FragmentActivity(), 0, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        mBounds = BOUNDS_GREATER_SYDNEY;
        AutocompleteFilter.Builder builder = new AutocompleteFilter.Builder();
        builder.setTypeFilter(Place.TYPE_UNIVERSITY);
        mPlaceFilter = builder.build();
        return false;
    }





    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "Cursor query");
        Log.d(TAG, "uri : "+ uri);

        String query = uri.getLastPathSegment().toUpperCase();
        Log.d(TAG, "query: " + query);
        // Use the UriMatcher to see what kind of query we have
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                Log.d(TAG, "Search suggestions requested.");
                MatrixCursor cursor = new MatrixCursor(SEARCH_SUGGEST_COLUMNS, 1);


                ArrayList<AutocompletePrediction> filterData = new ArrayList<>();

                // Skip the autocomplete query if no constraints are given.
                if (query != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = getAutocomplete(query);
                }

                /*
                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }*/

                for (int i= 0; i < filterData.size(); i++){
                    cursor.addRow(new String[] {
                            ""+i, ""+filterData.get(i).getPrimaryText(STYLE_BOLD), ""+filterData.get(i).getSecondaryText(STYLE_BOLD), ""+filterData.get(i).getPlaceId()
                    });
                }

                return cursor;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * objects to store the Place ID and description that the API returns.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
     * @see AutocompletePrediction#freeze()
     */
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.");
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "onConnectionFailed: " + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getContext(), "onConnected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "onConnectionSuspended", Toast.LENGTH_LONG).show();
    }
}
