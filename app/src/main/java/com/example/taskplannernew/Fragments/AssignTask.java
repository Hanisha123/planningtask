package com.example.taskplannernew.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.DataBrand;
import com.example.taskplannernew.Models.DataDept;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.layout.simple_spinner_item;

import static android.app.Activity.RESULT_OK;
import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class AssignTask extends Fragment {

    private Calendar.OnFragmentInteractionListener mListener;
    private ArrayList<String> dataDeptArray = new ArrayList<String>();
    private ArrayList<String> dataEmployeeArray = new ArrayList<String>();
    private ArrayList<String> dataBrandArray = new ArrayList<String>();
    Spinner spinner_department;
    MultipleSelectionSpinner spinner_employee;
    Spinner spinner_brand;
    String jsonObjectStr, response, message;

    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText subject_assign;
    private RadioGroup priority_assign;
    private EditText description_assign;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatterServer;

    List<String> list = new ArrayList<String>();
    ImageView choosefile;
    Intent intent;
    private static final int INTENT_REQUEST_CODE = 100;

    Button submitButton;

    LinearLayout from_date_linearlayout, to_date_linearlayout, choosefile_linearlayout;

    RadioButton highRadioBtn, mediumRadioBtn, lowRadioBtn;

    RequestBody requestFile;
    RequestBody requestFile1;
    RequestBody descBody;
    MultipartBody.Part body;
    File file;
    private String mImageUrl = "";

    String depart_id;
    String brand_id;
    String sel_emp_ids;
    String subject;
    String dat_from;
    String dat_to;
    String priority;
    String description;
    String Empid;
    int priorityRadioBtnId;
    TextView filenamepath;

    SharePref sharePref;
    String userid,token;
    GlobalProgressDialog globalProgressDialog;
    Date date_from,date_to;
    public AssignTask() {
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
        toolbarTextView.setText("ASSIGN TASK");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_assign_task, container, false);
        spinner_department = view.findViewById(R.id.spinner_department);
        spinner_employee = view.findViewById(R.id.spinner_employee);
        spinner_brand = view.findViewById(R.id.spinner_brand);
        from_date_linearlayout = view.findViewById(R.id.from_date_linearlayout);
        to_date_linearlayout = view.findViewById(R.id.to_date_linearlayout);
        choosefile_linearlayout = view.findViewById(R.id.choosefile_linearlayout);
        subject_assign = view.findViewById(R.id.subject_assign);
        description_assign = view.findViewById(R.id.description_assign);
        priority_assign = view.findViewById(R.id.priority_assign);
        highRadioBtn = view.findViewById(R.id.highRadioBtn);
        mediumRadioBtn = view.findViewById(R.id.mediumRadioBtn);
        lowRadioBtn = view.findViewById(R.id.lowRadioBtn);
        filenamepath = view.findViewById(R.id.filenamepath);

        filenamepath.setText("There is no file chosen.");

        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::",""+userid);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt = view.findViewById(R.id.fromDateEtxt);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = view.findViewById(R.id.toDateEtxt);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        submitButton = view.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                depart_id = "";
                if (dataDeptArray.get(spinner_department.getSelectedItemPosition()).equalsIgnoreCase("Select Department")) {
                    Toast.makeText(getActivity(), "Please Select Department", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Please Select Employees", Toast.LENGTH_SHORT).show();
                    return;
                }

                brand_id = "";
                if (dataBrandArray.get(spinner_brand.getSelectedItemPosition()).equalsIgnoreCase("Select Brand")) {
                    Toast.makeText(getActivity(), "Please select brand", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    brand_id = dataBrandHashMap.get(dataBrandArray.get(spinner_brand.getSelectedItemPosition())).getBrandId();
                }



                   /* subject = "";
                    if (subject == ""){
                        Toast.makeText(getActivity(), "Subject cannot be Empty.", Toast.LENGTH_SHORT).show();
                    } else {
                        subject = subject_assign.getText().toString();
                        //return;
                    }*/
                subject = subject_assign.getText().toString();
                if (subject.isEmpty()){
                    Toast.makeText(getActivity(), "Subject cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                date_from = null;
                    dat_from = fromDateEtxt.getText().toString();
                    try {
                        date_from = dateFormatter.parse(dat_from);
                        dat_from = dateFormatterServer.format(date_from);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Please select from date", Toast.LENGTH_SHORT).show();
                        return;

                    }
                   Log.i("dat_from","dat_from:"+dat_from);

                    date_to = null;
                    dat_to = toDateEtxt.getText().toString();
                    try {
                        date_to = dateFormatter.parse(dat_to);
                        dat_to = dateFormatterServer.format(date_to);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Please select to date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(date_from.getTime() > date_to.getTime()){
                    Toast.makeText(getContext(),"From Date cannot be greater than To date",Toast.LENGTH_SHORT).show();
                    return;
                    }

                description = description_assign.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getActivity(), "Description cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                priorityRadioBtnId = priority_assign.getCheckedRadioButtonId();
                if (priorityRadioBtnId == highRadioBtn.getId()) {
                    priority = "HIGH";
                } else if (priorityRadioBtnId == mediumRadioBtn.getId()) {
                    priority = "Medium";
                } else {
                    priority = "Low";
                }
                if (isStoragePermissionGranted()) {
                    if (isCameraPermissionGranted()) {
                        globalProgressDialog.ProgressDialogShow(getActivity());
                        new RegisterAsyncTask(getActivity()).execute();
                    }
                }


            }
        });

        //fetchJSON();
        getDataFromSpinner();

        setDateTimeField();
        choosefile_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //opening file chooser

                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 100);


            }
        });

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromDatePickerDialog.show();

            }
        });

        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toDatePickerDialog.show();

            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            //calling the upload file method after choosing the file
            uploadFile(selectedImage, "My Image");
        }
    }

    //Upload the file
    private void uploadFile(Uri fileUri, String desc) {

        //creating a file

            file = new File(getRealPathFromURI(fileUri));


        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);

    }

    //set date and time field on click on calendar
    private void setDateTimeField() {
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));


    }

    HashMap<String, DataDept> dataDeptHashMap = new HashMap<String, DataDept>();
    HashMap<String, DataEmployee> dataEmployeeHashMap = new HashMap<String, DataEmployee>();
    HashMap<String, DataBrand> dataBrandHashMap = new HashMap<String, DataBrand>();

    // Get Data In All Spinnner such as Department , Employees and Brands
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
                        ArrayAdapter<String> spinnerArrayAdapterDataDept = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataDeptArray);
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
                        ArrayAdapter<String> spinnerArrayAdapterEmployee = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataEmployeeArray);
                        spinnerArrayAdapterEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_employee.setItems(dataEmployeeArray);


                        dataBrandArray.add("Select Brand");
                        for (int i = 0; i < deptModel.getDataBrand().size(); i++) {
                            DataBrand dataBrandObj = deptModel.getDataBrand().get(i);
                            dataBrandArray.add(dataBrandObj.getBrandName());
                            dataBrandHashMap.put(dataBrandObj.getBrandName().trim(), dataBrandObj);


                        }
                        ArrayAdapter<String> spinnerArrayAdapterBrand = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataBrandArray);
                        spinnerArrayAdapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_brand.setAdapter(spinnerArrayAdapterBrand);
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

    void setSpinnerDataByDeptId(DataDept dataDeptObj) {
        dataEmployeeArray.clear();
        // dataEmployeeArray.add("Select Employee");
        try {
            globalProgressDialog.dismissProgressDialog();
            for (int j = 0; j < dataDeptObj.getEmpData().size(); j++) {
                EmployeeData dataEmployeeObj = dataDeptObj.getEmpData().get(j);
                dataEmployeeArray.add(dataEmployeeObj.getEmpName());
            }
            ArrayAdapter<String> spinnerArrayAdapterEmployee = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataEmployeeArray);
            spinnerArrayAdapterEmployee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner_employee.setItems(dataEmployeeArray);
            globalProgressDialog.dismissProgressDialog();

        } catch (Exception e) {
            globalProgressDialog.dismissProgressDialog();
            Log.d("msg", e.getMessage());
        }

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        Log.d("result : ", result);
        if(result.isEmpty()){
            filenamepath.setText("There is no file chosen.");
        }
        else {
            filenamepath.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            filenamepath.setText(result);
        }
        cursor.close();
        return result;
    }

    public void ClearAllFields() {
        subject_assign.setText("");
        fromDateEtxt.setText("");
        toDateEtxt.setText("");
        description_assign.setText("");
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Grant", "Permission is granted");
                return true;
            } else {

                Log.v("Revoked", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("Granted", "Permission is granted");
            return true;
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Grant", "Permission is granted");
                return true;
            } else {

                Log.v("Revoked", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Granted", "Permission is granted");
            return true;
        }
    }


    class RegisterAsyncTask extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask(Activity activity) {
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


            File file1 = new File("" + file);
            /*if(!file1.exists()){

               // Toast.makeText(,"Please select file",Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(getActivity(),"Please select file",Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("fileattach","fileattach");
            }else{*/
                multipartEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("file", new FileBody(file1));

                try {


                    multipartEntity.addPart("user_id", new StringBody(userid));
                    multipartEntity.addPart("action", new StringBody("assignTask"));
                    multipartEntity.addPart("token", new StringBody(token));
                    multipartEntity.addPart("depart_id", new StringBody(depart_id));
                    multipartEntity.addPart("sel_emp_ids", new StringBody(sel_emp_ids));
                    multipartEntity.addPart("brand_id", new StringBody(brand_id));
                    multipartEntity.addPart("subject", new StringBody(subject));
                    multipartEntity.addPart("dat_from", new StringBody(dat_from));
                    multipartEntity.addPart("dat_to", new StringBody(dat_to));
                    multipartEntity.addPart("priority", new StringBody(priority));
                    multipartEntity.addPart("description", new StringBody(description));

                    post.setEntity(multipartEntity);
                    HttpResponse responserr = null;
                    responserr = client.execute(post);
                    HttpEntity resEntity = responserr.getEntity();
                    jsonObjectStr = EntityUtils.toString(resEntity);
                    jsonObject = new JSONObject(jsonObjectStr);
                    response = jsonObject.getString("message");

                    if (response.contains("Success")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                globalProgressDialog.dismissProgressDialog();
                                ClearAllFields();
                                Toast.makeText(getActivity(), "Task has been assigned .", Toast.LENGTH_SHORT).show();

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



