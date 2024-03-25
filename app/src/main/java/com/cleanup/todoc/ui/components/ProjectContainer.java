package com.cleanup.todoc.ui.components;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.model.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class allowing to link a list of project to an adapter without needing to recreate the
 * array adapter every time.
 */
public class ProjectContainer {

    private final ArrayAdapter<Project> adapter;

    public ProjectContainer(Context context) {

        this.adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    /**
     * Define the list of {@link Project} for this container. This will refresh the internal state
     * of the {@link ArrayAdapter}.
     *
     * @param projects
     *         The {@link Project} list.
     */
    public void setContent(List<Project> projects) {

        this.adapter.clear();
        int i = 0;
        for (Project project : projects) {
            this.adapter.insert(project, i++);
        }

        this.adapter.notifyDataSetChanged();
    }

    /**
     * Retrieve the {@link ArrayAdapter} to use in a {@link RecyclerView} to display the list of
     * {@link Project}.
     *
     * @return An {@link ArrayAdapter} instance.
     */
    public ArrayAdapter<Project> getAdapter() {

        return adapter;
    }

}
