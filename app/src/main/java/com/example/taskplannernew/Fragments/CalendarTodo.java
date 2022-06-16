package com.example.taskplannernew.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Adapter.CalendarRecentTaskAdapter;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.Models.TaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;

public class CalendarTodo extends Fragment  {

    //private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private OnFragmentInteractionListener mListener;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
 //   TextView textView;
    String userid,token;

    SharePref sharePref;

    List<EventDay> mEventDays;
    EventDay myEventDay;
    java.util.Calendar cal;
    ArrayList<RecentTaskModel> recentTaskModels = null;
    RecentTaskModel recentTaskModel;
    GlobalProgressDialog globalProgressDialog;
    public CalendarTodo() {
        // Required empty public constructor
    }


    public static CalendarTodo newInstance(String param1, String param2) {
        CalendarTodo fragment = new CalendarTodo();

        return fragment;
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
    private CalendarView mCalendarView;
    private RecyclerView recycl_view;


   // HashMap<Long,RecentTaskModel> hashMap = new HashMap<Long,RecentTaskModel>();


    HashMap<Long,ArrayList<RecentTaskModel>> hashMapMultiple = new HashMap<Long,ArrayList<RecentTaskModel>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("TODO CALENDAR");
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        toolbarTextView.setLayoutParams(params);
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        //textView = view.findViewById(R.id.textView);
        recycl_view = view.findViewById(R.id.recycl_view);
        mCalendarView = view.findViewById(R.id.calendarView);

        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        Log.d("userid::::11",""+userid);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("action", "AllTaskList");


        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<TaskModel> call = jsonPostService.getAllTaskData(jsonObject);
        call.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                globalProgressDialog.dismissProgressDialog();

                try {
                    globalProgressDialog.dismissProgressDialog();

                    Log.i("onResponse", call.toString());
                    TaskModel taskModel = new TaskModel();
                    taskModel = response.body();

                    if (taskModel.getMessage().equalsIgnoreCase("success")) {
                        ArrayList<RecentTaskModel> recentTaskModels = null;
                        mEventDays = new ArrayList<>();
                        for (int i = 0; i < taskModel.getDataTask().size(); i++) {
                            recentTaskModel = new RecentTaskModel();
                            recentTaskModel = taskModel.getDataTask().get(i);
                            String[] assignedToIds = recentTaskModel.getAssigntoId().split(",");
                            long createdById = Long.parseLong(recentTaskModel.getCreatedbyId());
                            if (!recentTaskModel.getFromdate().isEmpty()) {
                                Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(recentTaskModel.getFromdate());

                                //hashMap.put(fromDate.getTime(),recentTaskModel);
                                for(String tempId : assignedToIds) {
                                    long currAssignedToId = Long.parseLong(tempId);
                                    Log.d("assignedToId", "assignedToId:" + currAssignedToId + "::" + createdById);
                                    if (currAssignedToId == createdById) {
                                        if (hashMapMultiple.containsKey(fromDate.getTime())) {
                                            recentTaskModels = hashMapMultiple.get(fromDate.getTime());
                                        } else {
                                            recentTaskModels = new ArrayList<>();
                                        }
                                        recentTaskModels.add(recentTaskModel);
                                        hashMapMultiple.put(fromDate.getTime(), recentTaskModels);
                                        Log.i("mCalendarView", "fromDate.getTime()" + fromDate.getTime());
                                        java.util.Calendar calendarTemp = GregorianCalendar.getInstance();
                                        calendarTemp.setTime(fromDate);

                                        if (recentTaskModel.getTaskStatus().equals("PENDING")) {
                                            myEventDay = new EventDay(calendarTemp, R.drawable.cal_pending);
                                        } else if (recentTaskModel.getTaskStatus().equals("ON-PROCESS")) {
                                            myEventDay = new EventDay(calendarTemp, R.drawable.cal_onprocess);
                                        } else if (recentTaskModel.getTaskStatus().equals("RE-OPEN")) {
                                            myEventDay = new EventDay(calendarTemp, R.drawable.cal_onprocess);
                                        } else if (recentTaskModel.getTaskStatus().equals("COMPLETED")) {
                                            myEventDay = new EventDay(calendarTemp, R.drawable.cal_completed);
                                        }
                                        // myEventDay = new EventDay(calendarTemp, R.drawable.calendar_image1);
                                        // mEventDays.add(new EventDay(calendar, DrawableUtils.getCircleDrawableWithText(this, "M")));
                                        mEventDays.add(myEventDay);
                                    }
                                }
                            }
                            else {
                                //Toast.makeText(getActivity(),"No Task Assigned this month",Toast.LENGTH_SHORT).show();

                            }
                            mCalendarView.setEvents(mEventDays);

                        }

                        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
                            @Override
                            public void onDayClick(EventDay eventDay) {
                                java.util.Calendar clickedDayCalendar = eventDay.getCalendar();
                                Log.i("mCalendarView","eventDay"+clickedDayCalendar);
                                Log.i("mCalendarView","eventDay"+clickedDayCalendar.getTime());
                                Log.i("mCalendarView","eventDay"+clickedDayCalendar.getTimeInMillis());
                                Log.i("mCalendarView","eventDay in millis"+hashMapMultiple.containsKey(clickedDayCalendar.getTime()));
                                if(hashMapMultiple.containsKey(clickedDayCalendar.getTimeInMillis())){
                                    //final Dialog dialog = new Dialog(getActivity());
                                   // dialog.setContentView(R.layout.dialog_recent_task);
                                    //Window window = dialog.getWindow();
                                  ////  window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                  //  RecyclerView recyclerView = (RecyclerView)dialog.findViewById(R.id.recent_task_rv);
                                    recycl_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    CalendarRecentTaskAdapter adapter = new CalendarRecentTaskAdapter(hashMapMultiple.get(clickedDayCalendar.getTimeInMillis()), getActivity());
                                    recycl_view.setAdapter(adapter);
                                    globalProgressDialog.dismissProgressDialog();


//                                    AppCompatImageView dialogButton = (AppCompatImageView) dialog.findViewById(R.id.btn_ok);
//                                    // if button is clicked, close the custom dialog
//                                    dialogButton.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                    dialog.show();
                                }
                                else{
                                    Toast.makeText(getActivity(),"No Task Assigned",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        /*for (Map.Entry<Long,RecentTaskModel> entry : hashMap.entrySet()){
                            System.out.println("Key = " + entry.getKey() +
                                    ", Value = " + entry.getValue());
                        }*/
                    } else {
                        globalProgressDialog.dismissProgressDialog();

                        Toast.makeText(getActivity(), taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail", "fail");
                Log.e("response-failure", call.toString());
                Log.i("onFailure",t.getMessage());
                t.printStackTrace();
            }
        });

        List<java.util.Calendar> selectedDates = new ArrayList<java.util.Calendar>();
        cal = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        java.util.Calendar calendar = new GregorianCalendar(2019,9,19);
        System.out.println(sdf.format(calendar.getTime()));
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        selectedDates.add(calendar);
        mCalendarView.setSelectedDates(selectedDates);
        //mCalendarView.setcol




        return view;
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_calendar, container, false);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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


}
