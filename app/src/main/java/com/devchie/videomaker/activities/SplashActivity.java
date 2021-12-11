package com.devchie.videomaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;


import com.devchie.videomaker.ads.AdManager;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

import com.devchie.videomaker.R;
import com.devchie.videomaker.ads.AdmobAds;

public class SplashActivity extends BaseSplitActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3500;


    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        MobileAds.initialize(this);
        AudienceNetworkAds.initialize(this);
        AdmobAds.initFullAdStart(this);

        LinearLayout adContainer = findViewById(R.id.banner_container);

        if (!AdManager.isloadFbMAXAd) {
            //admob
            AdManager.initAd(SplashActivity.this);
            AdManager.loadBannerAd(SplashActivity.this, adContainer);
            AdManager.maxInterstital(SplashActivity.this);
        } else {

//            MAX + Fb banner Ads
            AdManager.initMAX(SplashActivity.this);
            AdManager.maxBanner(SplashActivity.this, adContainer);
            AdManager.maxInterstital(SplashActivity.this);

        }


        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startToMainActivity();
            }
        }, 3500);

    }


    public void startToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void startToHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    public void onDestroy() {
        super.onDestroy();
    }


}
