package com.example.taskplannernew.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Adapter.RecentTaskAdapter;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.Models.TaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class MemberTasksActivity extends AppCompatActivity {
    GlobalProgressDialog globalProgressDialog;
    SharePref sharePref;
    RecyclerView recyclerView;
    ArrayList<RecentTaskModel> recentTaskModelArrayList;
    RecentTaskAdapter adapter;
    RecentTaskModel recentTaskModel;
    String userid, token, usertype, empid, assignedid;
    Bundle arguments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_tasks);
        TextView toolbarTextView = (TextView)findViewById(R.id.heading);
        toolbarTextView.setText("MEMBER TASK");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sharePref = new SharePref(this);
        globalProgressDialog = new GlobalProgressDialog();
        globalProgressDialog.ProgressDialogShow(MemberTasksActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recent_task_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(MemberTasksActivity.this));
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        usertype = sharePref.getUserType();
        arguments = getIntent().getExtras();
        empid = arguments.getString("emp_id");
        assignedid = sharePref.getAssignedId();
        Log.d("userid::::", "" + userid);
        Log.d("usertype::::", "" + usertype);
        Log.d("getemp_id::::", "" + empid);
        Log.d("getassignedid::::", "" + assignedid);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "AllTaskList");


        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<TaskModel> call = jsonPostService.getAllTaskData(jsonObject);
        call.enqueue(new Callback<TaskModel>() {

            @Override
            public void onResponse(Call<TaskModel> call, retrofit2.Response<TaskModel> response) {
                globalProgressDialog.dismissProgressDialog();
                try {
                    globalProgressDialog.dismissProgressDialog();
                    // if(assignedid.equals(empid)) {
                    TaskModel taskModel = new TaskModel();
                    taskModel = response.body();
                    recentTaskModelArrayList = new ArrayList<RecentTaskModel>();
                    for (int i = 0; i < taskModel.getDataTask().size(); i++) {
                        if (taskModel.getDataTask().get(i).getAssigntoId().equals(empid)) {

                            recentTaskModel = new RecentTaskModel();

                            recentTaskModel = taskModel.getDataTask().get(i);
                            recentTaskModelArrayList.add(recentTaskModel);
                            adapter = new RecentTaskAdapter(recentTaskModelArrayList, MemberTasksActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }


                    globalProgressDialog.dismissProgressDialog();
                    if (taskModel.getMessage().equalsIgnoreCase("success")) {
                        Log.d("responce", "" + response.body());

                        globalProgressDialog.dismissProgressDialog();

                        globalProgressDialog.dismissProgressDialog();
                    } else {
                        Toast.makeText(MemberTasksActivity.this, taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                        globalProgressDialog.dismissProgressDialog();
                    }


                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail", "fail");
                Log.e("response-failure", call.toString());
            }
        });
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
