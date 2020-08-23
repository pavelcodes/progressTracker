package com.example.pavelplakhotny_c196.ui.courses.addCourse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

public class CourseAddViewModel extends AndroidViewModel {

    private AppRepository repository;

    public CourseAddViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void saveCourse(Course course) {
        repository.insertCourse(course);
    }


}
