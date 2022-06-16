package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecentTaskModel {

    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("assignto")
    @Expose
    private String assignto;
    @SerializedName("fromdate")
    @Expose
    private String fromdate;
    @SerializedName("todate")
    @Expose
    private String todate;
    @SerializedName("taskdescription")
    @Expose
    private String taskdescription;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("progress")
    @Expose
    private String progress;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("task_status")
    @Expose
    private String taskStatus;
    @SerializedName("task_type")
    @Expose
    private String taskType;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @SerializedName("assignby")
    @Expose
    private String assignby;
    @SerializedName("createdby")
    @Expose
    private String createdby;
    @SerializedName("assignby_id")
    @Expose
    private String assignbyId;
    @SerializedName("createdby_id")
    @Expose
    private String createdbyId;
    @SerializedName("assignto_id")
    @Expose
    private String assigntoId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAssignto() {
        return assignto;
    }

    public void setAssignto(String assignto) {
        this.assignto = assignto;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAssignby() {
        return assignby;
    }

    public void setAssignby(String assignby) {
        this.assignby = assignby;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getAssignbyId() {
        return assignbyId;
    }

    public void setAssignbyId(String assignbyId) {
        this.assignbyId = assignbyId;
    }

    public String getCreatedbyId() {
        return createdbyId;
    }

    public void setCreatedbyId(String createdbyId) {
        this.createdbyId = createdbyId;
    }

    public String getAssigntoId() {
        return assigntoId;
    }

    public void setAssigntoId(String assigntoId) {
        this.assigntoId = assigntoId;
    }


}
