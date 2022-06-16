package com.example.taskplannernew.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.taskplannernew.Activity.RetrofitInterface;
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


public class ChangeTATFragment extends Fragment {

    private Calendar.OnFragmentInteractionListener mListener;
    SharePref sharePref;

    EditText comment_changetat;
    TextView task_subject,start_date,end_date;

    String subject1,startdate1,enddate1,taskid,comment;
    LinearLayout from_date_linearlayout, to_date_linearlayout;


    String dat_from;

    Bundle arguments;


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


    public ChangeTATFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = new SharePref(getActivity());
        globalProgressDialog = new GlobalProgressDialog();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("CHANGE TAT");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_tat, container, false);

        globalProgressDialog.dismissProgressDialog();
        comment_changetat = view.findViewById(R.id.comment_changetat);
        from_date_linearlayout = view.findViewById(R.id.from_date_linearlayout);
        to_date_linearlayout = view.findViewById(R.id.to_date_linearlayout);
        task_subject = view.findViewById(R.id.task_subject);
        start_date = view.findViewById(R.id.start_date);
        end_date = view.findViewById(R.id.end_date);
        btn_updatetat = view.findViewById(R.id.btn_updatetat);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt = view.findViewById(R.id.fromDateEtxt);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = view.findViewById(R.id.toDateEtxt);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        arguments = getArguments();
        subject1 = arguments.getString("subject");
        startdate1 = arguments.getString("fromdate");
        enddate1 = arguments.getString("todate");
        taskid = arguments.getString("task_id");
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
                globalProgressDialog.ProgressDialogShow(getActivity());
                UpdateChangeTAT();
            }
        });
        return view;
        //return inflater.inflate(R.layout.fragment_recent_task, container, false);

    }

    private void UpdateChangeTAT() {

       // dat_from = fromDateEtxt.getText().toString(); //15-09-2019

        comment = comment_changetat.getText().toString();
        if (comment.isEmpty()){
            globalProgressDialog.dismissProgressDialog();
            Toast.makeText(getActivity(), "Comment cannot be Empty.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please select from date", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "TAT Updated", Toast.LENGTH_LONG).show();
                    } else {
                        globalProgressDialog.dismissProgressDialog();
                        Toast.makeText(getActivity(), "Kindly fill all the fields properly.", Toast.LENGTH_LONG).show();
                        Log.d("Error Message", response.body().getMessage());
                    }
                }
                catch (Exception e)
                {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();

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
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onPause() {
        super.onPause();
        globalProgressDialog.dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalProgressDialog.dismissProgressDialog();
    }

}

