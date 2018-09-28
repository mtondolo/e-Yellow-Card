package com.android.example.e_yellowcard.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * This class serves as the ContentProvider for all of Policy's data. This class allows us to
 * insert data, query data, and delete data.
 */
public class PolicyProvider extends ContentProvider {

    // These constant will be used to match URIs with the data they are looking for.
    public static final int CODE_POLICY = 100;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private PolicyDBHelper mOpenHelper;

    // Creates the UriMatcher that will match each URI to the CODE_POLICY constant defined above.
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PolicyContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PolicyContract.PATH_POLICY, CODE_POLICY);
        return matcher;
    }

    //In onCreate, we initialize our content provider on startup.
    @Override
    public boolean onCreate() {
        mOpenHelper = new PolicyDBHelper(getContext());
        return true;
    }

    // Handles query requests from clients.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        /*
         * Given a URI, will determine what kind of request is being made and query the database
         * accordingly.
         */
        switch (sUriMatcher.match(uri)) {
            case CODE_POLICY: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        PolicyContract.PolicyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * We aren't going to do anything with this method. However, we are required to override it as
     * WeatherProvider extends ContentProvider.
     */
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("We are not implementing getType.");
    }

    /**
     * We aren't going to do anything with this method. However, we are required to
     * override it as PolicyProvider extends ContentProvider.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    // Handles requests to insert a set of new rows.
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_POLICY:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PolicyContract.PolicyEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows inserted from our implementation of bulkInsert
                return rowsInserted;

            // If the URI does match match CODE_POLICY, return the super implementation of bulkInsert
            default:
                return super.bulkInsert(uri, values);
        }
    }


    // Deletes data at a given URI with optional arguments for more fine tuned deletions.
    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case CODE_POLICY:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        PolicyContract.PolicyEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        throw new RuntimeException("We will implement the update method!");
    }
}
