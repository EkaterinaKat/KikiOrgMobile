package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.view.utils.MainTaskRecycleView;

import java.util.Date;

public class TaskListFragment extends Fragment {
    private MainTaskRecycleView.TaskListAdapter taskListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        RecyclerView taskList = view.findViewById(R.id.main_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));

        taskList.setAdapter(taskListAdapter);
        return view;
    }

    void initAdapter(Date date, MainActivity mainActivity) {
        taskListAdapter = new MainTaskRecycleView.TaskListAdapter(mainActivity, date);
    }

    public void setDate(Date date) {
        taskListAdapter.setDate(date);
    }
}