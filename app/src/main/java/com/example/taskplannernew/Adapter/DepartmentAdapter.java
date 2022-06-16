package com.example.taskplannernew.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.taskplannernew.Models.DepartmentModel;

import java.util.List;

public class DepartmentAdapter extends ArrayAdapter<DepartmentModel> {
    private Context context;
    private List<DepartmentModel> objects;

    public DepartmentAdapter(Context context, int resource, List<DepartmentModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public void add(DepartmentModel object) {
        this.objects.add(object);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DepartmentModel getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        //label.setText(objects.get(position).getText());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        //label.setText(objects.get(position).getText());
        return label;
    }

}
