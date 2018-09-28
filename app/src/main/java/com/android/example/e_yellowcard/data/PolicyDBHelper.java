/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.example.e_yellowcard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Manages a local database for policy data.
public class PolicyDBHelper extends SQLiteOpenHelper {

    //This is the name of our database.
    public static final String DATABASE_NAME = "policy.db";

    /*
     * If we change the database schema, we must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 2;

    public PolicyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POLICY_TABLE =
                "CREATE TABLE " + PolicyContract.PolicyEntry.TABLE_NAME + " (" +
                        PolicyContract.PolicyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PolicyContract.PolicyEntry.COLUMN_DAYS + " INTEGER, " +
                        PolicyContract.PolicyEntry.COLUMN_NUMBER + " TEXT, " +
                        PolicyContract.PolicyEntry.COLUMN_REG + " TEXT, " +
                        PolicyContract.PolicyEntry.COLUMN_STATUS + " TEXT" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_POLICY_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PolicyContract.PolicyEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
