package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskModel {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data_task")
@Expose
private List<RecentTaskModel> dataTask = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<RecentTaskModel> getDataTask() {
return dataTask;
}

public void setDataTask(List<RecentTaskModel> dataTask) {
this.dataTask = dataTask;
}

}