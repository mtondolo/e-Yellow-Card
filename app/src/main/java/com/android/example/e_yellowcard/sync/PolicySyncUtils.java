package com.android.example.e_yellowcard.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.example.e_yellowcard.data.PolicyContract;

public class PolicySyncUtils {

    private static boolean sInitialized;

    // Creates periodic sync tasks and checks to see if an immediate sync is required.
    synchronized public static void initialize(@NonNull final Context context) {

        // Only perform initialization once per app lifetime.
        if (sInitialized) return;

        // If the method body is executed, set sInitialized to true
        sInitialized = true;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                /* URI for every row of policy data in our policy table*/
                Uri policyQueryUri = PolicyContract.PolicyEntry.CONTENT_URI;

                /*
                 * Since this query is going to be used only as a check to see if we have any
                 * data (rather than to display data), we just need to PROJECT the ID of each
                 * row.
                 */
                String[] projectionColumns = {PolicyContract.PolicyEntry._ID};

                /* Here, we perform the query to check to see if we have any policy data */
                Cursor cursor = context.getContentResolver().query(
                        policyQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                /*
                 * If the Cursor was null OR if it was empty, we need to sync immediately to
                 * be able to display data to the user.
                 */
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                /* Close the Cursor to avoid memory leaks! */
                cursor.close();
                return null;
            }
        }.execute();
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, PolicySyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
