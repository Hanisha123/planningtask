package com.example.taskplannernew.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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


public class Todo extends Fragment {

    private Calendar.OnFragmentInteractionListener mListener;
    private ArrayList<String> dataBrandArray = new ArrayList<String>();
    Spinner spinner_brand_todo;
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText subject_todo;
    private RadioGroup priority_todo;
    private EditText description_todo;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatterServer;


    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    ImageView choosefile;
    Intent intent;
    RequestBody requestFile;
    MultipartBody.Part body;
    File file;

    String depart_id;
    String brand_id;
    String subject;
    String dat_from;
    String dat_to;
    String priority;
    String description;
    String Empid;
    int priorityRadioBtnId;

    String jsonObjectStr, response, message;

    LinearLayout from_date_linearlayout, to_date_linearlayout, choosefile_linearlayout;

    RadioButton highRadioBtn, mediumRadioBtn, lowRadioBtn;

    Button submit_todo_button;

    SharePref sharePref;
    String userid,token;
    GlobalProgressDialog globalProgressDialog;

    Date date_from,date_to;
    TextView filenamepath;


    public Todo() {
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
        toolbarTextView.setText("TODO");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_todo, container, false);
        spinner_brand_todo = view.findViewById(R.id.spinner_brand_todo);
        subject_todo = view.findViewById(R.id.subject_todo);
        priority_todo = view.findViewById(R.id.priority_todo);
        description_todo = view.findViewById(R.id.description_todo);
        highRadioBtn = view.findViewById(R.id.highRadioBtn);
        mediumRadioBtn = view.findViewById(R.id.mediumRadioBtn);
        lowRadioBtn = view.findViewById(R.id.lowRadioBtn);
        submit_todo_button = view.findViewById(R.id.submit_todo_button);
        filenamepath = view.findViewById(R.id.filenamepath);

        filenamepath.setText("There is no file chosen.");
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::",""+userid);
        Log.d("token::::",""+token);

        from_date_linearlayout = view.findViewById(R.id.from_date_linearlayout);
        to_date_linearlayout = view.findViewById(R.id.to_date_linearlayout);
        choosefile_linearlayout = view.findViewById(R.id.choosefile_linearlayout);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateEtxt = view.findViewById(R.id.fromDateEtxt);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = view.findViewById(R.id.toDateEtxt);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

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

        submit_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               // saveDataToServer();
                //brand_id = dataBrandHashMap.get(dataBrandArray.get(spinner_brand_todo.getSelectedItemPosition())).getBrandId();
                brand_id = "";
                if (dataBrandArray.get(spinner_brand_todo.getSelectedItemPosition()).equalsIgnoreCase("Select Brand")) {
                    Toast.makeText(getActivity(), "Please select brand", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    brand_id = dataBrandHashMap.get(dataBrandArray.get(spinner_brand_todo.getSelectedItemPosition())).getBrandId();
                }
                subject = subject_todo.getText().toString();
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
               /* dat_from = fromDateEtxt.getText().toString();
                Date date_from = null;
                try {
                    date_from = dateFormatter.parse(dat_from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dat_from = dateFormatterServer.format(date_from);*/
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

               /*dat_to = toDateEtxt.getText().toString();
                Date date_to = null;
                try {
                    date_to = dateFormatter.parse(dat_to);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ;
                dat_to = dateFormatterServer.format(date_to);*/
                priorityRadioBtnId = priority_todo.getCheckedRadioButtonId();
                if (priorityRadioBtnId == highRadioBtn.getId()) {
                    priority = "HIGH";
                } else if (priorityRadioBtnId == mediumRadioBtn.getId()) {
                    priority = "Medium";
                } else {
                    priority = "Low";
                }


                description = description_todo.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getActivity(), "Description cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isStoragePermissionGranted()) {
                    if (isCameraPermissionGranted()) {
//                        saveDataToServer1();
                        globalProgressDialog.ProgressDialogShow(getActivity());
                        new RegisterAsyncTask1(getActivity()).execute();

                    }
                }
            }
        });

        fetchJSON();
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


    private void fetchJSON() {
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
                //Log.i("Responsestring1", response.body().getDataDept());
                Log.i("Responsestring", "onResponse: ConfigurationListener::" + call.request().url());
                try {
                    globalProgressDialog.dismissProgressDialog();
                    DepartmentModel deptModel = new DepartmentModel();
                    deptModel = response.body();

                    if (deptModel.getMessage().equalsIgnoreCase("success")) {
                        dataBrandArray.add("Select Brand");
                        for (int i = 0; i < deptModel.getDataBrand().size(); i++) {
                            DataBrand dataBrandObj = deptModel.getDataBrand().get(i);
                            dataBrandArray.add(dataBrandObj.getBrandName());
                            dataBrandHashMap.put(dataBrandObj.getBrandName().trim(), dataBrandObj);
                        }
                        ArrayAdapter<String> spinnerArrayAdapterBrand = new ArrayAdapter<String>(getActivity(), simple_spinner_item, dataBrandArray);
                        spinnerArrayAdapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spinner_brand_todo.setAdapter(spinnerArrayAdapterBrand);
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

    private void saveDataToServer() {
        String brand_id = dataBrandHashMap.get(dataBrandArray.get(spinner_brand_todo.getSelectedItemPosition())).getBrandId();
        ;
        String subject = subject_todo.getText().toString();
        String dat_from = fromDateEtxt.getText().toString();
        Date date_from = null;
        try {
            date_from = dateFormatter.parse(dat_from);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dat_from = dateFormatterServer.format(date_from);
        String dat_to = toDateEtxt.getText().toString();
        Date date_to = null;
        try {
            date_to = dateFormatter.parse(dat_to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ;
        dat_to = dateFormatterServer.format(date_to);
        int priorityRadioBtnId = priority_todo.getCheckedRadioButtonId();
        String priority;
        if (priorityRadioBtnId == highRadioBtn.getId()) {
            priority = "HIGH";
        } else if (priorityRadioBtnId == mediumRadioBtn.getId()) {
            priority = "Medium";
        } else {
            priority = "Low";
        }
        String description = description_todo.getText().toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "createTodo");
        jsonObject.addProperty("brand_id", brand_id);
        jsonObject.addProperty("subject", subject);
        jsonObject.addProperty("dat_from", dat_from);
        jsonObject.addProperty("dat_to", dat_to);
        jsonObject.addProperty("priority", priority);
        jsonObject.addProperty("description", description);

        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<TaskModel> call = jsonPostService.createTask1(jsonObject);

        call.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {

                Log.i("Responsestring", response.body().toString());
                Log.i("Responsestring1", response.body().getMessage());
                Log.i("Responsestring", "onResponse: ConfigurationListener::" + call.request().url());
                if (response.body().getMessage().equalsIgnoreCase("Success: TODO has been created.")) {
                    ClearAllFieldsTodo();
                    Toast.makeText(getActivity(), "Todo has been assigned", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Kindly fill all the fields properly.", Toast.LENGTH_LONG).show();
                    Log.d("Error Message", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                Log.i("onFailure", t.getLocalizedMessage());
                Log.i("onFailure", "onResponse: ConfigurationListener::" + call.request().url());
                t.printStackTrace();
            }

        });


    }

    public void ClearAllFieldsTodo() {
        subject_todo.setText("");
        fromDateEtxt.setText("");
        toDateEtxt.setText("");
        description_todo.setText("");
    }

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

    HashMap<String, DataBrand> dataBrandHashMap = new HashMap<String, DataBrand>();

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //Storage Permission Granted
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

    //Camera Permssion Granted
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

    //RegisterAsynTask
    class RegisterAsyncTask1 extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask1(Activity activity) {
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
           /* if(!file1.exists()){

                // Toast.makeText(,"Please select file",Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(getActivity(),"Please select file",Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("fileattach","fileattach");
            }else {*/
                multipartEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("file", new FileBody(file1));

                try {

                    multipartEntity.addPart("user_id", new StringBody(userid));
                    multipartEntity.addPart("action", new StringBody("createTodo"));
                    multipartEntity.addPart("token", new StringBody(token));
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

                    //Log.i("MultiPart", multipartEntity.toString());
                    if (response.contains("Success")) {

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                globalProgressDialog.dismissProgressDialog();
                                ClearAllFieldsTodo();
                                Toast.makeText(getActivity(), "Todo has been assigned .", Toast.LENGTH_SHORT).show();
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
            catch (Exception e){
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
