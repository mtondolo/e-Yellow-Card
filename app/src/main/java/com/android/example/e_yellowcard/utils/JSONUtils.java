package com.android.example.e_yellowcard.utils;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.android.example.e_yellowcard.data.PolicyContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     *
     * @param ycPolicyJsonStr JSON response from server
     * @return Array of Strings describing policies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ContentValues[] getSimpleYCPolicyStringFromJson(Context context, String ycPolicyJsonStr)
            throws JSONException {

        // Policy item. Each policy item is an element of the "attributes" array
        final String YC_DETAILS = "attributes";

        // Policy Status is an object under policy item
        final String YC_STATUS = "policyStatus";

        // Policy number,Number of days
        final String YC_NUMBER = "yellowCardNumber";
        final String YC_DAYS = "numdays";
        final String YC_VEHICLE_REG = "vehicleRegistrationNumber";
        final String YC_STATUS_DETAILS = "name";

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(ycPolicyJsonStr)) {
            return null;
        }

        JSONArray ycPolicyArray = new JSONArray(ycPolicyJsonStr);

        ContentValues[] policyContentValues = new ContentValues[ycPolicyArray.length()];

        for (int i = 0; i < ycPolicyArray.length(); i++) {

            /* These are the values that will be collected */
            String ycNumber;
            int ycDays;
            String ycVehicleReg;
            String ycStatusDetails;

            /* Get the JSON object representing the yc policy */
            JSONObject ycPolicyItem = ycPolicyArray.getJSONObject(i);

            JSONObject ycPolicyDetails = ycPolicyItem.getJSONObject(YC_DETAILS);

            // Extract the value for the keys called "numdays", "yellowCardNumber" and "vehicleRegistrationNumber"
            ycDays = ycPolicyDetails.getInt(YC_DAYS);
            ycNumber = ycPolicyDetails.getString(YC_NUMBER);
            ycVehicleReg = ycPolicyDetails.getString(YC_VEHICLE_REG);

            // Extract the value for the key called "name"
            JSONObject ycPolicyStatus = ycPolicyDetails.getJSONObject(YC_STATUS);
            ycStatusDetails = ycPolicyStatus.getString(YC_STATUS_DETAILS);

            ContentValues policyValues = new ContentValues();
            policyValues.put(PolicyContract.PolicyEntry.COLUMN_DAYS, ycDays);
            policyValues.put(PolicyContract.PolicyEntry.COLUMN_NUMBER, ycNumber);
            policyValues.put(PolicyContract.PolicyEntry.COLUMN_REG, ycVehicleReg);
            policyValues.put(PolicyContract.PolicyEntry.COLUMN_STATUS, ycStatusDetails);
            policyContentValues[i] = policyValues;

        }

        return policyContentValues;
    }
}
