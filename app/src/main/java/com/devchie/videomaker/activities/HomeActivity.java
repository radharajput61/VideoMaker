package com.devchie.videomaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devchie.videomaker.R;
import com.devchie.videomaker.dialog.RateDialog;
import com.devchie.videomaker.dialog.SettingDialog;
import com.devchie.videomaker.library.VideoListActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView create_project;
    private long mLastClickTime = 0;
    private LinearLayout btnRate,btnShare,btnPolicy,btnLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        create_project = findViewById(R.id.create_project);
        btnRate = findViewById(R.id.btnRate);
        btnShare = findViewById(R.id.btnShare);
        btnPolicy = findViewById(R.id.btnPolicy);
        btnLibrary = findViewById(R.id.btnLibrary);

        clickListener();
    }

    private void clickListener()
    {
        create_project.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnPolicy.setOnClickListener(this);
        btnLibrary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_project:
                if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
                    this.mLastClickTime = SystemClock.elapsedRealtime();
                    Dexter.withContext(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                HomeActivity.this.startActivity(new Intent(HomeActivity.this, SelectedPhotoActivity.class));
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                SettingDialog.showSettingDialog(HomeActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                        public void onError(DexterError dexterError) {
                            Toast.makeText(HomeActivity.this.getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();
                    return;
                }
            break;

            case R.id.btnRate:
                rateApp(false);
            break;

            case R.id.btnShare:
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "My application name");
                    intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "\"https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Choose one"));
//                    return;
                } catch (Exception unused) {
//                    return;
                }
            break;

            case R.id.btnPolicy:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
            break;

            case R.id.btnLibrary:
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, VideoListActivity.class));
            break;
        }
    }


    private void rateApp(boolean z) {
        new RateDialog(this, z).show();
    }
}