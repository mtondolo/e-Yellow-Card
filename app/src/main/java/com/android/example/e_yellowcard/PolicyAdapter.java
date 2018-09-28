package com.android.example.e_yellowcard;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyAdapterViewHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final PolicyAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface PolicyAdapterOnClickHandler {
        void onClick(String policyItem);
    }

    private Cursor mCursor;

    /**
     * Creates a PolicyAdapter.
     */
    public PolicyAdapter(Context context, PolicyAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created.
     */
    @Override
    public PolicyAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Inflate the list item xml into a view
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.policy_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        // Return a new PolicyAdapterViewHolder with the above view passed in as a parameter
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PolicyAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     */
    @Override
    public void onBindViewHolder(PolicyAdapterViewHolder policyAdapterViewHolder, int position) {

        // Move the cursor to the appropriate position
        mCursor.moveToPosition(position);

        /*******************
         * Policy Item *
         *******************/

        // Read numdays, yellowCardNumber, vehicleRegistrationNumber and name from the cursor
        String numdays = mCursor.getString(PolicyActivity.INDEX_DAYS);
        String yellowCardNumber = mCursor.getString(PolicyActivity.INDEX_NUMBER);
        String vehicleRegistrationNumber = mCursor.getString(PolicyActivity.INDEX_REG);
        String status = mCursor.getString(PolicyActivity.INDEX_STATUS);

        // Display the summary that we created above
        String policyItem = numdays + " - " + yellowCardNumber
                + " - " + vehicleRegistrationNumber + " - " + status;
        policyAdapterViewHolder.mPolicyTextView.setText(policyItem);

    }

    /**
     * This method simply returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    // Swaps the cursor used by the PolicyAdapter for its policy data.
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a policy list item.
     */
    public class PolicyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mPolicyTextView;

        // Constructor for the PolicyAdapter class that accepts a View as a parameter
        public PolicyAdapterViewHolder(View view) {
            super(view);

            // Get a reference to this layout's TextView and save it to mPolicyTextView
            mPolicyTextView = (TextView) view.findViewById(R.id.tv_policy_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String policyItem = mPolicyTextView.getText().toString();
            mClickHandler.onClick(policyItem);
        }
    }
}
