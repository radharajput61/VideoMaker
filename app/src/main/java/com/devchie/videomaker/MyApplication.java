package com.devchie.videomaker;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

//import com.onesignal.OneSignal;

//import com.onesignal.OneSignal;

public class MyApplication extends Application /*MultiDexApplication*/ {



    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    private static final String ONESIGNAL_APP_ID = "bfa517a9-d42a-4246-b647-2fc401ca0c64";
    public void onCreate() {
        super.onCreate();
       // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
       // OneSignal.initWithContext(this);
      //  OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
