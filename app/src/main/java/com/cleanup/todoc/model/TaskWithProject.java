package com.cleanup.todoc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Comparator;

/**
 * Model class allowing to describe the association of both {@link Project} and {@link Task} in the
 * database.
 */
public class TaskWithProject {

    public static TaskWithProject build(Project project, Task task) {

        if (project.getId() != task.getId()) {
            throw new IllegalArgumentException(
                    "The project and task provided doesn't match the relation constraint (task.projectId == project.id)"
            );
        }

        TaskWithProject twp = new TaskWithProject();
        twp.setProject(project);
        twp.setTask(task);

        return twp;
    }

    @Embedded
    private Task task;

    @Relation(
            parentColumn = "projectId",
            entityColumn = "id"
    )
    private Project project;

    public Task getTask() {

        return task;
    }

    public void setTask(Task task) {

        this.task = task;
    }

    public Project getProject() {

        return project;
    }

    public void setProject(Project project) {

        this.project = project;
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<TaskWithProject> {

        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {

            return new Task.TaskAZComparator().compare(left.getTask(), right.getTask());
        }

    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<TaskWithProject> {

        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {

            return new Task.TaskZAComparator().compare(left.getTask(), right.getTask());
        }

    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<TaskWithProject> {

        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {

            return new Task.TaskRecentComparator().compare(left.getTask(), right.getTask());
        }

    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<TaskWithProject> {

        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {

            return new Task.TaskOldComparator().compare(left.getTask(), right.getTask());
        }

    }

}
