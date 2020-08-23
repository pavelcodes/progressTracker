package com.example.pavelplakhotny_c196.ui.courses.courseList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

import java.util.List;

public class CourseListViewModel extends AndroidViewModel {

    private LiveData<List<Course>> mCourse;
    private AppRepository repository;

    public CourseListViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        mCourse = repository.getAllCourses();
    }

    public LiveData<List<Course>> getCourseList() {
        return mCourse;
    }

    public void insert(Course course) {
        repository.insertCourse(course);
    }

    public void delete(Course course) {
        repository.deleteCourse(course);
    }

    public void update(Course course) {
        repository.updateCourse(course);
    }
}