package com.android.example.e_yellowcard;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.e_yellowcard.utils.JSONUtils;
import com.android.example.e_yellowcard.utils.NetworkUtils;

import java.net.URL;

public class PolicyActivity extends AppCompatActivity implements
        PolicyAdapter.PolicyAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String[]> {

    private RecyclerView mRecyclerView;
    private PolicyAdapter mPolicyAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static final int POLICY_LOADER_ID = 0;

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
        mPolicyAdapter = new PolicyAdapter(this);

        /*
         * Use mRecyclerView.setAdapter and pass in mNewsAdapter.
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        mRecyclerView.setAdapter(mPolicyAdapter);

        // This ID will uniquely identify the Loader.
        int loaderId = POLICY_LOADER_ID;

        /*
         * From PolicyActivity, we have implemented the LoaderCallbacks interface with the type of
         * String array.
         */
        LoaderCallbacks<String[]> callback = PolicyActivity.this;

        // The second parameter of the initLoader method below is a Bundle.
        Bundle bundleForLoader = null;

        // Ensures a loader is initialized and active.
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

    }

    /**
     * This method will make the View for the news data visible and hide the error message.
     */
    private void showPolicyDataView() {
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

    /**
     * This method is overridden by the NewsActivity class in order to handle RecyclerView item
     * clicks.
     */
    @Override
    public void onClick(String policyItem) {
        Context context = this;
        Toast.makeText(context, policyItem, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     */
    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle loaderArgs) {

        return new AsyncTaskLoader<String[]>(this) {

            /* This String array will hold and help cache our policy data */
            String[] mPolicyData = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (mPolicyData != null) {
                    deliverResult(mPolicyData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from Policy API in the background.
             */
            @Override
            public String[] loadInBackground() {
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

            /**
             * Sends the result of the load to the registered listener.
             */
            @Override
            public void deliverResult(String[] data) {
                mPolicyData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     */
    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mPolicyAdapter.setPolicyData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showPolicyDataView();
        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     */
    @Override
    public void onLoaderReset(Loader<String[]> loader) {
        /*
         * We aren't using this method at the moment, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

}


