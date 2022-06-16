package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_team_size")
    @Expose
    private String totalTeamSize;
    @SerializedName("task_ON_PROCESS")
    @Expose
    private String taskONPROCESS;
    @SerializedName("task_COMPLETED")
    @Expose
    private String taskCOMPLETED;
    @SerializedName("task_PENDING")
    @Expose
    private String taskPENDING;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalTeamSize() {
        return totalTeamSize;
    }

    public void setTotalTeamSize(String totalTeamSize) {
        this.totalTeamSize = totalTeamSize;
    }

    public String getTaskONPROCESS() {
        return taskONPROCESS;
    }

    public void setTaskONPROCESS(String taskONPROCESS) {
        this.taskONPROCESS = taskONPROCESS;
    }

    public String getTaskCOMPLETED() {
        return taskCOMPLETED;
    }

    public void setTaskCOMPLETED(String taskCOMPLETED) {
        this.taskCOMPLETED = taskCOMPLETED;
    }

    public String getTaskPENDING() {
        return taskPENDING;
    }

    public void setTaskPENDING(String taskPENDING) {
        this.taskPENDING = taskPENDING;
    }
}
