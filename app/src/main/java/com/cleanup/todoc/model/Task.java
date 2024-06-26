package com.cleanup.todoc.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Objects;

/**
 * <p>Model for the tasks of the application.</p>
 *
 * @author Gaëtan HERFRAY
 */
@Entity
public class Task {

    /**
     * The unique identifier of the task
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The unique identifier of the project associated to the task
     */
    private long projectId;

    /**
     * The name of the task
     *
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private String name;

    /**
     * The timestamp when the task has been created
     */
    private long creationTimestamp;

    /**
     * Instantiates a new Task.
     *
     * @param projectId
     *         the project id associated to the task to set
     * @param name
     *         the name of the task to set
     * @param creationTimestamp
     *         the timestamp when the task has been created to set
     */
    public Task(long projectId, @NonNull String name, long creationTimestamp) {

        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    /**
     * Returns the unique identifier of the task.
     *
     * @return the unique identifier of the task
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the unique identifier of the task.
     *
     * @param id
     *         the unique idenifier of the task to set
     */
    public void setId(long id) {

        this.id = id;
    }

    public long getProjectId() {

        return projectId;
    }

    public void setProjectId(long projectId) {

        this.projectId = projectId;
    }

    /**
     * Returns the name of the task.
     *
     * @return the name of the task
     */
    @NonNull
    public String getName() {

        return name;
    }

    /**
     * Sets the name of the task.
     *
     * @param name
     *         the name of the task to set
     */
    public void setName(@NonNull String name) {

        this.name = name;
    }

    /**
     * Sets the timestamp when the task has been created.
     *
     * @param creationTimestamp
     *         the timestamp when the task has been created to set
     */
    public void setCreationTimestamp(long creationTimestamp) {

        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Get the timestamp when the task has been created.
     *
     * @return the timestamp when the task has been created
     */
    public long getCreationTimestamp() {

        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {

        return String.format(
                "Task{id=%s, projectId=%s, name=%s, creationTimestamp=%s}",
                this.id,
                this.projectId,
                this.name,
                this.creationTimestamp
        );
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<Task> {

        @Override
        public int compare(Task left, Task right) {

            return left.name.compareTo(right.name);
        }

    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {

        @Override
        public int compare(Task left, Task right) {

            return right.name.compareTo(left.name);
        }

    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {

        @Override
        public int compare(Task left, Task right) {

            return (int) (right.creationTimestamp - left.creationTimestamp);
        }

    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {

        @Override
        public int compare(Task left, Task right) {

            return (int) (left.creationTimestamp - right.creationTimestamp);
        }

    }

}
