package com.example.taskplannernew.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_dept")
    @Expose
    private List<DataDept> dataDept = null;
    @SerializedName("data_employees")
    @Expose
    private List<DataEmployee> dataEmployees = null;
    @SerializedName("data_brand")
    @Expose
    private List<DataBrand> dataBrand = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDept> getDataDept() {
        return dataDept;
    }

    public void setDataDept(List<DataDept> dataDept) {
        this.dataDept = dataDept;
    }

    public List<DataEmployee> getDataEmployees() {
        return dataEmployees;
    }

    public void setDataEmployees(List<DataEmployee> dataEmployees) {
        this.dataEmployees = dataEmployees;
    }

    public List<DataBrand> getDataBrand() {
        return dataBrand;
    }

    public void setDataBrand(List<DataBrand> dataBrand) {
        this.dataBrand = dataBrand;
    }

}
