package com.example.taskplannernew.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.TaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class ChangeTATActivity extends AppCompatActivity {

    SharePref sharePref;

    EditText comment_changetat;
    TextView task_subject,start_date,end_date;

    String subject1,startdate1,enddate1,taskid,comment;
    LinearLayout from_date_linearlayout, to_date_linearlayout;
    String dat_from;
    Bundle bundle;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatterServer;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    Button btn_updatetat;
    String userid,token;
    GlobalProgressDialog globalProgressDialog;
    Date date_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tat);
        TextView toolbarTextView = (TextView)findViewById(R.id.heading);
        toolbarTextView.setText("CHANGE TAT");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sharePref = new SharePref(this);
        globalProgressDialog = new GlobalProgressDialog();

        comment_changetat = (EditText)findViewById(R.id.comment_changetat);
        from_date_linearlayout = (LinearLayout)findViewById(R.id.from_date_linearlayout);
        to_date_linearlayout = (LinearLayout)findViewById(R.id.to_date_linearlayout);
        task_subject = (TextView) findViewById(R.id.task_subject);
        start_date = (TextView)findViewById(R.id.start_date);
        end_date = (TextView)findViewById(R.id.end_date);
        btn_updatetat = (Button)findViewById(R.id.btn_updatetat);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt = (EditText)findViewById(R.id.fromDateEtxt);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText)findViewById(R.id.toDateEtxt);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        bundle = getIntent().getExtras();
        taskid = getIntent().getExtras().getString("task_id");
        subject1 = getIntent().getExtras().getString("subject");
        startdate1 =getIntent().getExtras().getString("fromdate");
        enddate1 = getIntent().getExtras().getString("todate");
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();

        Log.d("subject", "" + subject1);
        Log.d("startdate", "" + startdate1);
        Log.d("enddate", "" + enddate1);
        Log.d("token", "" + token);
        Log.d("taskid", "" + taskid);

        task_subject.setText(subject1);
        start_date.setText(startdate1);
        end_date.setText(enddate1);

        setDateTimeField();
        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromDatePickerDialog.show();

            }
        });

        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toDatePickerDialog.show();

            }
        });

        btn_updatetat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalProgressDialog.ProgressDialogShow(ChangeTATActivity.this);
                UpdateChangeTAT();
            }
        });
    }

    private void UpdateChangeTAT() {

        // dat_from = fromDateEtxt.getText().toString(); //15-09-2019

        comment = comment_changetat.getText().toString();
        if (comment.isEmpty()){
            globalProgressDialog.dismissProgressDialog();
            Toast.makeText(ChangeTATActivity.this, "Comment cannot be Empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        date_from = null;
        dat_from = fromDateEtxt.getText().toString();
        try {
            date_from = dateFormatter.parse(dat_from);
            dat_from = dateFormatterServer.format(date_from);
        } catch (ParseException e) {
            e.printStackTrace();
            globalProgressDialog.dismissProgressDialog();
            Toast.makeText(ChangeTATActivity.this, "Please select from date", Toast.LENGTH_SHORT).show();
            return;

        }


        //comment = comment_changetat.getText().toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "TatUpdate");
        jsonObject.addProperty("task_id", taskid);
        jsonObject.addProperty("new_tat", dat_from);
        jsonObject.addProperty("comment", comment);

        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<TaskModel> call = jsonPostService.updateTask(jsonObject);
        call.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {

                Log.i("Responsestring", ""+response);
                Log.i("Responsestring", "onResponse: ConfigurationListener::"+call.request().url());
                try {
                    globalProgressDialog.dismissProgressDialog();
                    if (response.body().getMessage().equalsIgnoreCase("TAT Updated")) {
                        globalProgressDialog.dismissProgressDialog();
                        ClearAllFields();
                        Toast.makeText(ChangeTATActivity.this, "TAT Updated", Toast.LENGTH_LONG).show();
                    } else {
                        globalProgressDialog.dismissProgressDialog();
                        Toast.makeText(ChangeTATActivity.this, "Kindly fill all the fields properly.", Toast.LENGTH_LONG).show();
                        Log.d("Error Message", response.body().getMessage());
                    }
                }
                catch (Exception e)
                {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(ChangeTATActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.i("onFailure", t.getLocalizedMessage());
                Log.i("onFailure", "onResponse: ConfigurationListener::"+call.request().url());
                t.printStackTrace();
            }

        });

    }

    private void ClearAllFields() {
        fromDateEtxt.setText("");
        comment_changetat.setText("");
    }

    //set date and time field on click on calendar
    private void setDateTimeField() {
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(ChangeTATActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(ChangeTATActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
