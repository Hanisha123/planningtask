package com.example.taskplannernew.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("emp_code")
    @Expose
    private String empCode;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("designation_name")
    @Expose
    private String designationName;
    @SerializedName("emp_phone")
    @Expose
    private String empPhone;
    @SerializedName("emp_email")
    @Expose
    private String empEmail;
    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("report_to")
    @Expose
    private String reportTo;
    @SerializedName("message")
    @Expose
    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}