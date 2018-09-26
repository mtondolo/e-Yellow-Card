package com.android.example.e_yellowcard;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.example.e_yellowcard.utils.JSONUtils;
import com.android.example.e_yellowcard.utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mYCListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mYCListTextView = (TextView) findViewById(R.id.tv_yc);

        /* Once all of our views are setup, we can load the weather data. */
        loadYCPolicyData();
    }

    /**
     * This method will tell some background method to get the weather data in the background.
     */
    private void loadYCPolicyData() {
        new FetchYCPolicyTask().execute();
    }

    public class FetchYCPolicyTask extends AsyncTask<String, Void, String[]> {

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
            if (ycPolicyData != null) {
                /*
                 * Iterate through the array and append the Strings to the TextView. We add
                 * the "\n\n\n" after the String to give visual separation between each String in the
                 * TextView.
                 */
                for (String ycPolicyString : ycPolicyData) {
                    mYCListTextView.append((ycPolicyString) + "\n\n\n");
                }
            }
        }
    }
}

