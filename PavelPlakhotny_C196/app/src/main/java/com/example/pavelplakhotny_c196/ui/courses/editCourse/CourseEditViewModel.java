package com.example.pavelplakhotny_c196.ui.courses.editCourse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

import java.util.List;

public class CourseEditViewModel extends AndroidViewModel {
    private LiveData<List<Assessment>> assessmentDialogList;
    private AppRepository repository;


    public CourseEditViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        assessmentDialogList = repository.getUnassignedAssessments();
    }


    public LiveData<List<Assessment>> getAssessmentListForCurrentCourse(Integer courseId) {
        LiveData<List<Assessment>> mAssessmentList = repository.getAssessmentListForCurrentCourse(courseId);
        return mAssessmentList;
    }

    public void update(Course course) {
        repository.updateCourse(course);
    }

    public void update(Assessment assessment) {
        repository.updateAssessment(assessment);
    }

    public void addAssessmentToCourse(Integer id, String title) {
        repository.addAssessmentToCourse(id, title);
    }

    public void unlinkAssessmentFromCourse(Integer courseIdNull, int id) {
        repository.unlinkAssessmentFromCourse(courseIdNull, id);
    }

    public LiveData<List<Assessment>> getUnassignedAssessments() {
        return assessmentDialogList;
    }
}

