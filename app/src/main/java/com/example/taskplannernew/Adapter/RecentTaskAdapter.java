package com.example.taskplannernew.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.taskplannernew.Activity.Re_Assign_Activity;
import com.example.taskplannernew.Activity.RecentTaskDetails;
import com.example.taskplannernew.Fragments.AddActivityFragment;
import com.example.taskplannernew.Fragments.AllTaskActivityFragment;
import com.example.taskplannernew.Fragments.ChangeTATFragment;
import com.example.taskplannernew.Fragments.Re_assignFragment;
import com.example.taskplannernew.Models.AllJuniorsModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.SharePref;

import java.util.ArrayList;

public class RecentTaskAdapter extends RecyclerView.Adapter<RecentTaskAdapter.MyVh> {

    ImageView details, activity, change_tat;

    ArrayList<RecentTaskModel> recentTaskModels;
    Context context;
    FrameLayout activity_changetat;
    SharePref sharePref;
    Bundle bundle,bundle1;
    RecentTaskModel recent_task_model;
    String usertype,task_status,task_id;
    LinearLayout task_status_color , recent_task_detail;


    public RecentTaskAdapter(ArrayList<RecentTaskModel> recentTaskModels, Context context) {
        this.recentTaskModels = recentTaskModels;
        this.context = context;

    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_of_tasks, viewGroup, false);

        details = (ImageView) v.findViewById(R.id.details);
        activity = (ImageView) v.findViewById(R.id.activity);
        change_tat = (ImageView) v.findViewById(R.id.change_tat);
        task_status_color = (LinearLayout) v.findViewById(R.id.linear_layout);
        recent_task_detail = (LinearLayout) v.findViewById(R.id.recent_task_detail);

        sharePref = new SharePref(context);
        usertype = sharePref.getUserType();
        Log.d("usertypeadapter",""+usertype);

        return new MyVh(v);
    }
    //set date and time field on click on calendar

    @Override
    public void onBindViewHolder(@NonNull MyVh myVh, int i) {
        try {
            recent_task_model = recentTaskModels.get(i);

            myVh.priority_txt.setText(recent_task_model.getPriority());
            myVh.fromdate_txt.setText("From Date - " +recent_task_model.getFromdate());
            myVh.todate_txt.setText("To Date - " +recent_task_model.getTodate());
            myVh.subject_txt.setText(recent_task_model.getSubject());
            myVh.description_txt.setText(recent_task_model.getTaskdescription());
            myVh.priority_txt.setText("Department - "+recent_task_model.getDepartmentName());
            myVh.assigntotask.setText("Assign To - "+recent_task_model.getAssignto());

            //sharePref.setAssignedId(recent_task_model.getAssigntoId());

            Log.i("RecentTaskAdapter", "onBindViewHolder i:" + i);

            Log.d("1priority_txt",""+recent_task_model.getPriority());
            Log.d("1fromdate_txt",""+recent_task_model.getFromdate());
            Log.d("1todate_txt",""+recent_task_model.getTodate());
            Log.d("1subject_txt",""+recent_task_model.getSubject());
            Log.d("1ASSIGNEDBY",""+recent_task_model.getAssignby());
            Log.d("1CREATEDID",""+recent_task_model.getCreatedbyId());
            Log.d("1ASSIGNEDBYID",""+recent_task_model.getAssignbyId());
            Log.d("1111111taskid",""+recent_task_model.getTaskId());
            Log.d("assignedId1",""+recent_task_model.getAssigntoId());


            if(recent_task_model.getAssignby().equals(recent_task_model.getAssignto()))
            {
                myVh.task_type.setText("TODO");
            }
            else {
                myVh.task_type.setText("ASSIGN");
            }

            task_status = recent_task_model.getTaskStatus();
            Log.d("task_status1",""+task_status);


            if(recent_task_model.getTaskStatus().equals("PENDING")){
                task_status_color.setBackgroundColor(Color.parseColor("#ef5350"));
            } else if(recent_task_model.getTaskStatus().equals("ON-PROCESS")){
                task_status_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            } else if(recent_task_model.getTaskStatus().equals("RE-OPEN")){
                task_status_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            } else if(recent_task_model.getTaskStatus().equals("COMPLETED")){
                task_status_color.setBackgroundColor(Color.parseColor("#26dad2"));
            }

            myVh.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recent_task_model = recentTaskModels.get(i);
                    task_id = recent_task_model.getTaskId();
                    Log.d("UNIQUE111111",""+task_id);
                   // loadFragment(new AllTaskActivityFragment());
                    Fragment fragment = new AllTaskActivityFragment();
                    bundle = new Bundle();
                    bundle.putString("task_status",recent_task_model.getTaskStatus());
                    bundle.putString("task_id",recent_task_model.getTaskId());
                    fragment.setArguments(bundle);

                    Log.d("task_idadapter",""+recent_task_model.getTaskId());
                    Log.d("task_status", "" + recent_task_model.getTaskStatus());
                    // create a FragmentManager
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    // replace the FrameLayout with new Fragment
                    FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                    fragmentTransaction.commit();
                }
            });


            myVh.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recent_task_model = recentTaskModels.get(i);
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_box_card);
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    //dialog.setTitle("Task Details");
                    AppCompatTextView subject_detail = (AppCompatTextView) dialog.findViewById(R.id.subject_detail);
                    AppCompatTextView fromdate_details = (AppCompatTextView) dialog.findViewById(R.id.fromdate_details);
                    AppCompatTextView todate_details = (AppCompatTextView) dialog.findViewById(R.id.todate_details);
                    AppCompatTextView assignedby_details = (AppCompatTextView) dialog.findViewById(R.id.assignedby_details);
                    AppCompatTextView assignto_details = (AppCompatTextView) dialog.findViewById(R.id.assignto_details);
                    AppCompatTextView brand_details = (AppCompatTextView) dialog.findViewById(R.id.brand_details);
                    AppCompatTextView filename_detail = (AppCompatTextView) dialog.findViewById(R.id.filename_detail);
                    AppCompatTextView description_detail = (AppCompatTextView) dialog.findViewById(R.id.description_detail);
                    AppCompatTextView task_status = (AppCompatTextView) dialog.findViewById(R.id.task_status);
                    subject_detail.setText(recent_task_model.getSubject());
                    fromdate_details.setText(recent_task_model.getFromdate());
                    todate_details.setText(recent_task_model.getTodate());
                    assignedby_details.setText(recent_task_model.getAssignby());
                    assignto_details.setText(recent_task_model.getAssignto());
                    brand_details.setText(recent_task_model.getBrandName());
                    filename_detail.setText(recent_task_model.getFilename());
                    description_detail.setText(recent_task_model.getTaskdescription());
                    task_status.setText(recent_task_model.getTaskStatus());

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


                    Log.d("filename",recent_task_model.getFilename());
                    Log.d("task_status",recent_task_model.getFilename());
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



            myVh.change_tat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loadFragment(new ChangeTATFragment());
                }
            });

            myVh.re_assign_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    recent_task_model = recentTaskModels.get(i);
                    //loadFragment(new Re_assignFragment());
                    Intent intent= new Intent(context, Re_Assign_Activity.class);
                    intent.putExtra("task_id", recent_task_model.getTaskId());
                    intent.putExtra("subject", recent_task_model.getSubject());
                    intent.putExtra("fromdate", recent_task_model.getFromdate());
                    intent.putExtra("todate", recent_task_model.getTodate());
                    intent.putExtra("task_status", recent_task_model.getTaskStatus());
                    context.startActivity(intent);
                }
            });

            myVh.recent_task_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recent_task_model = recentTaskModels.get(i);
                    Intent intent= new Intent(context, RecentTaskDetails.class);
                    intent.putExtra("task_id", recent_task_model.getTaskId());
                    intent.putExtra("subject", recent_task_model.getSubject());
                    intent.putExtra("fromdate", recent_task_model.getFromdate());
                    intent.putExtra("todate", recent_task_model.getTodate());
                    intent.putExtra("task_status", recent_task_model.getTaskStatus());
                    intent.putExtra("assignby", recent_task_model.getAssignby());
                    intent.putExtra("assignto", recent_task_model.getAssignto());
                    intent.putExtra("filename", recent_task_model.getFilename());
                    intent.putExtra("brand_name", recent_task_model.getBrandName());
                    intent.putExtra("taskdescription", recent_task_model.getTaskdescription());
                    context.startActivity(intent);
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
        bundle.putString("task_id",recent_task_model.getTaskId());
        fragment.setArguments(bundle);
        Log.d("task_idadapter",""+recent_task_model.getTaskId());

        Log.d("subject", "" + recent_task_model.getSubject());

        // create a FragmentManager
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
       // fragmentTransaction.disallowAddToBackStack();
        // replace the FrameLayout with new Fragment
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);

        fragmentTransaction.commit(); // save the changes
    }


    @Override
    public int getItemCount() {
        return recentTaskModels.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        TextView priority_txt, fromdate_txt, todate_txt, subject_txt, description_txt,task_type,description_detail,task_status,assigntotask;
        ImageView details, activity, change_tat,re_assign_task;
        LinearLayout task_status_color , recent_task_detail;
        //For Activity
        TextView activity_at, activity_note, employee_name, assign_to;



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
            re_assign_task = itemView.findViewById(R.id.re_assign_task);
            task_status_color = itemView.findViewById(R.id.linear_layout);
            task_type = itemView.findViewById(R.id.task_type);
            description_detail = itemView.findViewById(R.id.description_detail);
            task_status = itemView.findViewById(R.id.task_status);
            recent_task_detail = itemView.findViewById(R.id.recent_task_detail);
            assigntotask = itemView.findViewById(R.id.assigntotask);

            if(usertype.contains("MANAGER"))
            {
                Log.d("re_assign_task",""+usertype);
                re_assign_task.setVisibility(View.VISIBLE);
            }
            else
            {
                re_assign_task.setVisibility(View.INVISIBLE);
            }
            //Change color according to status





        }
    }

}
