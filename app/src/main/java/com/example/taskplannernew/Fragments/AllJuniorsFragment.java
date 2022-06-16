package com.example.taskplannernew.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskplannernew.Activity.RetrofitInterface;
import com.example.taskplannernew.Adapter.AllJuniorAdapter;
import com.example.taskplannernew.Adapter.RecentTaskAdapter;
import com.example.taskplannernew.Interface.IRetrofit;
import com.example.taskplannernew.Models.AllJuniorsModel;
import com.example.taskplannernew.Models.JuniorModel;
import com.example.taskplannernew.Models.RecentTaskModel;
import com.example.taskplannernew.Models.TaskModel;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.taskplannernew.utils.ApiClient.BASE_URL;


public class AllJuniorsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<AllJuniorsModel> allJuniorsModelArrayList;
    SharePref sharePref;
    AllJuniorAdapter adapter;
    AllJuniorsModel allJuniorsModel;
    String userid,token,usertype;
    GlobalProgressDialog globalProgressDialog;


    private OnFragmentInteractionListener mListener;

    public AllJuniorsFragment() {
        // Required empty public constructor
    }

    public static AllJuniorsFragment newInstance(String param1, String param2) {
        AllJuniorsFragment fragment = new AllJuniorsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = new SharePref(getActivity());
        globalProgressDialog = new GlobalProgressDialog();
        globalProgressDialog.ProgressDialogShow(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.heading);
        toolbarTextView.setText("ALL JUNIORS");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_all_juniors, container, false);
        recyclerView = view.findViewById(R.id.all_juniors_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userid = sharePref.getLoginId();
        token = sharePref.getTOKEN();
        usertype = sharePref.getUserType();

        Log.d("userid::::",""+userid);
        Log.d("usertype::::",""+usertype);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("action", "AllJuniors");
        jsonObject.addProperty("token", token);

        IRetrofit jsonPostService = RetrofitInterface.createService(IRetrofit.class, BASE_URL);
        Call<JuniorModel> call = jsonPostService.getAllJuniors(jsonObject);
        call.enqueue(new Callback<JuniorModel>() {

            @Override
            public void onResponse(Call<JuniorModel> call, retrofit2.Response<JuniorModel> response) {
                globalProgressDialog.dismissProgressDialog();
                try {
                    globalProgressDialog.dismissProgressDialog();
                    JuniorModel juniorModel = new JuniorModel();
                    juniorModel = response.body();
                    globalProgressDialog.dismissProgressDialog();
                    if (juniorModel.getMessage().equalsIgnoreCase("success")) {
                        Log.d("responce",""+response.body());

                        globalProgressDialog.dismissProgressDialog();
                        allJuniorsModelArrayList = new ArrayList<AllJuniorsModel>();
                        for (int i = 0; i < juniorModel.getAllJuniors().size(); i++) {
                            allJuniorsModel = new AllJuniorsModel();

                            allJuniorsModel = juniorModel.getAllJuniors().get(i);
                            allJuniorsModelArrayList.add(allJuniorsModel);
                        }
                        adapter = new AllJuniorAdapter(allJuniorsModelArrayList, getActivity());
                        recyclerView.setAdapter(adapter);
                        globalProgressDialog.dismissProgressDialog();
                    } else {
                        Toast.makeText(getActivity(), juniorModel.getMessage(), Toast.LENGTH_SHORT).show();
                        globalProgressDialog.dismissProgressDialog();
                    }

                } catch (Exception e) {
                    globalProgressDialog.dismissProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JuniorModel> call, Throwable t) {
                globalProgressDialog.dismissProgressDialog();
                Log.d("fail", "fail");
                Log.e("response-failure", call.toString());
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
