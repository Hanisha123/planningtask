package com.example.taskplannernew.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Fragments.AllTaskActivityFragment;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class CalendarRecentTaskAdapter extends RecyclerView.Adapter<CalendarRecentTaskAdapter.MyVh> {

    ImageView details, activity, change_tat;

    ArrayList<RecentTaskModel> recentTaskModels;
    ArrayList<ActivityTaskModel> dataTaskActivityModels = null;
    Context context;
    ActivityTaskModel dataTaskActivity;
    FrameLayout activity_changetat;
    SharePref sharePref;
    Bundle bundle;
    RecentTaskModel recent_task_model;
    LinearLayout task_status_color;

    public CalendarRecentTaskAdapter(ArrayList<RecentTaskModel> recentTaskModels, Context context) {
        this.recentTaskModels = recentTaskModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_of_calendar_selected_date, viewGroup, false);
        details = (ImageView) v.findViewById(R.id.details);
        activity = (ImageView) v.findViewById(R.id.activity);
        change_tat = (ImageView) v.findViewById(R.id.change_tat);
        activity_changetat = (FrameLayout) v.findViewById(R.id.activity_changetat);
        task_status_color = (LinearLayout) v.findViewById(R.id.task_status_color);


        return new MyVh(v);
    }
    //set date and time field on click on calendar

    @Override
    public void onBindViewHolder(@NonNull MyVh myVh, int i) {
        try {
            recent_task_model = recentTaskModels.get(i);

            myVh.priority_txt.setText(recent_task_model.getPriority());
            myVh.fromdate_txt.setText("From Date - " +recent_task_model.getFromdate());
            myVh.todate_txt.setText("From Date - " +recent_task_model.getTodate());
            myVh.subject_txt.setText(recent_task_model.getSubject());
            myVh.description_txt.setText(recent_task_model.getTaskdescription());
            myVh.priority_txt.setText("Department - "+recent_task_model.getDepartmentName());
            myVh.assigntocal.setText("Assign To - " +recent_task_model.getAssignto());
            Log.i("RecentTaskAdapter", "onBindViewHolder i:" + i);

            if(recent_task_model.getTaskStatus().equals("PENDING")){
                task_status_color.setBackgroundColor(Color.parseColor("#ef5350"));
            } else if(recent_task_model.getTaskStatus().equals("ON-PROCESS")){
                task_status_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            } else if(recent_task_model.getTaskStatus().equals("RE-OPEN")){
                task_status_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            }
            else if(recent_task_model.getTaskStatus().equals("COMPLETED")){
                task_status_color.setBackgroundColor(Color.parseColor("#26dad2"));
            }

            myVh.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_box_card);
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //dialog.setTitle("Task Details");
                    AppCompatTextView subject_detail = (AppCompatTextView) dialog.findViewById(R.id.subject_detail);
                    AppCompatTextView fromdate_details = (AppCompatTextView) dialog.findViewById(R.id.fromdate_details);
                    AppCompatTextView todate_details = (AppCompatTextView) dialog.findViewById(R.id.todate_details);
                    AppCompatTextView assignedby_details = (AppCompatTextView) dialog.findViewById(R.id.assignedby_details);
                    AppCompatTextView assignto_details = (AppCompatTextView) dialog.findViewById(R.id.assignto_details);
                    AppCompatTextView brand_details = (AppCompatTextView) dialog.findViewById(R.id.brand_details);
                    AppCompatTextView filename_detail = (AppCompatTextView) dialog.findViewById(R.id.filename_detail);
                    AppCompatTextView task_status = (AppCompatTextView) dialog.findViewById(R.id.task_status);
                    AppCompatTextView description_detail = (AppCompatTextView) dialog.findViewById(R.id.description_detail);
                    Log.i("RecentTaskAdapter", "onBindViewHolder details:" + i);
                    Log.i("RecentTaskAdapter", "onBindViewHolder i:" + i);
                    RecentTaskModel recentTaskModel = recentTaskModels.get(i);
                    Log.i("RecentTaskAdapter", "recentTaskModel i:" + recentTaskModel.getSubject());

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("user_id", "34");
                    jsonObject.addProperty("token", "$2y$10$rN7OjVVUQzecNsINq9h.d.6ZxurhaM.J.Q9YPdn754kYr906zS1xi");
                    jsonObject.addProperty("action", "AllTaskList");
                    jsonObject.addProperty("task_id", "92");

                    IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
                    Call<RecentTaskModel> call = jsonPostService.getTaskDetail(jsonObject);
                    call.enqueue(new Callback<RecentTaskModel>() {
                        @Override
                        public void onResponse(Call<RecentTaskModel> call, Response<RecentTaskModel> response) {
                            Log.i("Responsestring", "" + response);
                            Log.i("Responsestring", "" + response);
                            RecentTaskModel recentTaskModel = response.body();
                        }

                        @Override
                        public void onFailure(Call<RecentTaskModel> call, Throwable t) {
                            Log.i("onFailure", t.getLocalizedMessage());
                            Log.i("onFailure", "onResponse: ConfigurationListener::" + call.request().url());
                            t.printStackTrace();
                        }
                    });

                    subject_detail.setText(recent_task_model.getSubject());
                    fromdate_details.setText(recent_task_model.getFromdate());
                    todate_details.setText(recent_task_model.getTodate());
                    assignedby_details.setText(recent_task_model.getAssignby());
                    assignto_details.setText(recent_task_model.getAssignto());
                    brand_details.setText(recent_task_model.getBrandName());
                    filename_detail.setText(recent_task_model.getFilename());
                    task_status.setText(recent_task_model.getTaskStatus());
                    description_detail.setText(recent_task_model.getTaskdescription());



                    if(recent_task_model.getTaskStatus().equals("PENDING")){
                        task_status.setTextColor(Color.parseColor("#ef5350"));
                    } else if(recent_task_model.getTaskStatus().equals("ON-PROCESS")){
                        task_status.setTextColor(Color.parseColor("#ffb22b"));
                    } else if(recent_task_model.getTaskStatus().equals("RE-OPEN")){
                        task_status.setTextColor(Color.parseColor("#ffb22b"));
                    }
                    else if(recent_task_model.getTaskStatus().equals("COMPLETED")){
                        task_status.setTextColor(Color.parseColor("#26dad2"));
                    }
                    AppCompatButton dialogButton = (AppCompatButton) dialog.findViewById(R.id.btn_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
            myVh.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //loadFragment(new ChangeTATFragment());

                }
            });


            myVh.change_tat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("fragment", "fragment");
                    Log.i("RecentTaskAdapter", "onBindViewHolder details:" + i);
                    Log.i("RecentTaskAdapter", "onBindViewHolder i:" + i);
                    RecentTaskModel recentTaskModel = recentTaskModels.get(i);
                    Log.i("RecentTaskAdapter", "recentTaskModel i:" + recentTaskModel.getSubject());
                    Log.d("fragment111", "fragment111");
                    loadFragment(new AllTaskActivityFragment());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadFragment(Fragment fragment) {


        bundle = new Bundle();
        bundle.putString("subject", recent_task_model.getSubject() );
        bundle.putString("fromdate", recent_task_model.getFromdate());
        bundle.putString("todate", recent_task_model.getTodate());
        fragment.setArguments(bundle);

        Log.d("subject", "" + recent_task_model.getSubject());


        // create a FragmentManager
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();


        // replace the FrameLayout with new Fragment
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);

        fragmentTransaction.commit(); // save the changes
    }


    @Override
    public int getItemCount() {
        return recentTaskModels.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        TextView priority_txt, fromdate_txt, todate_txt, subject_txt, description_txt,task_status,assigntocal;
        ImageView details, activity, change_tat;
        LinearLayout task_status_color;

        public MyVh(@NonNull View itemView) {
            super(itemView);
            priority_txt = itemView.findViewById(R.id.priority);
            fromdate_txt = itemView.findViewById(R.id.fromdate);
            todate_txt = itemView.findViewById(R.id.todate);
            subject_txt = itemView.findViewById(R.id.subject);
            description_txt = itemView.findViewById(R.id.description);
            details = itemView.findViewById(R.id.details);
            activity = itemView.findViewById(R.id.activity);
            change_tat = itemView.findViewById(R.id.change_tat);
            task_status = itemView.findViewById(R.id.task_status);
            task_status_color = itemView.findViewById(R.id.task_status_color);
            assigntocal = itemView.findViewById(R.id.assigntocal);



        }
    }

}
