package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataEmployee {
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("emp_code")
    @Expose
    private String empCode;
    @SerializedName("emp_name")
    @Expose
    private String empName;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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
}
