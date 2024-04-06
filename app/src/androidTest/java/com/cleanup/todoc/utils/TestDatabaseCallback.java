package com.cleanup.todoc.utils;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.ApplicationDatabase;
import com.cleanup.todoc.model.Project;

public class TestDatabaseCallback extends RoomDatabase.Callback {

    /**
     * Return all default {@link Project} instances to add to the database when it is first
     * created.
     *
     * @return An array of {@link Project}
     */
    public static Project[] projects() {

        return new Project[]{
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2)
        };
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {

        super.onCreate(db);
        ApplicationDatabase.run(
                () -> ApplicationDatabase.getInstance().projectRepository().saveAll(projects())
        );
    }

}
