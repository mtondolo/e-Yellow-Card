package com.android.example.e_yellowcard;

import android.database.Cursor;
import android.net.Uri;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.example.e_yellowcard.data.PolicyContract;

public class PolicyActivity extends AppCompatActivity implements
        PolicyAdapter.PolicyAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = PolicyActivity.class.getSimpleName();

    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * policy data.
     */
    public static final String[] MAIN_POLICY_PROJECTION = {
            PolicyContract.PolicyEntry.COLUMN_DAYS,
            PolicyContract.PolicyEntry.COLUMN_NUMBER,
            PolicyContract.PolicyEntry.COLUMN_REG,
            PolicyContract.PolicyEntry.COLUMN_STATUS
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_DAYS = 0;
    public static final int INDEX_NUMBER = 1;
    public static final int INDEX_REG = 2;
    public static final int INDEX_STATUS = 3;

    // This ID will be used to identify the Loader responsible for loading our policies.
    private static final int ID_POLICY_LOADER = 44;

    private RecyclerView mRecyclerView;
    private PolicyAdapter mPolicyAdapter;
    private ProgressBar mLoadingIndicator;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_policy);

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
         * The PolicyAdapter is responsible for linking our policy data with the Views that will end up
         * displaying our policy data.
         */
        mPolicyAdapter = new PolicyAdapter(this, this);

        /*
         * Use mRecyclerView.setAdapter and pass in mPolicyAdapter.
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        mRecyclerView.setAdapter(mPolicyAdapter);

        showLoading();

        // Ensures a loader is initialized and active.
        getSupportLoaderManager().initLoader(ID_POLICY_LOADER, null, this);

    }

    // This method will make the View for the policy data visible and hide the error message.
    private void showPolicyDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the policy View and error
     * message.
     */
    private void showLoading() {
        /* Then, hide the policy data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This method is overridden by the PolicyActivity class in order to handle RecyclerView item
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
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_POLICY_LOADER:
                Uri policyQueryUri = PolicyContract.PolicyEntry.CONTENT_URI;
                return new android.support.v4.content.CursorLoader(this,
                        policyQueryUri,
                        MAIN_POLICY_PROJECTION,
                        null,
                        null,
                        null
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    // Called when a previously created loader has finished its load.
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPolicyAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showPolicyDataView();
    }

    // Called when a previously created loader is being reset, and thus making its data unavailable.
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        mPolicyAdapter.swapCursor(null);
    }
}


