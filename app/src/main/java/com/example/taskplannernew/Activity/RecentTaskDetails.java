package com.example.taskplannernew.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Adapter.AllTaskAdapter;
import com.example.taskplannernew.Fragments.AllTaskActivityFragment;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.ActivityModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class RecentTaskDetails extends AppCompatActivity {
    SharePref sharePref;
    Bundle bundle;
    String userid,token,taskid;
    RecyclerView recyclerView;
    ArrayList<ActivityTaskModel> activityTaskModelArrayList;
    AllTaskAdapter adapter;
    ActivityTaskModel activityTaskModel;
    String taskstatus,usertype;
    Bundle arguments;
    GlobalProgressDialog globalProgressDialog;
    TextView nodata_found;
    FloatingActionButton add_activity_fab,fab_reopen_approve ;
    String jsonObjectStr, response, message;
    FloatingActionButton fab, fab_addactivity, fab_approve, fab_reopen;
    LinearLayout fabLayout_addactivity, fabLayout_approve, fabLayout_reopen;
    View fabBGLayout , fabBGLayout_reopen_approve;
    boolean isFABOpen = false;
    TextView subjectId;
    LinearLayout fabbutton;
    TextView subject_detail,fromdate_details,todate_details,assignedby_details,assignto_details,
            brand_details,task_status,filename_detail,description_detail;
    String subject, fromdate , todate , assignby , assignto , brand , filename , description;
    TextView reassign_text,changetat_text;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_task_details);
        globalProgressDialog = new GlobalProgressDialog();
        TextView toolbarTextView = (TextView)findViewById(R.id.heading);
        toolbarTextView.setText("RE ASSIGN TASK");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TASK DETAILS");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sharePref = new SharePref(this);
        usertype = sharePref.getUserType();
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::",""+userid);
        Log.d("token::::",""+token);
        bundle = getIntent().getExtras();
        taskid = getIntent().getExtras().getString("task_id");
        taskstatus = getIntent().getExtras().getString("task_status");
        Log.d("taskid", "" + taskid);
        Log.d("task_status", "" + taskstatus);

        recyclerView = (RecyclerView)findViewById(R.id.recent_task_rv);
        nodata_found = (TextView) findViewById(R.id.nodata_found);
        add_activity_fab = (FloatingActionButton)findViewById(R.id.add_activity_fab);
        fabLayout_approve = (LinearLayout) findViewById(R.id.fabLayout_approve);

        fabLayout_addactivity = (LinearLayout) findViewById(R.id.fabLayout_addactivity);
        fabLayout_approve = (LinearLayout) findViewById(R.id.fabLayout_approve);
        fabLayout_reopen = (LinearLayout) findViewById(R.id.fabLayout_reopen);
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_reopen_approve = (FloatingActionButton) findViewById(R.id.fab_reopen_approve);
        fab_addactivity = (FloatingActionButton) findViewById(R.id.fab_addactivity);
        fab_approve = (FloatingActionButton) findViewById(R.id.fab_approve);
        fab_reopen = (FloatingActionButton)findViewById(R.id.fab_reopen);
        fabBGLayout = findViewById(R.id.fabBGLayout);
        fabBGLayout_reopen_approve = findViewById(R.id.fabBGLayout_reopen_approve);
        fabbutton = (LinearLayout)findViewById(R.id.fabbutton);

        subject_detail = (TextView) findViewById(R.id.subject_detail);
        fromdate_details = (TextView) findViewById(R.id.fromdate_details);
        todate_details = (TextView) findViewById(R.id.todate_details);
        assignedby_details = (TextView) findViewById(R.id.assignedby_details);
        assignto_details = (TextView) findViewById(R.id.assignto_details);
        brand_details = (TextView) findViewById(R.id.brand_details);
        task_status = (TextView) findViewById(R.id.task_status);
        filename_detail = (TextView) findViewById(R.id.filename_detail);
        description_detail = (TextView) findViewById(R.id.description_detail);

        reassign_text = (TextView) findViewById(R.id.reassign_text);
        changetat_text = (TextView) findViewById(R.id.changetat_text);

        subject = getIntent().getExtras().getString("subject");
        fromdate = getIntent().getExtras().getString("fromdate");
        todate = getIntent().getExtras().getString("todate");
        assignby = getIntent().getExtras().getString("assignby");
        assignto = getIntent().getExtras().getString("assignto");
        brand = getIntent().getExtras().getString("brand_name");
        taskstatus = getIntent().getExtras().getString("task_status");
        filename = getIntent().getExtras().getString("filename");
        description = getIntent().getExtras().getString("taskdescription");

        subject_detail.setText(subject);
        fromdate_details.setText(fromdate);
        todate_details.setText(todate);
        assignedby_details.setText(assignby);
        assignto_details.setText(assignto);
        brand_details.setText(brand);
        task_status.setText(taskstatus);
        filename_detail.setText(filename);
        description_detail.setText(description);

        reassign_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reassign_task = new Intent(RecentTaskDetails.this,Re_Assign_Activity.class);
                reassign_task.putExtra("task_id", taskid);
                Log.d("taskid_abc",taskid);
                startActivity(reassign_task);
            }
        });

        changetat_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changetat = new Intent(RecentTaskDetails.this,ChangeTATActivity.class);
                changetat.putExtra("task_id", taskid);
                changetat.putExtra("subject", subject);
                changetat.putExtra("fromdate", fromdate);
                changetat.putExtra("todate", todate);
                Log.d("taskid_abc",taskid);
                startActivity(changetat);
            }
        });

        fab_addactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addactivity = new Intent(RecentTaskDetails.this,AddActivity_Activity.class);
                startActivity(addactivity);
                closeFABMenu1();
            }
        });


        fabbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello","hello");
                if (!isFABOpen) {
                    showFABMenu1();
                } else {
                    closeFABMenu1();
                }
            }
        });
        //View For AddActivity
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu1();
                Log.d("bye","bye");
            }
        });
        fabLayout_reopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RecentTaskDetails.this);
                builder1.setMessage("Do you really want to re-open the task?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //new AllTaskActivityFragment.RegisterAsyncTask5(getActivity()).execute();
                                RecentTaskDetails recentTaskDetails = new RecentTaskDetails();
                                new RegisterAsyncTask5(recentTaskDetails).execute();
                                closeFABMenu1();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeFABMenu1();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        });

        fabLayout_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RecentTaskDetails.this);
                builder1.setMessage("Do you want to approve the task?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Toast.makeText(view.getContext(), "Approved.", Toast.LENGTH_LONG).show();
                                //finish();
                                // new AllTaskActivityFragment.RegisterAsyncTask6(getActivity()).execute();
                                RecentTaskDetails recentTaskDetails = new RecentTaskDetails();
                                new RegisterAsyncTask6(recentTaskDetails).execute();
                                //globalProgressDialog.ProgressDialogShow(getActivity());
                                closeFABMenu1();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeFABMenu1();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        });

        fabLayout_addactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFragment(new AddActivityFragment());

            }
        });
        if(usertype.contains("MANAGER"))
        {
            Log.d("managerbottom","managerbottom");
            reassign_text.setVisibility(View.VISIBLE);
            changetat_text.setVisibility(View.VISIBLE);
        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("executivebottom","executivebottom");
            reassign_text.setVisibility(View.INVISIBLE);
            changetat_text.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.d("nomanexe","nomanexe");
            (Toast.makeText(RecentTaskDetails.this, "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }

        //STATUS COLORED TEXT
        if(taskstatus.equals("PENDING")){
            task_status.setTextColor(Color.parseColor("#ef5350"));
        } else if(taskstatus.equals("ON-PROCESS")){
            task_status.setTextColor(Color.parseColor("#ffb22b"));
        } else if(taskstatus.equals("RE-OPEN")){
            task_status.setTextColor(Color.parseColor("#ffb22b"));
        } else if(taskstatus.equals("COMPLETED")){
            task_status.setTextColor(Color.parseColor("#26dad2"));
        }
    /*    if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);

            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_reopen.setVisibility(View.VISIBLE);
                fabLayout_approve.setVisibility(View.VISIBLE);
            }
        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("usertype31", "" + usertype);
            isFABOpen = true;
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(RecentTaskDetails.this, "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }*/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("task_id", taskid);
        jsonObject.addProperty("action", "AllActivityTask");
        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<ActivityModel> call = jsonPostService.getAllActivity(jsonObject);
        call.enqueue(new Callback<ActivityModel>() {
            @Override
            public void onResponse(Call<ActivityModel> call, retrofit2.Response<ActivityModel> response) {
//                globalProgressDialog.dismissProgressDialog();
                try {
                    globalProgressDialog.dismissProgressDialog();
                    ActivityModel actalltask = new ActivityModel();
                    actalltask = response.body();

                    if (actalltask.getMessage().equalsIgnoreCase("success")) {
                        activityTaskModelArrayList = new ArrayList<ActivityTaskModel>();
                        if(actalltask.getDataTaskActivity()==null){
                            nodata_found.setVisibility(View.VISIBLE);
                            Log.d("nodata","nodata");
                            Toast.makeText(RecentTaskDetails.this,"There is no Data Task Activities",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Log.d("yesdata","yesdata");
                            for (int i = 0; i < actalltask.getDataTaskActivity().size(); i++) {
                                activityTaskModel = new ActivityTaskModel();

                                activityTaskModel = actalltask.getDataTaskActivity().get(i);
                                activityTaskModelArrayList.add(activityTaskModel);
                                Log.i("activityTaskModel","activityTaskModel:"+activityTaskModel.getActivityId());
                            }
                            adapter = new AllTaskAdapter(activityTaskModelArrayList, RecentTaskDetails.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(RecentTaskDetails.this));
                            recyclerView.setAdapter(adapter);
                            globalProgressDialog.dismissProgressDialog();
                        }

                    } else {
                        globalProgressDialog.dismissProgressDialog();
                        Toast.makeText(RecentTaskDetails.this, actalltask.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ActivityModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail", "fail");
                Log.e("response-failure", call.toString());
                Toast.makeText(RecentTaskDetails.this,"There is no Data Task Activities",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("RestrictedApi")
    private void showFABMenu1() {
        isFABOpen = true;
        Log.d("usertype11", "" + usertype);
        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
               // fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_reopen.setVisibility(View.VISIBLE);
                fabLayout_approve.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                //fab.animate().rotationBy(180);
                //fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("usertype31", "" + usertype);
            isFABOpen = true;
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(RecentTaskDetails.this, "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }
    }

    private void closeFABMenu1() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        //fab.animate().rotation(0);
        fabLayout_addactivity.animate().translationY(0);
        fabLayout_approve.animate().translationY(0);
        fabLayout_reopen.animate().translationY(0);
        fabLayout_approve.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout_addactivity.setVisibility(View.GONE);
                    fabLayout_approve.setVisibility(View.GONE);
                    fabLayout_reopen.setVisibility(View.GONE);
                }
/*                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }*/
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    //REOPEN API
    class RegisterAsyncTask5 extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask5(Activity activity) {
            this.activity = activity;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }


        @Override
        public String doInBackground(String... params) {
            MultipartEntity multipartEntity;
            HttpPost post;
            HttpClient client;
            client = new DefaultHttpClient();
            post = new HttpPost("http://abgo.in/api/approve_reopen.php");

            multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                multipartEntity.addPart("user_id", new StringBody(userid));
                multipartEntity.addPart("token", new StringBody(token));
                multipartEntity.addPart("action", new StringBody("reOpen"));
                multipartEntity.addPart("task_id", new StringBody(taskid));
                post.setEntity(multipartEntity);
                HttpResponse responserr = null;
                responserr = client.execute(post);
                HttpEntity resEntity = responserr.getEntity();
                jsonObjectStr = EntityUtils.toString(resEntity);
                jsonObject = new JSONObject(jsonObjectStr);
                response = jsonObject.getString("message");

                if (response.contains("Task has been reopen")) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            globalProgressDialog.dismissProgressDialog();
                            Toast.makeText(RecentTaskDetails.this, "Task has been reopen.", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(RecentTaskDetails.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    message = jsonObject.getString("message");
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (jsonObject.has("message")) {
                    globalProgressDialog.dismissProgressDialog();
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                globalProgressDialog.dismissProgressDialog();
            }
        }
    }

    //APPROVE API

    class RegisterAsyncTask6 extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask6 (Activity activity) {
            this.activity = activity;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }


        @Override
        public String doInBackground(String... params) {
            MultipartEntity multipartEntity;
            HttpPost post;
            HttpClient client;
            client = new DefaultHttpClient();
            post = new HttpPost("http://abgo.in/api/approve_reopen.php");

            multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                multipartEntity.addPart("user_id", new StringBody(userid));
                multipartEntity.addPart("token", new StringBody(token));
                multipartEntity.addPart("action", new StringBody("approve"));
                multipartEntity.addPart("task_id", new StringBody(taskid));
                post.setEntity(multipartEntity);
                HttpResponse responserr = null;
                responserr = client.execute(post);
                HttpEntity resEntity = responserr.getEntity();
                jsonObjectStr = EntityUtils.toString(resEntity);
                jsonObject = new JSONObject(jsonObjectStr);
                response = jsonObject.getString("message");

                if (response.contains("Task has been approved")) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            globalProgressDialog.dismissProgressDialog();
                            Toast.makeText(RecentTaskDetails.this, "Task has been approved", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(RecentTaskDetails.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    message = jsonObject.getString("message");
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (jsonObject.has("message")) {
                    globalProgressDialog.dismissProgressDialog();
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                globalProgressDialog.dismissProgressDialog();
            }
        }
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
