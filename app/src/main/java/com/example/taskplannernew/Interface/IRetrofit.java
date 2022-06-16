package com.example.taskplannernew.Interface;


import com.example.taskplannernew.Activity.LoginModel;
import com.example.taskplannernew.Models.ActivityModel;
import com.example.taskplannernew.Models.ActivityTaskModel;
import com.example.taskplannernew.Models.DashboardModel;
import com.example.taskplannernew.Models.DepartmentModel;
import com.example.taskplannernew.Models.JuniorModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.Models.TaskModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Anand singh on 12/31/2016.
 */

public interface IRetrofit {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Content-Transfer-Encoding: binary",
            "Content-Type: text/plain; charset=utf-8"
    })


    @POST("auth_login.php")
    Call<LoginModel> getAllexpenseData(@Body JsonObject jsonObject);

    @POST("task.php")
    Call<TaskModel> getAllTaskData(@Body JsonObject jsonObject);

    @POST("dashboard.php")
    Call<JuniorModel> getAllJuniors(@Body JsonObject jsonObject);

    @POST("task.php")
    Call<ActivityModel> getAllActivity(@Body JsonObject jsonObject);

   /* @POST("task_create.php")
    Call<AssignTaskModel> setAllAssignTask(@Body JsonObject jsonObject);
*/
    //REOPEN TASK API
    @POST("approve_reopen.php")
    Call<TaskModel> reopenTask(@Body JsonObject jsonObject);

    @POST("brand_department_info.php")
    Call<DepartmentModel> getAllDepartment(@Body JsonObject jsonObject);


    @POST("task_create.php")
    Call<TaskModel> createTask1(@Body JsonObject jsonObject);

    @POST("task.php")
    Call<TaskModel> updateTask(@Body JsonObject jsonObject);

    @Multipart
    @POST("task_create.php")
    Call<TaskModel> createTask(
            @Part("user_id") String user_id,@Part("action") String action,
                               @Part("token") String token,@Part("depart_id") String depart_id,
                               @Part("sel_emp_ids") String sel_emp_ids,
                               @Part("brand_id") String brand_id,@Part("subject") String subject,
                               @Part("dat_from") String dat_from,@Part("dat_to") String dat_to,
                               @Part("priority") String priority, @Part("description") String description,
                               @Part MultipartBody.Part file);


    @POST("task.php")
    Call<RecentTaskModel> getTaskDetail(@Body JsonObject jsonObject);

    @POST("dashboard.php")
    Call<DashboardModel> getDashboardDetail(@Body JsonObject jsonObject);



}
