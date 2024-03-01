package com.cleanup.todoc.database.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface ProjectRepository {

    @Query("SELECT * FROM project")
    LiveData<List<Project>> findAll();

    @Query("SELECT * FROM project WHERE id = :id")
    LiveData<Project> findById(long id);

    @Insert
    void saveAll(Project... projects);

}
