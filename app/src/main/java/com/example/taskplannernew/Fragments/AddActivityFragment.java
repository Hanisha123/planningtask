package com.example.taskplannernew.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.taskplannernew.Activity.MainActivity;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;

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
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class AddActivityFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    //UI References
    private EditText add_activity_note;
    SharePref sharePref;
    String userid,token,taskid;
    String activity_note;
    Button btn_addactivity_submit;
    String jsonObjectStr, response, message;
    Bundle arguments;
    LinearLayout choosefile_linearlayout;
    MultipartBody.Part body;
    File file;
    RequestBody requestFile;
    Spinner spinner_status;
    String selected_status,progress;
    GlobalProgressDialog globalProgressDialog;
    TextView progressing_text;
    SeekBar seekBar;
    int progressChangedValue ;
    int progressValue = 0;
    CrystalSeekbar rangeSeekbar;
    TextView filenamepath;


    public AddActivityFragment() {
        // Required empty public constructor
    }


    public static AddActivityFragment newInstance(String param1, String param2) {
        AddActivityFragment fragment = new AddActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = new SharePref(getActivity());
        globalProgressDialog = new GlobalProgressDialog();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("ADD ACTIVITY");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_activity, container, false);
        add_activity_note = view.findViewById(R.id.add_activity_note);
        btn_addactivity_submit = view.findViewById(R.id.btn_addactivity_submit);
        choosefile_linearlayout = view.findViewById(R.id.choosefile_linearlayout);
        spinner_status = view.findViewById(R.id.spinner_status);
        progressing_text = view.findViewById(R.id.progressing_text);
        filenamepath = view.findViewById(R.id.filenamepath);
        filenamepath.setText("There is no file chosen.");
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        // perform seek bar change listener event used for getting the progress value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                progressValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                progressing_text.setText("Your current progress is "+progressChangedValue);
                Toast.makeText(getActivity(), "Your progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        List<String> list = new ArrayList<String>();
        list.add("Select Status");
        list.add("PENDING");
        list.add("ON-PROCESS");
        list.add("COMPLETED");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(dataAdapter);

        String status_addact = String.valueOf(spinner_status.getSelectedItem());
        Log.d("value of status_addact",""+status_addact);

        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        taskid = sharePref.getTaskId();
        progress = Integer.toString(progressChangedValue);

        Log.d("userid::::",""+userid);
        Log.d("taskid::::1",""+taskid);
        Log.d("progress::::",""+progress);

        Log.d("getselecteditem2",""+ spinner_status.getSelectedItem().toString());


        btn_addactivity_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("getselecteditem","progressValue"+progressValue);
                int number = progressValue;
                if(number <= 0){
                    Toast.makeText(getActivity(), "Please Select Progress", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Log.d("getselecteditem","progressValue"+progressValue);
                }
                activity_note = add_activity_note.getText().toString();
                if (activity_note.isEmpty()){
                    Toast.makeText(getActivity(), "Note cannot be Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                selected_status = spinner_status.getSelectedItem().toString();
                if (String.valueOf(spinner_status.getSelectedItem()).equalsIgnoreCase("Select Status")) {
                    Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();
                    return;
                }




                //progress = String.valueOf(progressChangedValue);
                //Log.d("progressChangedValue", "" +progress);
                if (isStoragePermissionGranted()) {
                    if (isCameraPermissionGranted()) {
                       globalProgressDialog.ProgressDialogShow(getActivity());
                        new RegisterAsyncTask2(getActivity()).execute();

                    }
                }
            }
        });

        choosefile_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //opening file chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
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

    class RegisterAsyncTask2 extends AsyncTask<String, String, String> {
        Activity activity;
        JSONObject jsonObject = null;

        public RegisterAsyncTask2(Activity activity) {
            this.activity = activity;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
           // globalProgressDialog.ProgressDialogShow(activity);
        }


        @Override
        public String doInBackground(String... params) {
            MultipartEntity multipartEntity;
            HttpPost post;
            HttpClient client;
            client = new DefaultHttpClient();
            post = new HttpPost("http://abgo.in/api/task_create.php");


            File file1 = new File("" + file);
            if(!file1.exists()){

               // Toast.makeText(,"Please select file",Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(getActivity(),"Please select file",Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("fileattach","fileattach");
            }
            else {
                multipartEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("file", new FileBody(file1));
            }





            multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("file", new FileBody(file1));

            try {


                multipartEntity.addPart("user_id", new StringBody(userid));
                multipartEntity.addPart("action", new StringBody("add_Activity"));
                multipartEntity.addPart("token", new StringBody(token));
                multipartEntity.addPart("task_id", new StringBody(taskid)); //taskid
                multipartEntity.addPart("progress", new StringBody(String.valueOf(progressValue)));

                multipartEntity.addPart("txt_note", new StringBody(activity_note));
                multipartEntity.addPart("sel_status", new StringBody(selected_status));

                post.setEntity(multipartEntity);
                HttpResponse responserr = null;
                responserr = client.execute(post);
                HttpEntity resEntity = responserr.getEntity();
                jsonObjectStr = EntityUtils.toString(resEntity);
                jsonObject = new JSONObject(jsonObjectStr);
                response = jsonObject.getString("message");

                //Log.i("MultiPart", multipartEntity.toString());
                if (response.contains("Success")) {
                    //ClearAllFields();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            globalProgressDialog.dismissProgressDialog();
                            add_activity_note.setText("");
                            Toast.makeText(getActivity(), "Success: Task Activity Updated.", Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(activity, "Something went wrong." + message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        globalProgressDialog.dismissProgressDialog();
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
