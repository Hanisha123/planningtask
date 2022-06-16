package com.example.taskplannernew.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.SharePref;

public class SplashScreen extends AppCompatActivity {

    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
  //      setTheme(R.style.Theme_AppCompat_Dialog); //this line i added
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharePref = new SharePref(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        YoYo.with(Techniques.ZoomIn)
                .duration(1500)
                .repeat(0)
                .playOn(findViewById(R.id.img_logo));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharePref.getLoginSuccess()) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(SplashScreen.this, AppLogin.class));
                    finish();
                }
            }
        }, 4000);
    }






}
