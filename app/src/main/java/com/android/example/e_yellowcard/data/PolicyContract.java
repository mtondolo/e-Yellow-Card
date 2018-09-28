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

import android.net.Uri;
import android.provider.BaseColumns;

public class PolicyContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.
     */
    public static final String CONTENT_AUTHORITY = "com.android.example.e_yellowcard";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Policy
     * can handle. For instance.
     */
    public static final String PATH_POLICY = "policy";

    /* Inner class that defines the table contents of the policy table */
    public static final class PolicyEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POLICY)
                .build();

        /* Used internally as the name of our policy table. */
        public static final String TABLE_NAME = "policy";

        /* days is stored as int representing number of days */
        public static final String COLUMN_DAYS = "numdays";

        /* number is stored as a string representing the policy number */
        public static final String COLUMN_NUMBER = "yellowCardNumber";

        /* reg is stored as a string representing the vehicle registration number */
        public static final String COLUMN_REG = "vehicleRegistrationNumber";

        /* status is stored as a string representing the policy status */
        public static final String COLUMN_STATUS = "status";

    }
}
