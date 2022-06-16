package com.example.taskplannernew.utils;

import android.content.Context;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;

public class GlobalProgressDialog {
    KProgressHUD hud;
    public void ProgressDialogShow(Context context)
    {
        Log.d("hud","hud::");
        if(hud != null){
            hud.dismiss();
            hud = null;
            Log.d("hud","hud::init null");
        }else{
            Log.d("hud","hud::already null");
        }
        try{
            hud = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setMaxProgress(100)
                    .setDimAmount(.3f)
                    .show();
            hud.setProgress(40);

        }catch (Exception ex){
ex.printStackTrace();
        }
            }
    public void dismissProgressDialog()
    {
        Log.d("hud","dismissProgressDialog::already null");
        if(hud != null){
            hud.dismiss();
            hud = null;
            Log.d("hud","dismissProgressDialog::init null");
        }

    }
}
