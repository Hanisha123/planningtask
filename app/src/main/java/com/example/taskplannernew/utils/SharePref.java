package com.example.taskplannernew.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.taskplannernew.Activity.LoginModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.AllJuniorsModel;
import com.example.taskplannernew.Models.RecentTaskModel;

import retrofit2.Callback;

public class SharePref {
    private Context context;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String PREFRENCE_NAME = "abgo_pref";
    private static String LOGINSTATUS = "login_status";

    private static String EMP_CODE = "emp_code";
    private static String LOGIN_ID = "user_id";
    private static String EMP_NAME = "emp_name";
    private static String DESINATION_NAME = "designation_name";
    private static String EMP_PHONE = "emp_phone";
    private static String EMP_MAIL = "emp_email";
    private static String DEPARTMENT_NAME = "department_name";
    private static String USER_TYPE = "user_type";
    private static String REPORT_TO = "report_to";
    private static String MESSAGE = "message";
    private static String TOKEN = "token";

    //ChangeTAT
    private static String TASK_SUBJECT = "subject";
    private static String START_DATE = "fromdate";
    private static String END_DATE = "todate";
    private static String TASK_STATUS = "task_status";
    private static String TASK_ID = "task_id";
    private static String ASSIGNED_ID = "assignto_id";


    public SharePref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setLoginSuccess(boolean loginSucess) {
        editor = sharedPreferences.edit();
        editor.putBoolean(LOGINSTATUS, loginSucess);
        editor.commit();
    }

    public boolean getLoginSuccess() {
        return sharedPreferences.getBoolean(LOGINSTATUS, false);
    }

    public void setLoginId(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(LOGIN_ID, id);
        editor.commit();
    }

    public String getLoginId() {
        return sharedPreferences.getString(LOGIN_ID, null);
    }

    //TOKEN
    public void setTOKENSuccess(boolean tokenSucess) {
        editor = sharedPreferences.edit();
        editor.putBoolean(TOKEN, tokenSucess);
        editor.commit();
    }

    public boolean getTOKENSuccess(String b) {
        return sharedPreferences.getBoolean(TOKEN, false);
    }


    public void setTOKEN(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(TOKEN, id);
        editor.commit();
    }

    public String getTOKEN() {
        return sharedPreferences.getString(TOKEN, null);
    }
    //TASKID
    public void setTaskId(String taskId) {

        editor = sharedPreferences.edit();
        editor.putString(TASK_ID, taskId);
        editor.commit();
    }

    public String getTaskId() {
        return sharedPreferences.getString(TASK_ID, "");
    }

    //ASSIGNEDID
    public void setAssignedId(String assignedId) {

        editor = sharedPreferences.edit();
        editor.putString(ASSIGNED_ID, assignedId);
        editor.commit();
    }

    public String getAssignedId() {
        return sharedPreferences.getString(ASSIGNED_ID, "");
    }
    //Task Subject
    public String setSubject(Callback<RecentTaskModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(TASK_SUBJECT, id);
        editor.commit();
        return id;

    }

    public String getSubject() {
        return sharedPreferences.getString(TASK_SUBJECT, null);
    }

    //USER TYPE
    public void setUserType(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(USER_TYPE, id);
        editor.commit();
    }

    public String getUserType() {
        return sharedPreferences.getString(USER_TYPE, null);
    }

    //TASK STATUS
    public String setTaskStatus(Callback<ActivityTaskModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(TASK_STATUS, id);
        editor.commit();
        return id;
    }

    public String getTaskStatus() {
        return sharedPreferences.getString(TASK_STATUS, null);
    }

    //Task Start Date
    public void setStartDate(Callback<RecentTaskModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(START_DATE, id);
        editor.commit();
    }

    public String getStartDate() {
        return sharedPreferences.getString(START_DATE, null);
    }

    //Task End Date
    public void setEndDate(Callback<RecentTaskModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(END_DATE, id);
        editor.commit();
    }

    public String getEndDate() {
        return sharedPreferences.getString(END_DATE, null);
    }

    //Employee Code
    public void setEmpCode(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(EMP_CODE, id);
        editor.commit();
    }

    public String getEmpCode() {
        return sharedPreferences.getString(EMP_CODE, null);
    }

    //Employee Name
    public void setEmpName(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(EMP_NAME, id);
        editor.commit();
    }

    public String getEmpName() {
        return sharedPreferences.getString(EMP_NAME, null);
    }

    //Desination Name
    public void setDesinationName(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(DESINATION_NAME, id);
        editor.commit();
    }

    public String getDesinationName() {
        return sharedPreferences.getString(DESINATION_NAME, null);
    }

    //EMP PHONE
    public void setEmpPhone(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(EMP_PHONE, id);
        editor.commit();
    }

    public String getEmpPhone() {
        return sharedPreferences.getString(EMP_PHONE, null);
    }

    //EMP EMAIL
    public void setEmpMail(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(EMP_MAIL, id);
        editor.commit();
    }

    public String getEmpMail() {
        return sharedPreferences.getString(EMP_MAIL, null);
    }

    //DEPARTMENT NAME
    public void setDepartmentName(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(DEPARTMENT_NAME, id);
        editor.commit();
    }

    public String getDepartmentName() {
        return sharedPreferences.getString(DEPARTMENT_NAME, null);
    }



    //REPORT TO
    public void setReportTo(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(REPORT_TO, id);
        editor.commit();
    }

    public String getReportTo() {
        return sharedPreferences.getString(REPORT_TO, null);
    }

    //MESSAGE
    public void setMESSAGE(Callback<LoginModel> callback, String id) {

        editor = sharedPreferences.edit();
        editor.putString(MESSAGE, id);
        editor.commit();
    }

    public String getMESSAGE() {
        return sharedPreferences.getString(MESSAGE, null);
    }



}
