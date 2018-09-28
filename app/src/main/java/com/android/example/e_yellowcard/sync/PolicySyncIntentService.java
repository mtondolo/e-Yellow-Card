package com.android.example.e_yellowcard.sync;

import android.app.IntentService;
import android.content.Intent;

public class PolicySyncIntentService extends IntentService {

    // Creates an IntentService.  Invoked by our subclass's constructor.
    public PolicySyncIntentService() {
        super("PolicySyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PolicySyncTask.syncPolicy(this);
    }
}
