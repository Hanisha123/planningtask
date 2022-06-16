package com.example.taskplannernew.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplannernew.Activity.RetrofitInterface;
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


public class CompletedTaskList extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecentTaskModel> recentTaskModelArrayList;
    SharePref sharePref;
    RecentTaskAdapter adapter;
    RecentTaskModel recentTaskModel;
    String userid,token,usertype;
    GlobalProgressDialog globalProgressDialog;
    String taskstatus;

    public CompletedTaskList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = new SharePref(getActivity());
        globalProgressDialog = new GlobalProgressDialog();
        globalProgressDialog.ProgressDialogShow(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("COMPLETED TASKS");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recent_task, container, false);
        recyclerView = view.findViewById(R.id.recent_task_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        usertype = sharePref.getUserType();


        Log.d("userid::::",""+userid);
        Log.d("usertype::::",""+usertype);

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
                    TaskModel taskModel = new TaskModel();
                    taskModel = response.body();
                    globalProgressDialog.dismissProgressDialog();
                    if (taskModel.getMessage().equalsIgnoreCase("success")) {
                        Log.d("responce", "" + response.body());
                        if (taskModel.getDataTask() == null) {
                            Log.d("notasks", "notasks");
                            Toast.makeText(getActivity(), "There is no data.", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("yestasks","yestasks");
                            globalProgressDialog.dismissProgressDialog();
                            recentTaskModelArrayList = new ArrayList<RecentTaskModel>();
                            for (int i = 0; i < taskModel.getDataTask().size(); i++) {

                                recentTaskModel = new RecentTaskModel();
                                recentTaskModel = taskModel.getDataTask().get(i);
                                Log.d("completedtask11", "completedtask11");
                                // for (RecentTaskModel recentTaskModel : recentTaskModelArrayList) {
                                taskstatus = recentTaskModel.getTaskStatus();
                                Log.d("completedtask12", "completedtask12");
                                if (taskstatus.contains("COMPLETED")) {
                                    recentTaskModelArrayList.add(recentTaskModel);
                                }


                            }
                            adapter = new RecentTaskAdapter(recentTaskModelArrayList, getActivity());
                            recyclerView.setAdapter(adapter);
                            globalProgressDialog.dismissProgressDialog();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), taskModel.getMessage(), Toast.LENGTH_SHORT).show();
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

        return view;
               //return inflater.inflate(R.layout.fragment_recent_task, container, false);

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

