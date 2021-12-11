package com.devchie.videomaker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devchie.videomaker.ads.FacebookAds;
import com.devchie.videomaker.library.VideoListActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.devchie.videomaker.R;
import com.devchie.videomaker.ads.AdmobAds;
import com.devchie.videomaker.dialog.RateDialog;
import com.devchie.videomaker.dialog.SettingDialog;
import com.devchie.videomaker.model.MySharedPreferences;

import java.lang.Thread;
import java.util.List;

public class MainActivity extends BaseSplitActivity implements View.OnClickListener {
    boolean doubleBackToExitPressedOnce = false;
    private long mLastClickTime = 0;
    AdView adView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (!AdmobAds.showFullAdStart((AdmobAds.OnAdsCloseListener) null)) {
            FacebookAds.showFullAds(null);
        }

        setContentView((int) R.layout.activity_main);
        AdmobAds.loadBanner(this);
        AdmobAds.initFullAds(this);
        AdmobAds.loadNativeAds(this, (View) null);

        AudienceNetworkAds.initialize(this);

      //  loadBanner();
//        FacebookAds.initFullAds(this);
//        FacebookAds.loadNativeAd(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
                Log.e("EXCEPTION_IN_THREAD", thread.getName() + " : " + th.getMessage());
            }
        });
        addControls();
    }
    private void loadBanner() {
        adView = new AdView(this, "IMG_16_9_APP_INSTALL#506486199950768_506490359950352", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Toast.makeText(
                        MainActivity.this,
                        "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                FrameLayout aw=findViewById(R.id.admob_banner);
                aw.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };
        AdSettings.addTestDevice("7e852746-0142-4639-8afe-94240cc38d86");
        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    private void addControls() {
        findViewById(R.id.bt_new_project).setOnClickListener(this);
        findViewById(R.id.bt_open_project).setOnClickListener(this);
        findViewById(R.id.bt_rate).setOnClickListener(this);
        findViewById(R.id.bt_share).setOnClickListener(this);
        findViewById(R.id.bt_policy).setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_new_project:
                if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
                    this.mLastClickTime = SystemClock.elapsedRealtime();
                    Dexter.withContext(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                MainActivity.this.startActivity(new Intent(MainActivity.this, SelectedPhotoActivity.class));
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                SettingDialog.showSettingDialog(MainActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                        public void onError(DexterError dexterError) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();
                    return;
                }
                return;
            case R.id.bt_open_project:

                MainActivity.this.startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                return;
            case R.id.bt_policy:

                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
                return;
            case R.id.bt_rate:
                rateApp(false);
                return;
            case R.id.bt_share:
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "My application name");
                    intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "\"https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Choose one"));
                    return;
                } catch (Exception unused) {
                    return;
                }
            default:
                return;
        }
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public void onBackPressed() {
        if (!MySharedPreferences.getInstance(this).getBoolean("rated", false)) {
            rateApp(true);
        } else if (this.doubleBackToExitPressedOnce) {
            showGoodBye();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MainActivity.this.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void rateApp(boolean z) {
        new RateDialog(this, z).show();
    }

    public void showGoodBye() {
        AdmobAds.OnAdsCloseListener onAdsCloseListener = new AdmobAds.OnAdsCloseListener() {
            public void onAdsClose() {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ThankYouActivity.class));
                MainActivity.this.finish();
            }
        };
        if (!AdmobAds.showFullAds(onAdsCloseListener)) {
            onAdsCloseListener.onAdsClose();
        }
    }
}
