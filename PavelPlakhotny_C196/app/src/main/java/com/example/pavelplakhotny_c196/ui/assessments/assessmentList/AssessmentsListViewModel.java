package com.example.pavelplakhotny_c196.ui.assessments.assessmentList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

import java.util.List;

public class AssessmentsListViewModel extends AndroidViewModel {

    private LiveData<List<Assessment>> mAssessments;
    private AppRepository repository;

    public AssessmentsListViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        mAssessments = repository.getAllAssessments();
    }


    public LiveData<List<Assessment>> getAssessmentList() {
        return mAssessments;
    }

    public void insert(Assessment assessment) {
        repository.insertAssessment(assessment);
    }

    public void delete(Assessment assessment) {
        repository.deleteAssessment(assessment);
    }

    public void update(Assessment assessment) {
        repository.updateAssessment(assessment);
    }
}
