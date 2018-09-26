package com.android.example.e_yellowcard.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    // Constant for logging
    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String YC_POLICIES_BASE_URL = "http://67.205.112.174:8180/YC/api/policies?fields=id," +
            "issuedOn,validFrom,numdays,validTo,policyNumber,yellowCardNumber,vehicleRegistrationNumber," +
            "engineNumber,chassisNumber,color,typeOfBody,make,useOfVehicle,vehicleType,premium,tax," +
            "yellowCard%3Aid,insured%3AfirstName,insured%3AlastName,insured%3AinsuredName," +
            "insured%3Aemail,insured%3AmobileNumber,insured%3AinsuredType,insured%3AcompanyName," +
            "countriesCoveredStr,policyStatus,valid&page=1&where=issuedBy.id,%3D,1158df4a-6f5b-4e75-929f-2e8ebb6180a9";

    /**
     * Builds the URL used to query e-Yellow Card.
     *
     * @return The URL to use to query the e-Yellow Card.
     */
    public static URL buildYCPoliciesUrl() {
        Uri builtUri = Uri.parse(YC_POLICIES_BASE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestProperty("token", "bhrp3hk5rgh6p99gjtl2f3qvd1");

        try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
