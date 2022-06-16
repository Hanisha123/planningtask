package com.example.taskplannernew.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataDept {

    @SerializedName("depart_id")
    @Expose
    private String departId;
    @SerializedName("department_name")
    @Expose
    private String departmentName;

    @SerializedName("data_employees")
    @Expose
    private List<DataEmployee> dataEmployees = null;
    @SerializedName("emp_data")
    @Expose
    private List<EmployeeData> empData = null;

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public List<DataEmployee> getDataEmployees() {
        return dataEmployees;
    }

    public void setDataEmployees(List<DataEmployee> dataEmployees) {
        this.dataEmployees = dataEmployees;
    }


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<EmployeeData> getEmpData() {
        return empData;
    }

    public void setEmpData(List<EmployeeData> empData) {
        this.empData = empData;
    }
}
