package com.cleanup.todoc.ui.components;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.cleanup.todoc.model.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectContainer {

    private final ArrayAdapter<Project> adapter;

    public ProjectContainer(Context context) {
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setContent(List<Project> projects) {
        this.adapter.clear();
        int i = 0;
        for (Project project : projects) {
            this.adapter.insert(project, i++);
        }

        this.adapter.notifyDataSetChanged();
    }

    public ArrayAdapter<Project> getAdapter() {
        return adapter;
    }
}
