package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.database.repository.ProjectRepository;
import com.cleanup.todoc.database.repository.TaskRepository;

import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Class handling the database connection through android room library.
 */
@Database(entities = {Project.class, Task.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase instance = null;

    /**
     * Check if the database has been instanced yet.
     *
     * @return True if an instance of {@link ApplicationDatabase} exists.
     */
    public static boolean hasInstance() {

        return !Objects.isNull(instance);
    }

    /**
     * Retrieve the instance of {@link ApplicationDatabase} whatever its instance state. You
     * probably should use {@link #hasInstance()} before doing further calls on this method return
     * value.
     *
     * @return The possibly null {@link ApplicationDatabase} instance.
     */
    public static ApplicationDatabase getInstance() {

        return instance;
    }

    /**
     * Initialize the database if it hasn't been initialized yet.
     *
     * @param context
     *         The application context for which the database will be created.
     *
     * @return An {@link ApplicationDatabase} instance.
     */
    public static ApplicationDatabase initialize(Context context) {

        if (!hasInstance()) {
            instance = Room.databaseBuilder(context, ApplicationDatabase.class, "todoc")
                           .addCallback(new CallBack())
                           .build();
        }
        return getInstance();
    }

    /**
     * Utility method than run the provided {@link Runnable} on a separate thread. This can be
     * useful to run queries against the database when in the android UI thread. This also can be
     * used without interacting with the database as a mean to just run something outside the main
     * thread.
     *
     * @param runnable
     *         The {@link Runnable} to execute on a separate thread.
     */
    public static void run(Runnable runnable) {

        Executors.newSingleThreadExecutor().execute(runnable);
    }

    /**
     * Retrieve the {@link ProjectRepository} instance allowing to interact with {@link Project} in
     * database.
     *
     * @return A {@link ProjectRepository} proxy instance.
     */
    public abstract ProjectRepository projectRepository();

    /**
     * Retrieve the {@link TaskRepository} instance allowing to interact with {@link Task} in
     * database.
     *
     * @return A {@link TaskRepository} proxy instance.
     */
    public abstract TaskRepository taskRepository();

    /**
     * Callback class used to initialize the database default data when it is created for the first
     * time.
     */
    public static class CallBack extends RoomDatabase.Callback {

        /**
         * Return all default {@link Project} instances to add to the database when it is first
         * created.
         *
         * @return An array of {@link Project}
         */
        public static Project[] getAllProjects() {

            return new Project[]{
                    new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                    new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                    new Project(3L, "Projet Circus", 0xFFA3CED2),
                    };
        }

        /**
         * Return all default {@link Task} instances to add to the database when it is first
         * created.
         *
         * @return An array of {@link Task}
         */
        public static Task[] getAllTask() {

            Project[] projects = getAllProjects();

            return new Task[]{
                    new Task(
                            projects[0].getId(),
                            "Ajouter un header sur le site",
                            System.currentTimeMillis()
                    ),
                    new Task(
                            projects[1].getId(),
                            "Modifier la couleur des textes",
                            System.currentTimeMillis()
                    ),
                    new Task(
                            projects[1].getId(),
                            "Appeler le client",
                            System.currentTimeMillis()
                    ),
                    new Task(
                            projects[0].getId(),
                            "Intégrer Google Analytics",
                            System.currentTimeMillis()
                    ),
                    new Task(
                            projects[2].getId(),
                            "Ajouter un header sur le site",
                            System.currentTimeMillis()
                    )
            };
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            super.onCreate(db);

            ApplicationDatabase.run(() -> {
                instance.projectRepository().saveAll(getAllProjects());
                instance.taskRepository().saveAll(getAllTask());
            });
        }

    }

}
