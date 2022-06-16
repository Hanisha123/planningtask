package com.example.taskplannernew.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplannernew.Activity.AppLogin;
import com.example.taskplannernew.Activity.LoginModel;
import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Fragments.AddActivityFragment;
import com.example.taskplannernew.Fragments.AllTaskActivityFragment;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.ActivityModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class AllTaskAdapter extends RecyclerView.Adapter<AllTaskAdapter.MyVh> {

    ArrayList<ActivityTaskModel> activityTaskModels;
    Context context;
    Bundle bundle;
    ActivityTaskModel activity_task_model;
    String task_status,usertype,task_id;
    SharePref sharePref;
    Button btn_reopen,btn_approve;
    LinearLayout btn_add_activity , side_line_color ,attachment_layout;
    TextView status;
    String url;





    public AllTaskAdapter(ArrayList<ActivityTaskModel> activityTaskModels, Context context) {
        this.activityTaskModels = activityTaskModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_of_all_activity_task, viewGroup, false);
        btn_reopen = (Button) v.findViewById(R.id.btn_reopen);
        btn_approve = (Button) v.findViewById(R.id.btn_approve);
        side_line_color = (LinearLayout) v.findViewById(R.id.side_line_color);
        attachment_layout = (LinearLayout) v.findViewById(R.id.attachment_layout);
        status = (TextView) v.findViewById(R.id.status);

        sharePref = new SharePref(context);
        usertype = sharePref.getUserType();
        Log.d("usertypeadapter",""+usertype);
        //task_status = activity_task_model.getTaskStatus();
        //Log.d("task_status::::",""+task_status);
        return new MyVh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh myVh, int i) {
        try {
            activity_task_model=activityTaskModels.get(i);

            myVh.employee_name.setText(activity_task_model.getEmpName());
            myVh.status.setText(activity_task_model.getTaskStatus());
            myVh.activity_note.setText(activity_task_model.getActivityNote());
            myVh.activity_at.setText(activity_task_model.getCreatedOn());
            myVh.attachment.setText(activity_task_model.getAttachment());
            Log.i("RecentTaskAdapter","onBindViewHolder i:"+i);
            task_status = activity_task_model.getTaskStatus();
            Log.d("task_status1111",""+task_status);
            Log.d("attachment",""+activity_task_model.getAttachment());

           myVh.attachment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("url ","url" );
                    activity_task_model=activityTaskModels.get(i);
                    url = activity_task_model.getAttachment() ;
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                    catch (Exception e){
                        Toast.makeText(context,"No File attached",Toast.LENGTH_SHORT).show();
                    }
                    Log.d("url11 ","" +activity_task_model.getAttachment());
                    Log.d("url ","" +url);

                    /*activity_task_model=activityTaskModels.get(i);
                    myVh.attachment.setText(activity_task_model.getAttachment());
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(activity_task_model.getAttachment()));
                    //startActivity(i);*/
                }
            });

            if(activity_task_model.getTaskStatus().equals("PENDING")){
                side_line_color.setBackgroundColor(Color.parseColor("#ef5350"));
            } else if(activity_task_model.getTaskStatus().equals("ON-PROCESS")){
                side_line_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            } else if(activity_task_model.getTaskStatus().equals("RE-OPEN")){
                side_line_color.setBackgroundColor(Color.parseColor("#ffb22b"));
            }
            else if(activity_task_model.getTaskStatus().equals("COMPLETED")){
                side_line_color.setBackgroundColor(Color.parseColor("#26dad2"));
            }


            if(activity_task_model.getTaskStatus().equals("PENDING")){

                status.setTextColor(Color.parseColor("#ef5350"));
            }
            else if(activity_task_model.getTaskStatus().equals("ON-PROCESS")){
                status.setTextColor(Color.parseColor("#ffb22b"));

            }
            else if(activity_task_model.getTaskStatus().equals("RE-OPEN")){
                status.setTextColor(Color.parseColor("#ffb22b"));

            }
            else if(activity_task_model.getTaskStatus().equals("COMPLETED")){
                status.setTextColor(Color.parseColor("#26dad2"));

            }

            myVh.attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_task_model=activityTaskModels.get(i);

                }
            });

           /* btn_add_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   activity_task_model=activityTaskModels.get(i);
                    task_id = activity_task_model.getTaskId();
                    Log.d("task_id_addactivity",""+task_id);

                    Fragment fragment = new AddActivityFragment();
                    bundle = new Bundle();
                    bundle.putString("task_status",activity_task_model.getTaskStatus());
                    bundle.putString("task_id",activity_task_model.getTaskId());
                    fragment.setArguments(bundle);

                    Log.d("task_idadapter",""+activity_task_model.getTaskId());
                    Log.d("task_status", "" + activity_task_model.getTaskStatus());
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                    fragmentTransaction.commit();
                }
            });*/



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadFragment(Fragment fragment) {

        bundle = new Bundle();
        bundle.putString("task_id", activity_task_model.getTaskId() );
        fragment.setArguments(bundle);

        // create a FragmentManager
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);

        fragmentTransaction.commit(); // save the changes
    }


    @Override
    public int getItemCount() {
        return activityTaskModels.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        TextView employee_name, status, activity_note, activity_at, attachment;
        Button btn_reopen,btn_approve,btn_add_activity;
        LinearLayout add_activity_fab , side_line_color,attachment_layout;

        public MyVh(@NonNull View itemView) {
            super(itemView);
            employee_name = itemView.findViewById(R.id.employee_name);
            status = itemView.findViewById(R.id.status);
            activity_note = itemView.findViewById(R.id.activity_note);
            activity_at = itemView.findViewById(R.id.activity_at);
            attachment = itemView.findViewById(R.id.attachment);
            btn_reopen = itemView.findViewById(R.id.btn_reopen);
            btn_approve = itemView.findViewById(R.id.btn_approve);
            side_line_color = itemView.findViewById(R.id.side_line_color);
            attachment_layout = itemView.findViewById(R.id.attachment_layout);
        }
    }

}
