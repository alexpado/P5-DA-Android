package com.cleanup.todoc.utils;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding test data for unit tests.
 */
public final class UnitTestDataBank {

    private final List<Project>         projects         = new ArrayList<>();
    private final List<Task>            tasks            = new ArrayList<>();
    private final List<TaskWithProject> taskWithProjects = new ArrayList<>();

    public UnitTestDataBank() {

        long    increment = 0;
        Project project1  = new Project(++increment, "Project A", 0xFFEADAD1);
        Project project2  = new Project(++increment, "Project B", 0xFFB4CDBA);
        Project project3  = new Project(++increment, "Project C", 0xFFA3CED2);
        Project project4  = new Project(++increment, "Project D", 0xFFB4CED2);

        Task task1 = new Task(project1.getId(), "aaa", 3);
        Task task2 = new Task(project2.getId(), "zzz", 2);
        Task task3 = new Task(project3.getId(), "hhh", 0);
        Task task4 = new Task(project4.getId(), "bbb", 1);

        // Enforce IDs on tasks
        task1.setId(project1.getId());
        task2.setId(project2.getId());
        task3.setId(project3.getId());
        task4.setId(project4.getId());

        // Create relationship items
        TaskWithProject twp1 = TaskWithProject.build(project1, task1);
        TaskWithProject twp2 = TaskWithProject.build(project2, task2);
        TaskWithProject twp3 = TaskWithProject.build(project3, task3);
        TaskWithProject twp4 = TaskWithProject.build(project4, task4);

        // Fill up lists
        this.projects.add(project1);
        this.projects.add(project2);
        this.projects.add(project3);
        this.projects.add(project4);
        this.tasks.add(task1);
        this.tasks.add(task2);
        this.tasks.add(task3);
        this.tasks.add(task4);
        this.taskWithProjects.add(twp1);
        this.taskWithProjects.add(twp2);
        this.taskWithProjects.add(twp3);
        this.taskWithProjects.add(twp4);
    }

    public List<TaskWithProject> getTaskWithProjects() {

        return taskWithProjects;
    }

}
