package com.example.taskplannernew.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Activity.AppLogin;
import com.example.taskplannernew.Activity.InProgressDetailActivity;
import com.example.taskplannernew.Activity.LoginModel;
import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.Activity.RecentTaskDetails;
import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Adapter.AllTaskAdapter;
import com.example.taskplannernew.Adapter.RecentTaskAdapter;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.ActivityModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.DashboardModel;
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


public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    ArrayList<RecentTaskModel> recentTaskModelArrayList;
    SharePref sharePref;
    String userid,token,usertype,emp_name;
    RecentTaskAdapter adapter;
    RecentTaskModel recentTaskModel;
    TextView total_team_text,in_progress_text,complete_task_text,pending_task_text,user_name;
    DashboardModel dashboardModel;
    GlobalProgressDialog globalProgressDialog;
    LinearLayout total_team_members,in_progress_members,completed_task_list,pending_task_list;



    public HomeFragment() {
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
        toolbarTextView.setText("TASK PLANNER");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        usertype = sharePref.getUserType();
        emp_name = sharePref.getEmpName();
        Log.d("empname",""+emp_name);
        total_team_text = view.findViewById(R.id.total_team_text);
        in_progress_text = view.findViewById(R.id.in_progress_text);
        complete_task_text = view.findViewById(R.id.complete_task_text);
        pending_task_text = view.findViewById(R.id.pending_task_text);
        user_name = view.findViewById(R.id.user_name);
        user_name.setText("Hello, " +emp_name);
        total_team_members = view.findViewById(R.id.total_team_members);
        in_progress_members = view.findViewById(R.id.in_progress_members);
        completed_task_list = view.findViewById(R.id.completed_task_list);
        pending_task_list = view.findViewById(R.id.pending_task_list);

        total_team_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usertype.contains("MANAGER")) {
                    Log.d("manager","manager");
                    Fragment fragment = new AllJuniorsFragment();
                    FragmentManager fm = (getActivity()).getSupportFragmentManager();
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    //fragmentTransaction.addToBackStack(null);
                    FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                    fragmentTransaction.commit();
                }
                else{
                    Log.d("executive","executive");
                    Toast.makeText(getActivity(),"You don't have any Executive.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        in_progress_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OnprocessTaskList();
                FragmentManager fm = (getActivity()).getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                fragmentTransaction.commit();

            }
        });

        completed_task_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CompletedTaskList();
                FragmentManager fm = (getActivity()).getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                fragmentTransaction.commit();
            }
        });

        pending_task_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PendingTaskList();
                FragmentManager fm = (getActivity()).getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                fragmentTransaction.commit();
            }
        });

        //recyclerView = view.findViewById(R.id.recent_task_rv);
       // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getDashboardDetails();

        //getRecentTaskList();
        return view;
    }

    private void getDashboardDetails() {

        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "dashBoard");

        // Using the Retrofit
        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<DashboardModel> call = jsonPostService.getDashboardDetail(jsonObject);
        call.enqueue(new Callback<DashboardModel>() {
            @Override
            public void onResponse(Call<DashboardModel> call, retrofit2.Response<DashboardModel> response) {
                globalProgressDialog.dismissProgressDialog();
                try {



                    Log.i("responce_demo7", call.toString());
                    globalProgressDialog.dismissProgressDialog();
                    DashboardModel dashboard = new DashboardModel();
                    dashboard = response.body();
                    Log.d("responce_demo4", String.valueOf(response));
                    Log.d("responce_demo5",response.body().toString());
                    Log.d("responce_demo5",jsonObject.toString());
                    Log.d("responce_demo6",jsonPostService.toString());


                    total_team_text.setText(dashboard.getTotalTeamSize());
                    in_progress_text.setText(dashboard.getTaskONPROCESS());
                    complete_task_text.setText(dashboard.getTaskCOMPLETED());
                    pending_task_text.setText(dashboard.getTaskPENDING());
                    globalProgressDialog.dismissProgressDialog();
                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DashboardModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail","fail");
                Log.e("response-failure", call.toString());
            }
        });


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
