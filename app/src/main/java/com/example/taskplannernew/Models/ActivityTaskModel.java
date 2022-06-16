package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityTaskModel {

    @SerializedName("activity_id")
    @Expose
    private String activityId;
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("activity_note")
    @Expose
    private String activityNote;
    @SerializedName("progress")
    @Expose
    private String progress;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("task_status")
    @Expose
    private String taskStatus;
    @SerializedName("manager_approval")
    @Expose
    private String managerApproval;
    @SerializedName("assignto")
    @Expose
    private String assignto;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getActivityNote() {
        return activityNote;
    }

    public void setActivityNote(String activityNote) {
        this.activityNote = activityNote;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(String managerApproval) {
        this.managerApproval = managerApproval;
    }

    public String getAssignto() {
        return assignto;
    }

    public void setAssignto(String assignto) {
        this.assignto = assignto;
    }

}
