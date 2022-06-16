package com.example.taskplannernew.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.DataBrand;
import com.example.taskplannernew.Models.DataEmployee;
import com.example.taskplannernew.Models.DepartmentModel;
import com.example.taskplannernew.Models.EmployeeData;
import com.example.taskplannernew.Models.TaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.layout.simple_spinner_item;
import static com.example.taskplannernew.utils.ApiClient.BASE_URL;


public class Re_assignFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Spinner spinner_employees;
    SharePref sharePref;
    String userid,token, taskid;
    Button re_assign_button;
    private ArrayList<String> dataEmployeeArray = new ArrayList<String>();
    String emp_id , note_re_assign_text;
    EditText note_reassign;
    Bundle arguments;
    String jsonObjectStr, response, message;
    GlobalProgressDialog globalProgressDialog;


    public Re_assignFragment() {
        // Required empty public constructor
    }


    public static Re_assignFragment newInstance(String param1, String param2) {
        Re_assignFragment fragment = new Re_assignFragment();
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("RE-ASSIGN TASK");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_re_assign, container, false);
        spinner_employees = view.findViewById(R.id.spinner_employees);
        re_assign_button = view.findViewById(R.id.re_assign_button);
        note_reassign = view.findViewById(R.id.note_reassign);
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::",""+userid);
        Log.d("token::::",""+token);

        arguments = getArguments();
        taskid = arguments.getString("task_id");
        Log.d("taskid", "" + taskid);
        getDataOfEmployees();

        re_assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emp_id = "";
                if (dataEmployeeArray.get(spinner_employees.getSelectedItemPosition()).equalsIgnoreCase("Select Employee")) {
                    Toast.makeText(getActivity(), "Please Select Employee", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    emp_id = dataEmployeeHashMap.get(dataEmployeeArray.get(spinner_employees.getSelectedItemPosition())).getEmpId();
                }
                note_re_assign_text = note_reassign.getText().toString();
                if (note_re_assign_text.isEmpty()){
                    Toast.makeText(getActivity(), "Note cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                globalProgressDialog.ProgressDialogShow(getActivity());
                new RegisterAsyncTask4(getActivity()).execute();
            }
        });



        return view;
    }

    HashMap<String, DataEmployee> dataEmployeeHashMap = new HashMap<String, DataEmployee>();

    public void getDataOfEmployees() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("task_id", "60");
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "fetchAll");

        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<DepartmentModel> call = jsonPostService.getAllDepartment(jsonObject);

        call.enqueue(new Callback<DepartmentModel>() {
            @Override
            public void onResponse(Call<DepartmentModel> call, Response<DepartmentModel> response) {
                Log.i("Responsestring", response.body().toString());
                Log.i("Responsestring", "onResponse: ConfigurationListener::" + call.request().url());
                globalProgressDialog.dismissProgressDialog();
                try {
                    DepartmentModel deptModel = new DepartmentModel();
                    deptModel = response.body();

                    if (deptModel.getMessage().equalsIgnoreCase("success")) {
                        dataEmployeeArray.add("Select Employee");
                        for (int i = 0; i < deptModel.getDataEmployees().size(); i++) {
                            DataEmployee dataEmployeeObj = deptModel.getDataEmployees().get(i);
                            dataEmployeeArray.add(dataEmployeeObj.getEmpName());
                            dataEmployeeHashMap.put(dataEmployeeObj.getEmpName().trim(), dataEmployeeObj);


                        }
                        ArrayAdapter<String> spinnerArrayAdapterBrand = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataEmployeeArray);
                        spinnerArrayAdapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_employees.setAdapter(spinnerArrayAdapterBrand);
                        globalProgressDialog.dismissProgressDialog();
                    } else {
                        globalProgressDialog.dismissProgressDialog();
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DepartmentModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.i("onFailure", t.getLocalizedMessage());
                Log.i("onFailure", "onResponse: ConfigurationListener::" + call.request().url());
                t.printStackTrace();
            }

        });
    }

    class RegisterAsyncTask4 extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask4(Activity activity) {
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
            post = new HttpPost("http://abgo.in/api/task_create.php");

            multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);


            try {

                multipartEntity.addPart("user_id", new StringBody(userid));
                multipartEntity.addPart("action", new StringBody("re_Assign"));
                multipartEntity.addPart("token", new StringBody(token));
                multipartEntity.addPart("task_id", new StringBody(taskid));
                multipartEntity.addPart("sel_emp_ids", new StringBody(emp_id));
                multipartEntity.addPart("txt_note", new StringBody(note_re_assign_text));

                post.setEntity(multipartEntity);
                HttpResponse responserr = null;
                responserr = client.execute(post);
                HttpEntity resEntity = responserr.getEntity();
                jsonObjectStr = EntityUtils.toString(resEntity);
                jsonObject = new JSONObject(jsonObjectStr);
                response = jsonObject.getString("message");

                //Log.i("MultiPart", multipartEntity.toString());
                if (response.contains("Success")) {

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            globalProgressDialog.dismissProgressDialog();
                            note_reassign.setText("");
                            Toast.makeText(getActivity(), "Task Re-Assigned Successfully", Toast.LENGTH_SHORT).show();
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

            if (jsonObject.has("message")) {
                globalProgressDialog.dismissProgressDialog();
            } else {
                globalProgressDialog.dismissProgressDialog();
                Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
