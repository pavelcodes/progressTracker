package com.example.pavelplakhotny_c196.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pavelplakhotny_c196.database.dao.AssessmentDao;
import com.example.pavelplakhotny_c196.database.dao.CourseDao;
import com.example.pavelplakhotny_c196.database.dao.TermDao;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.entity.Term;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Term.class, Course.class, Assessment.class},
        version = 4,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    private static AppDatabase instance;
    private static final int number_of_threads = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(number_of_threads);


    public static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "App_Database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }


}
