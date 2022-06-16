package com.example.taskplannernew.Fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Adapter.AllTaskAdapter;
import com.example.taskplannernew.Adapter.RecentTaskAdapter;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.ActivityModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.Models.TaskModel;
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
import retrofit2.Response;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;


public class AllTaskActivityFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ActivityTaskModel> activityTaskModelArrayList;
    SharePref sharePref;
    AllTaskAdapter adapter;
    ActivityTaskModel activityTaskModel;
    String userid,token,taskid,taskstatus,usertype;
    Bundle arguments;
    GlobalProgressDialog globalProgressDialog;
    TextView nodata_found;
    FloatingActionButton add_activity_fab,fab_reopen_approve;
    Bundle bundle;
    String jsonObjectStr, response, message;

    FloatingActionButton fab, fab_addactivity, fab_approve, fab_reopen;
    LinearLayout fabLayout_addactivity, fabLayout_approve, fabLayout_reopen;
    View fabBGLayout , fabBGLayout_reopen_approve;
    boolean isFABOpen = false;


    public AllTaskActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = new SharePref(getActivity());
        globalProgressDialog = new GlobalProgressDialog();
        globalProgressDialog.ProgressDialogShow(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("ALL ACTIVITIES");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_all_task, container, false);
        recyclerView = view.findViewById(R.id.recent_task_rv);
        nodata_found = view.findViewById(R.id.nodata_found);
        add_activity_fab = view.findViewById(R.id.add_activity_fab);
        fabLayout_approve = view.findViewById(R.id.fabLayout_approve);

        fabLayout_addactivity = (LinearLayout) view.findViewById(R.id.fabLayout_addactivity);
        fabLayout_approve = (LinearLayout) view.findViewById(R.id.fabLayout_approve);
        fabLayout_reopen = (LinearLayout) view.findViewById(R.id.fabLayout_reopen);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab_reopen_approve = (FloatingActionButton) view.findViewById(R.id.fab_reopen_approve);
        fab_addactivity = (FloatingActionButton) view.findViewById(R.id.fab_addactivity);
        fab_approve = (FloatingActionButton) view.findViewById(R.id.fab_approve);
        fab_reopen = (FloatingActionButton) view.findViewById(R.id.fab_reopen);
        fabBGLayout = view.findViewById(R.id.fabBGLayout);
        fabBGLayout_reopen_approve = view.findViewById(R.id.fabBGLayout_reopen_approve);

        //FAB for AddActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });
        //View For AddActivity
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
        //FAB For Reopen and Approve
        fab_reopen_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu1();
                } else {
                    closeFABMenu1();
                }
            }
        });
        //View for Reopen and Approve
        fabBGLayout_reopen_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu1();
            }
        });


        fabLayout_reopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Do you really want to re-open the task?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //finish();
                                //Toast.makeText(view.getContext(), "Re-open.", Toast.LENGTH_LONG).show();
                                //Reopentask();
                                new RegisterAsyncTask5(getActivity()).execute();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Do you want to approve the task?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Toast.makeText(view.getContext(), "Approved.", Toast.LENGTH_LONG).show();
                                //finish();
                                new RegisterAsyncTask6(getActivity()).execute();
                                //globalProgressDialog.ProgressDialogShow(getActivity());
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                loadFragment(new AddActivityFragment());

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        usertype = sharePref.getUserType();
        arguments = getArguments();
        sharePref.setTaskId(arguments.getString("task_id"));
        taskid = arguments.getString("task_id");
        taskstatus = arguments.getString("task_status");
        Log.d("task_id123",""+sharePref.getTaskId());
        Log.d("task_status",""+taskstatus);
        Log.d("usertype",""+usertype);

        Log.d("taskidact1",""+taskid);
        add_activity_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddActivityFragment();
                bundle = new Bundle();
                bundle.putString("task_id",sharePref.getTaskId());
                fragment.setArguments(bundle);
                Log.d("taskidact",""+taskid);

                FragmentManager fm = (getActivity()).getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);
                fragmentTransaction.commit();

            }
        });

        if(taskstatus.equals("PENDING") || taskstatus.equals("ON-PROCESS"))
        {
            Log.d("task_status_pending",""+taskstatus);
            add_activity_fab.setVisibility(View.VISIBLE);
        }
        else if(taskstatus.equals("COMPLETED")){
            Log.d("task_status_completed",""+taskstatus);
            add_activity_fab.setVisibility(View.INVISIBLE);

        }

        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fab.setVisibility(View.VISIBLE);
            }
            else if (taskstatus.equals("COMPLETED")) {
                fab_reopen_approve.setVisibility(View.VISIBLE);
            }
        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("usertype31", "" + usertype);
            isFABOpen = true;
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fab.setVisibility(View.VISIBLE);
            }
            else if (taskstatus.equals("COMPLETED")) {
                fab.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(getActivity(), "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }

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
                globalProgressDialog.dismissProgressDialog();
                try {
                    globalProgressDialog.dismissProgressDialog();
                    ActivityModel actalltask = new ActivityModel();
                    actalltask = response.body();

                    if (actalltask.getMessage().equalsIgnoreCase("success")) {
                        activityTaskModelArrayList = new ArrayList<ActivityTaskModel>();
                        if(actalltask.getDataTaskActivity()==null){
                            nodata_found.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(),"There is no Data Task Activities",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            for (int i = 0; i < actalltask.getDataTaskActivity().size(); i++) {
                                activityTaskModel = new ActivityTaskModel();

                                activityTaskModel = actalltask.getDataTaskActivity().get(i);
                                activityTaskModelArrayList.add(activityTaskModel);

                            }
                            adapter = new AllTaskAdapter(activityTaskModelArrayList, getActivity());
                            recyclerView.setAdapter(adapter);
                            globalProgressDialog.dismissProgressDialog();
                        }

                    } else {
                        globalProgressDialog.dismissProgressDialog();
                        Toast.makeText(getActivity(), actalltask.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(),"There is no Data Task Activities",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @SuppressLint("RestrictedApi")
    private void showFABMenu() {
        isFABOpen = true;
        Log.d("usertype11", "" + usertype);
        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_reopen.setVisibility(View.VISIBLE);
                fabLayout_approve.setVisibility(View.VISIBLE);
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
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
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(getActivity(), "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
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


    @SuppressLint("RestrictedApi")
    private void showFABMenu1() {
        isFABOpen = true;
        Log.d("usertype11", "" + usertype);
        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            if (taskstatus.equals("ON-PROCESS") || taskstatus.equals("PENDING") || taskstatus.equals("RE-OPEN")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_reopen.setVisibility(View.VISIBLE);
                fabLayout_approve.setVisibility(View.VISIBLE);
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab_reopen_approve.animate().rotationBy(180);
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
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }
            else if (taskstatus.equals("COMPLETED")) {
                fabLayout_addactivity.setVisibility(View.VISIBLE);
                fabBGLayout_reopen_approve.setVisibility(View.VISIBLE);
                fab.animate().rotationBy(180);
                fabLayout_addactivity.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                //fabLayout_approve.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
                //fabLayout_reopen.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
            }

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(getActivity(), "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }
    }

    private void closeFABMenu1() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
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
    public void loadFragment(Fragment fragment) {

        // create a FragmentManager
        FragmentManager fm = (getActivity().getSupportFragmentManager());
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        // replace the FrameLayout with new Fragment
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);

        fragmentTransaction.commit(); // save the changes
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

    @Override
    public void onDetach() {
        super.onDetach();
        globalProgressDialog.dismissProgressDialog();
    }

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
                            Toast.makeText(getActivity(), "Task has been reopen.", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), "Task has been approved", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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

}

