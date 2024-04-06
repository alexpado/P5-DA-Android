package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.ApplicationDatabase;
import com.cleanup.todoc.enums.SortMethod;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import com.cleanup.todoc.ui.components.ProjectContainer;
import com.cleanup.todoc.ui.components.TasksAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    /**
     * The container for projects
     */
    private ProjectContainer projectContainer;

    /**
     * The container for tasks
     */
    private TasksAdapter tasksAdapter;

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    private TextView lblNoTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listTasks  = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        listTasks.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
        this.init();
    }

    public void init() {

        if (!ApplicationDatabase.hasInstance()) {
            ApplicationDatabase.createProductionDatabase(this.getApplicationContext());
        }

        this.tasksAdapter     = new TasksAdapter(new ArrayList<>(), this);
        this.projectContainer = new ProjectContainer(this.getApplicationContext());

        ApplicationDatabase.getInstance()
                           .projectRepository()
                           .findAll()
                           .observe(this, this.projectContainer::setContent);

        ApplicationDatabase.getInstance()
                           .taskRepository()
                           .findAll()
                           .observe(this, this::updateTasks);

        this.listTasks.setAdapter(this.tasksAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            this.tasksAdapter.setSortMethod(SortMethod.ALPHABETICAL);
        } else if (id == R.id.filter_alphabetical_inverted) {
            this.tasksAdapter.setSortMethod(SortMethod.ALPHABETICAL_INVERTED);
        } else if (id == R.id.filter_oldest_first) {
            this.tasksAdapter.setSortMethod(SortMethod.OLD_FIRST);
        } else if (id == R.id.filter_recent_first) {
            this.tasksAdapter.setSortMethod(SortMethod.RECENT_FIRST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {

        ApplicationDatabase.run(
                () -> ApplicationDatabase.getInstance().taskRepository().delete(task)
        );
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface
     *         the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {

        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner  = dialog.findViewById(R.id.project_spinner);

        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(this.projectContainer.getAdapter());
        }
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task
     *         the task to be added to the list
     */
    private void addTask(@NonNull Task task) {

        ApplicationDatabase.run(
                () -> ApplicationDatabase.getInstance().taskRepository().saveAll(task)
        );
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<TaskWithProject> tasks) {

        if (tasks.isEmpty()) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            this.tasksAdapter.updateTasks(tasks);

            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner  = null;
            dialog         = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });

        return dialog;
    }

}
