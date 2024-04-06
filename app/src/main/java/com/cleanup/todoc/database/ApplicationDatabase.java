package com.cleanup.todoc.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cleanup.todoc.database.repository.ProjectRepository;
import com.cleanup.todoc.database.repository.TaskRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

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
     * Create an instance of {@link ApplicationDatabase} that resides in memory. This is
     * particularly useful when it comes to instrumented tests.
     *
     * @param context
     *         The {@link Context} to use to create the {@link ApplicationDatabase}.
     * @param customCallback
     *         The {@link RoomDatabase.Callback} instance to use when building the
     *         {@link ApplicationDatabase} instance.
     *
     * @return An {@link ApplicationDatabase} instance.
     */
    public static ApplicationDatabase createInMemoryDatabase(Context context, RoomDatabase.Callback customCallback) {

        if (hasInstance()) {
            instance.close();
        }
        instance = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase.class)
                       .addCallback(customCallback)
                       .build();

        return getInstance();
    }

    /**
     * Create an instance of {@link ApplicationDatabase} that reside on the device. This is the
     * production database.
     *
     * @param context
     *         The {@link Context} to use to create the {@link ApplicationDatabase}.
     *
     * @return An {@link ApplicationDatabase} instance.
     */
    public static ApplicationDatabase createProductionDatabase(Context context) {

        if (hasInstance()) {
            instance.close();
        }
        instance = Room.databaseBuilder(context, ApplicationDatabase.class, "todoc")
                       .addCallback(new ProductionDatabaseCallback())
                       .build();
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

}
