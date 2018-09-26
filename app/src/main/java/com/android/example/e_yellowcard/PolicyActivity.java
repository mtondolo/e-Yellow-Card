package com.android.example.e_yellowcard;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.example.e_yellowcard.utils.JSONUtils;
import com.android.example.e_yellowcard.utils.NetworkUtils;

import java.net.URL;

public class PolicyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PolicyAdapter mPolicyAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_policy);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list
         * will have the same size.
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The NewsAdapter is responsible for linking our news data with the Views that will end up
         * displaying our news data.
         */
        mPolicyAdapter = new PolicyAdapter();

        /*
         * Use mRecyclerView.setAdapter and pass in mNewsAdapter.
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        mRecyclerView.setAdapter(mPolicyAdapter);

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
     * This method will make the View for the news data visible and hide the error message.
     */
    private void showYCPolicyDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the policy data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the news View.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
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
                        .getSimpleYCPolicyStringFromJson(PolicyActivity.this, jsonYCPolicyResponse);
                return simpleJsonYCPolicyData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] ycPolicyData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (ycPolicyData != null) {
                showYCPolicyDataView();
                mPolicyAdapter.setPolicyData(ycPolicyData);
            } else {
                showErrorMessage();
            }
        }
    }
}


