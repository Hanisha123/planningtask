package com.example.taskplannernew.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.Activity.MemberTasksActivity;
import com.example.taskplannernew.Fragments.AllTaskActivityFragment;
import com.example.taskplannernew.Fragments.ChangeTATFragment;
import com.example.taskplannernew.Fragments.JuniorTasks;
import com.example.taskplannernew.Fragments.Re_assignFragment;
import com.example.taskplannernew.Models.AllJuniorsModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.SharePref;

import java.util.ArrayList;
import java.util.Random;

public class AllJuniorAdapter extends RecyclerView.Adapter<AllJuniorAdapter.MyVh> {


    ArrayList<AllJuniorsModel> allJuniorsModels;
    Context context;
    SharePref sharePref;
    AllJuniorsModel all_juniors_model;
    String usertype , assignedId ,empid;
    Bundle bundle;
    TextView emp_first_letter;
    LinearLayout circular_layout;

    ImageView letter;
    String letter1;
    ColorGenerator generator = ColorGenerator.MATERIAL;



    public AllJuniorAdapter(ArrayList<AllJuniorsModel> allJuniorsModels, Context context) {
        this.allJuniorsModels = allJuniorsModels;
        this.context = context;

    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_of_juniors, viewGroup, false);
        //emp_first_letter = (TextView) v.findViewById(R.id.emp_first_letter);
        circular_layout = (LinearLayout) v.findViewById(R.id.circular_layout);
        letter = (ImageView) v.findViewById(R.id.gmailitem_letter);

        sharePref = new SharePref(context);
        usertype = sharePref.getUserType();
        Log.d("usertypeadapter",""+usertype);
        assignedId = sharePref.getAssignedId();
        Log.d("assignedId",""+assignedId);

        return new MyVh(v);
    }
    //set date and time field on click on calendar

    @Override
    public void onBindViewHolder(@NonNull MyVh myVh, int i) {
        try {
            all_juniors_model = allJuniorsModels.get(i);

            myVh.emp_name.setText(all_juniors_model.getEmpName());
            myVh.emp_code.setText(all_juniors_model.getEmpCode());
            myVh.emp_designation.setText(all_juniors_model.getDesignation());
            Log.d("emp_name",""+all_juniors_model.getEmpName());
            empid = all_juniors_model.getEmpId();
            Log.d("empid",""+all_juniors_model.getEmpId());

            String s = all_juniors_model.getEmpName();
            char result = s.charAt(0);
            System.out.println(result);
            Log.d("resultletter",""+result);
            //myVh.emp_first_letter.setText(String.valueOf(result));
            //letter1 = String.valueOf(allJuniorsModels.get(i).charAt(0));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(result), generator.getRandomColor());
            myVh.letter.setImageDrawable(drawable);


           /* Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            circular_layout.setBackgroundColor(color);*/

            /*String s = all_juniors_model.getEmpName();
            char result = s.charAt(0);
            System.out.println(result);
            Log.d("resultletter",""+result);
            myVh.emp_first_letter.setText(String.valueOf(result));*/

            myVh.user_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    all_juniors_model = allJuniorsModels.get(i);
                    empid = all_juniors_model.getEmpId();

                    Intent intent= new Intent(context, MemberTasksActivity.class);
                    intent.putExtra("emp_id", all_juniors_model.getEmpId());
                    Log.d("empidalljunior",""+all_juniors_model.getEmpId());
                    context.startActivity(intent);
                    Log.d("passempid1",""+empid);
                    Log.d("passassignedId2",""+assignedId);
                    //if(assignedId.equals(empid))

                        /*Log.d("equals","equals");
                        Fragment fragment = new JuniorTasks();
                        bundle = new Bundle();
                        bundle.putString("emp_id",all_juniors_model.getEmpId());
                        fragment.setArguments(bundle);
                        Log.d("empidalljunior",""+all_juniors_model.getEmpId());
                        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        //fragmentTransaction.addToBackStack(null);
                        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                        fragmentTransaction.commit();*/


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return allJuniorsModels.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        TextView emp_name , emp_code , emp_designation,emp_first_letter;
        LinearLayout user_cardview;
        LinearLayout circular_layout;
        ImageView letter;


        public MyVh(@NonNull View itemView) {
            super(itemView);
            emp_name = itemView.findViewById(R.id.emp_name);
            emp_code = itemView.findViewById(R.id.emp_code);
            emp_designation = itemView.findViewById(R.id.emp_designation);
            user_cardview = itemView.findViewById(R.id.user_cardview);
            emp_first_letter = itemView.findViewById(R.id.emp_first_letter);
            circular_layout = itemView.findViewById(R.id.circular_layout);
            letter = itemView.findViewById(R.id.gmailitem_letter);
        }
    }


}
