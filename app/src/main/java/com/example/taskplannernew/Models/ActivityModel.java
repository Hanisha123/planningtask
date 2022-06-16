package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_task_activity")
    @Expose
    private List<ActivityTaskModel> dataTaskActivity = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ActivityTaskModel> getDataTaskActivity() {
        return dataTaskActivity;
    }

    public void setDataTaskActivity(List<ActivityTaskModel> dataTaskActivity) {
        this.dataTaskActivity = dataTaskActivity;
    }
}
