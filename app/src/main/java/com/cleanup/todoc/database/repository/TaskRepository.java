package com.cleanup.todoc.database.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;

import java.util.List;

/**
 * Interface defining all the possible manipulation for {@link Task} in the database.
 */
@Dao
public interface TaskRepository {

    @Query("SELECT * FROM task")
    LiveData<List<TaskWithProject>> findAll();

    @Insert
    void saveAll(Task... tasks);

    @Delete
    void delete(Task task);

}
