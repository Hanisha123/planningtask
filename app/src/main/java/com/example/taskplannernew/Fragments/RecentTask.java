package com.example.taskplannernew.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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


public class RecentTask extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecentTaskModel> recentTaskModelArrayList;
    SharePref sharePref;
    RecentTaskAdapter adapter;
    RecentTaskModel recentTaskModel;
    String userid,token,usertype;
    GlobalProgressDialog globalProgressDialog;


    public RecentTask() {
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
        toolbarTextView.setText("RECENT TASKS");
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
                        Log.d("responce",""+response.body());

                        globalProgressDialog.dismissProgressDialog();
                        recentTaskModelArrayList = new ArrayList<RecentTaskModel>();
                        for (int i = 0; i < taskModel.getDataTask().size(); i++) {
                            recentTaskModel = new RecentTaskModel();

                            recentTaskModel = taskModel.getDataTask().get(i);
                            recentTaskModelArrayList.add(recentTaskModel);
                        }
                        adapter = new RecentTaskAdapter(recentTaskModelArrayList, getActivity());
                        recyclerView.setAdapter(adapter);
                        globalProgressDialog.dismissProgressDialog();
                    } else {
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

