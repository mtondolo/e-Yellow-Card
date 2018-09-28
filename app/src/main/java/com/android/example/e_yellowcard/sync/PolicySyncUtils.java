package com.android.example.e_yellowcard.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class PolicySyncUtils {
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, PolicySyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
