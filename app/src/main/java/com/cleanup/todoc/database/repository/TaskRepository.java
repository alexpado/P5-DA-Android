package com.cleanup.todoc.database.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;

import java.util.List;

@Dao
public interface TaskRepository {

    @Query("SELECT * FROM task")
    LiveData<List<TaskWithProject>> findAll();

    @Query("SELECT * FROM task WHERE projectId = :projectId")
    LiveData<List<TaskWithProject>> findAllByProject(long projectId);

    @Insert
    void saveAll(Task... tasks);

    @Delete
    void delete(Task task);
}
