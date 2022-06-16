package com.example.taskplannernew.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AppLogin extends AppCompatActivity {
    //SharedPreferences
    SharePref sharePref;

    EditText edittext_username , edittext_pass;
    Button loginNow;

    // Create string variable to hold the EditText Value.
    String UsenameHolder, PasswordHolder;

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Storing server url into String variable.
    String LoginUrl = "http://abgo.in/api/";

    Boolean CheckEditText;

    RelativeLayout relativelayout;

    Button signUp;
    Button forgotPassword;
    GlobalProgressDialog globalProgressDialog;

    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000);

        sharePref = new SharePref(this);
        globalProgressDialog = new GlobalProgressDialog();


        signUp = (Button) findViewById(R.id.signUp);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);

        // Assigning ID's to EditText.
        edittext_username = (EditText) findViewById(R.id.edittext_username);

        edittext_pass = (EditText) findViewById(R.id.edittext_password);

        // Assigning ID's to Button.
        loginNow = (Button) findViewById(R.id.loginNow);

        // Assigning Activity this to progress dialog.
        progressDialog = new ProgressDialog(AppLogin.this);

        // Adding click listener to button.
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    globalProgressDialog.ProgressDialogShow(AppLogin.this);
                    Loginretro();

                } else {

                    Toast.makeText(AppLogin.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    // Creating user login function.
    public void Loginretro() {
        //creating the json object to send
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("txt_username", edittext_username.getText().toString().trim());
        jsonObject.addProperty("pwd_password", edittext_pass.getText().toString().trim());
        // Using the Retrofit
        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, LoginUrl);
        Call<LoginModel> call = jsonPostService.getAllexpenseData(jsonObject);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
                globalProgressDialog.dismissProgressDialog();

                try {
                    globalProgressDialog.dismissProgressDialog();

                    LoginModel login = new LoginModel();
                    login = response.body();

                    Log.e("response-success", String.valueOf(login));

                    //Log.e("response-success", String.valueOf(login));
                    Log.d("success","success");
                   // String loginid = userModels.get(i).getId();
                    // MainPreferences.setIsLogin(LoginActivity.this, true);
                    //MainPreferences.setUser_img(LoginActivity.this, login.getProfilePic());
                   // MainPreferences.setUserID(LoginActivity.this, login.getId());
                   // MainPreferences.setO_email(LoginActivity.this, login.getOEmail());
                   // MainPreferences.setO_number(LoginActivity.this, login.getONumber());
                    Intent intent = new Intent(AppLogin.this, MainActivity.class);
                    startActivity(intent);
                    sharePref.setLoginSuccess(true);
                    sharePref.setTOKENSuccess(true);
                    sharePref.setLoginId(this,login.getUserId());
                    sharePref.setTOKEN(this,login.getToken());
                    sharePref.setUserType(this,login.getUserType());
                    sharePref.setEmpName(this,login.getEmpName());
                    Log.d("success1",login.getUserId());
                    Log.d("token", "" + login.getToken());
                    Log.d("Name", "" + login.getEmpName());
                    Toast.makeText(AppLogin.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                    globalProgressDialog.dismissProgressDialog();

                    //finish();
                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail","fail");
                Log.e("response-failure", call.toString());
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {

        // Getting values from EditText.
        UsenameHolder = edittext_username.getText().toString().trim();
        PasswordHolder = edittext_pass.getText().toString().trim();

        // Checking whether EditText value is empty or not.
        if (TextUtils.isEmpty(UsenameHolder) || TextUtils.isEmpty(PasswordHolder)) {

            // If any of EditText is empty then set variable value as False.
            CheckEditText = false;

        } else {

            // If any of EditText is filled then set variable value as True.
            CheckEditText = true;
        }
    }
}
