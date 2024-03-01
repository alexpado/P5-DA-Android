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

@Database(
        entities = {
                Project.class,
                Task.class
        },
        version = 1
)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase instance = null;

    public static boolean hasInstance() {
        return !Objects.isNull(instance);
    }

    public static ApplicationDatabase getInstance() {
        return instance;
    }

    public static ApplicationDatabase initialize(Context context) {
        if (!hasInstance()) {
            instance = Room.databaseBuilder(context, ApplicationDatabase.class, "todoc")
                    .addCallback(new CallBack()).build();
        }
        return getInstance();
    }

    public static void run(Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    public abstract ProjectRepository projectRepository();

    public abstract TaskRepository taskRepository();

    static class CallBack extends RoomDatabase.Callback {

        public static Project[] getAllProjects() {
            return new Project[]{
                    new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                    new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                    new Project(3L, "Projet Circus", 0xFFA3CED2),
            };
        }

        public static Task[] getAllTask() {
            Project[] projects = getAllProjects();

            return new Task[]{
                    new Task(projects[0].getId(), "Ajouter un header sur le site", System.currentTimeMillis()),
                    new Task(projects[1].getId(), "Modifier la couleur des textes", System.currentTimeMillis()),
                    new Task(projects[1].getId(), "Appeler le client", System.currentTimeMillis()),
                    new Task(projects[0].getId(), "Intégrer Google Analytics", System.currentTimeMillis()),
                    new Task(projects[2].getId(), "Ajouter un header sur le site", System.currentTimeMillis())
            };
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(() -> {
                instance.projectRepository().saveAll(getAllProjects());
                instance.taskRepository().saveAll(getAllTask());
            });
        }
    }
}
