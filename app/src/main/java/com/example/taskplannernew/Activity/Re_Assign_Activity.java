package com.example.taskplannernew.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.taskplannernew.Fragments.MultipleSelectionSpinner;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.DataDept;
import com.example.taskplannernew.Models.DataEmployee;
import com.example.taskplannernew.Models.DepartmentModel;
import com.example.taskplannernew.Models.EmployeeData;
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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.layout.simple_spinner_item;
import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class Re_Assign_Activity extends AppCompatActivity {

    Spinner spinner_employees;
    SharePref sharePref;
    String userid,token, taskid;
    Button re_assign_button;
    private ArrayList<String> dataDeptArray = new ArrayList<String>();
    private ArrayList<String> dataEmployeeArray = new ArrayList<String>();
    Spinner spinner_department;
    MultipleSelectionSpinner spinner_employee;
    String emp_id , note_re_assign_text;
    EditText note_reassign;
    Bundle arguments;
    String jsonObjectStr, response, message;
    GlobalProgressDialog globalProgressDialog;
    Bundle bundle;
    String depart_id,sel_emp_ids;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re__assign_);
        TextView toolbarTextView = (TextView)findViewById(R.id.heading);
        toolbarTextView.setText("RE ASSIGN TASK");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sharePref = new SharePref(this);
        globalProgressDialog = new GlobalProgressDialog();

        spinner_department = (Spinner)findViewById(R.id.spinner_department);
        spinner_employee = (MultipleSelectionSpinner) findViewById(R.id.spinner_employee);
        re_assign_button = (Button) findViewById(R.id.re_assign_button);
        note_reassign = (EditText)findViewById(R.id.note_reassign);
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::",""+userid);
        Log.d("token::::",""+token);
        bundle = getIntent().getExtras();
        taskid = getIntent().getExtras().getString("task_id");
        Log.d("taskid", "" + taskid);
        getDataFromSpinner();

        re_assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depart_id = "";
                if (dataDeptArray.get(spinner_department.getSelectedItemPosition()).equalsIgnoreCase("Select Department")) {
                    Toast.makeText(Re_Assign_Activity.this, "Please Select Department", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    depart_id = dataDeptHashMap.get(dataDeptArray.get(spinner_department.getSelectedItemPosition())).getDepartId();
                }
                sel_emp_ids = "";
                if (spinner_employee.getSelectedIndicies().size() > 0) {
                    for (Integer position : spinner_employee.getSelectedIndicies()) {
                        String Empid = dataEmployeeHashMap.get(dataEmployeeArray.get(position)).getEmpId();
                        if (sel_emp_ids == "") {
                            sel_emp_ids = Empid;
                        } else {
                            sel_emp_ids = sel_emp_ids + "," + Empid;
                        }
                    }
                } else {
                    Toast.makeText(Re_Assign_Activity.this, "Please Select Employees", Toast.LENGTH_SHORT).show();
                    return;
                }

                note_re_assign_text = note_reassign.getText().toString();
                if (note_re_assign_text.isEmpty()){
                    Toast.makeText(Re_Assign_Activity.this, "Note cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //globalProgressDialog.ProgressDialogShow(Re_Assign_Activity.this);
                Re_Assign_Activity re_assign_activity = new Re_Assign_Activity();
                new RegisterAsyncTask4(re_assign_activity).execute();
                //new Re_assignFragment.RegisterAsyncTask4(getActivity()).execute();
            }
        });


    }
    HashMap<String, DataDept> dataDeptHashMap = new HashMap<String, DataDept>();
    HashMap<String, DataEmployee> dataEmployeeHashMap = new HashMap<String, DataEmployee>();

    private void getDataFromSpinner() {

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
                Log.i("Responsestring", "onResponse: ConfigurationListener::" + call.request().url());
                try {
                    globalProgressDialog.dismissProgressDialog();
                    DepartmentModel deptModel = new DepartmentModel();
                    deptModel = response.body();

                    if (deptModel.getMessage().equalsIgnoreCase("success")) {
                        dataDeptArray.add("Select Department");
                        for (int i = 0; i < deptModel.getDataDept().size(); i++) {
                            DataDept dataDeptObj = deptModel.getDataDept().get(i);
                            dataDeptArray.add(dataDeptObj.getDepartmentName());
                            dataDeptHashMap.put(dataDeptObj.getDepartmentName().trim(), dataDeptObj);
                        }
                        ArrayAdapter<String> spinnerArrayAdapterDataDept = new ArrayAdapter<String>(Re_Assign_Activity.this, simple_spinner_item, dataDeptArray);
                        spinnerArrayAdapterDataDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_department.setAdapter(spinnerArrayAdapterDataDept);
                        globalProgressDialog.dismissProgressDialog();

                        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // your code here
                                DataDept dataDept = dataDeptHashMap.get(dataDeptArray.get(position));
                                Log.i("position:" + position, "dept:" + dataDeptArray.get(position) + "--"
                                );
                                if (dataDept != null) {
                                    Log.i("position:" + position, "dept:" + dataDeptArray.get(position) + "--"
                                            + dataDept.getDepartId());

                                    setSpinnerDataByDeptId(dataDept);
                                    globalProgressDialog.dismissProgressDialog();
                                    //setSpinnerDataByDeptId1(dataDept);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                                globalProgressDialog.dismissProgressDialog();
                            }

                        });

                        //dataEmployeeArray.add("Select Employee");
                        for (int i = 0; i < deptModel.getDataEmployees().size(); i++) {
                            DataEmployee dataEmployeeObj = deptModel.getDataEmployees().get(i);
                            dataEmployeeArray.add(dataEmployeeObj.getEmpName());
                            dataEmployeeHashMap.put(dataEmployeeObj.getEmpName().trim(), dataEmployeeObj);

                        }
                        ArrayAdapter<String> spinnerArrayAdapterEmployee = new ArrayAdapter<String>(Re_Assign_Activity.this, simple_spinner_item, dataEmployeeArray);
                        spinnerArrayAdapterEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_employee.setItems(dataEmployeeArray);
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

    void setSpinnerDataByDeptId(DataDept dataDeptObj) {
        dataEmployeeArray.clear();
        // dataEmployeeArray.add("Select Employee");
        try {
            globalProgressDialog.dismissProgressDialog();
            for (int j = 0; j < dataDeptObj.getEmpData().size(); j++) {
                EmployeeData dataEmployeeObj = dataDeptObj.getEmpData().get(j);
                dataEmployeeArray.add(dataEmployeeObj.getEmpName());
            }
            ArrayAdapter<String> spinnerArrayAdapterEmployee = new ArrayAdapter<String>(Re_Assign_Activity.this, simple_spinner_item, dataEmployeeArray);
            spinnerArrayAdapterEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner_employee.setItems(dataEmployeeArray);
            globalProgressDialog.dismissProgressDialog();

        } catch (Exception e) {
            globalProgressDialog.dismissProgressDialog();
            Log.d("msg", e.getMessage());
        }

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
                multipartEntity.addPart("sel_emp_ids", new StringBody(sel_emp_ids));
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
                           // globalProgressDialog.dismissProgressDialog();
                            note_reassign.setText("");
                            Toast.makeText(Re_Assign_Activity.this, "Task Re-Assigned Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    globalProgressDialog.dismissProgressDialog();
                    Toast.makeText(Re_Assign_Activity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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
              //  globalProgressDialog.dismissProgressDialog();
            } else {
               // globalProgressDialog.dismissProgressDialog();
                Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
            }
        }
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
