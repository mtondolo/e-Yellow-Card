package com.android.example.e_yellowcard.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.android.example.e_yellowcard.data.PolicyContract;
import com.android.example.e_yellowcard.utils.JSONUtils;
import com.android.example.e_yellowcard.utils.NetworkUtils;

import java.net.URL;

public class PolicySyncTask {

    /**
     * Performs the network request for updated policy, parses the JSON from that request, and
     * inserts the new policy information into our ContentProvider.
     */
    synchronized public static void syncPolicy(Context context) {
        try {

            /*
             * The getUrl method will return the URL that we need to get the policy JSON for the
             * policy.
             *
             */
            URL policyRequestUrl = NetworkUtils.buildYCPoliciesUrl();

            /* Use the URL to retrieve the JSON */
            String jsonPolicyResponse = NetworkUtils.getResponseFromHttpUrl(policyRequestUrl);

            /* Parse the JSON into a list of policy values */
            ContentValues[] policyValues = JSONUtils.getSimpleYCPolicyStringFromJson(context, jsonPolicyResponse);

            /*
             * In cases where our JSON contained an error code, getSimpleYCPolicyStringFromJson
             * would have returned null.
             */

            if (policyValues != null && policyValues.length != 0) {

                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver policyContentResolver = context.getContentResolver();

                /* Delete old policy data because we don't need to keep multiple days' data */
                policyContentResolver.delete(
                        PolicyContract.PolicyEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new policy data into Policy's ContentProvider */
                policyContentResolver.bulkInsert(
                        PolicyContract.PolicyEntry.CONTENT_URI,
                        policyValues);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
