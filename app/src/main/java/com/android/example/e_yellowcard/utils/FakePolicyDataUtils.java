package com.android.example.e_yellowcard.utils;

import android.content.ContentValues;
import android.content.Context;

import com.android.example.e_yellowcard.data.PolicyContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakePolicyDataUtils {

    // Creates a single ContentValues object with random policy data
    private static ContentValues createTestPolicyContentValues() {
        ContentValues testNewsValues = new ContentValues();
        testNewsValues.put(PolicyContract.PolicyEntry.COLUMN_DAYS, getPolicyString());
        testNewsValues.put(PolicyContract.PolicyEntry.COLUMN_NUMBER, getPolicyString());
        testNewsValues.put(PolicyContract.PolicyEntry.COLUMN_REG, getPolicyString());
        testNewsValues.put(PolicyContract.PolicyEntry.COLUMN_STATUS, getPolicyString());
        return testNewsValues;
    }

    // Creates random policy data 7 times
    public static void insertFakePolicyData(Context context) {
        List<ContentValues> fakeValues = new ArrayList<>();

        //loop over 7 times
        for (int i = 0; i < 8; i++) {
            fakeValues.add(FakePolicyDataUtils.createTestPolicyContentValues());
        }

        // Insert our new policy data into Policy's Database
        context.getContentResolver().bulkInsert(
                PolicyContract.PolicyEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[8]));
    }

    public static String getPolicyString() {
        String POLICYCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder policy = new StringBuilder();
        Random rnd = new Random();
        while (policy.length() < 18) {

            // length of the random string.
            int index = (int) (rnd.nextFloat() * POLICYCHARS.length());
            policy.append(POLICYCHARS.charAt(index));
        }
        String policyStr = policy.toString();
        return policyStr;
    }
}
