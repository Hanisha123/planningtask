package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllJuniorsModel {

    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("emp_code")
    @Expose
    private String empCode;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("report_to_id")
    @Expose
    private String reportToId;
    @SerializedName("depart_id")
    @Expose
    private String departId;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("designation")
    @Expose
    private String designation;

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

    public String getReportToId() {
        return reportToId;
    }

    public void setReportToId(String reportToId) {
        this.reportToId = reportToId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }



}
