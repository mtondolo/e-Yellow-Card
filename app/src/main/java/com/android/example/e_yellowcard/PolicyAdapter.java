package com.android.example.e_yellowcard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyAdapterViewHolder> {

    private String[] mPolicyData;

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

        // Set the text of the TextView to the policy for this list item's position
        String policyItem = mPolicyData[position];
        policyAdapterViewHolder.mPolicyTextView.setText(policyItem);

    }

    /**
     * This method simply returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (null == mPolicyData) return 0;
        return mPolicyData.length;
    }

    /**
     * Cache of the children views for a policy list item.
     */
    public class PolicyAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mPolicyTextView;

        // Constructor for the PolicyAdapter class that accepts a View as a parameter
        public PolicyAdapterViewHolder(View view) {
            super(view);

            // Get a reference to this layout's TextView and save it to mPolicyTextView
            mPolicyTextView = (TextView) view.findViewById(R.id.tv_policy_data);
        }
    }

    /**
     * This method is used to set the news on a PolicyAdapter if we've already created one.
     */
    public void setPolicyData(String[] policyData) {
        mPolicyData = policyData;
        notifyDataSetChanged();
    }
}
