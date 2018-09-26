package com.android.example.e_yellowcard;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.example.e_yellowcard.utils.JSONUtils;
import com.android.example.e_yellowcard.utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mYCListTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mYCListTextView = (TextView) findViewById(R.id.tv_yc);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadYCPolicyData();
    }

    /**
     * This method will tell some background method to get the policy data in the background.
     */
    private void loadYCPolicyData() {
        showYCPolicyDataView();
        new FetchYCPolicyTask().execute();
    }

    /**
     * This method will make the View for the news data visible and
     * hide the error message.
     */
    private void showYCPolicyDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the policy data is visible */
        mYCListTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the news
     * View.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mYCListTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchYCPolicyTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected String[] doInBackground(String... params) {

            URL ycPolicyRequestUrl = NetworkUtils.buildYCPoliciesUrl();

            try {

                String jsonYCPolicyResponse = NetworkUtils
                        .getResponseFromHttpUrl(ycPolicyRequestUrl);

                String[] simpleJsonYCPolicyData = JSONUtils
                        .getSimpleYCPolicyStringFromJson(MainActivity.this, jsonYCPolicyResponse);
                return simpleJsonYCPolicyData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] ycPolicyData) {
            //As soon as the data is finished loading, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (ycPolicyData != null) {

                // If the policy data was not null, make sure the data view is visible
                showYCPolicyDataView();

                /*
                 * Iterate through the array and append the Strings to the TextView. We add
                 * the "\n\n\n" after the String to give visual separation between each String in the
                 * TextView.
                 */
                for (String ycPolicyString : ycPolicyData) {
                    mYCListTextView.append((ycPolicyString) + "\n\n\n");
                }
            } else {
                // If the policy data was null, show the error message
                showErrorMessage();
            }
        }
    }
}

