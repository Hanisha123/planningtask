package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JuniorModel {

@SerializedName("message")
@Expose
private String message;
@SerializedName("allJuniors")
@Expose
private List<AllJuniorsModel> allJuniors  = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<AllJuniorsModel> getAllJuniors() {
return allJuniors;
}

public void setDataTask(List<AllJuniorsModel> allJuniors) {
this.allJuniors = allJuniors;
}

}