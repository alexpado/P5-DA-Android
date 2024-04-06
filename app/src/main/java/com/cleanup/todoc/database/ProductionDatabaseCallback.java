package com.cleanup.todoc.database;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

public class ProductionDatabaseCallback extends RoomDatabase.Callback {

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
                new Project(3L, "Projet Circus", 0xFFA3CED2)
        };
    }

    /**
     * Return all default {@link Task} instances to add to the database when it is first created.
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
            ApplicationDatabase.getInstance().projectRepository().saveAll(getAllProjects());
            ApplicationDatabase.getInstance().taskRepository().saveAll(getAllTask());
        });
    }

}
