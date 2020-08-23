package com.example.pavelplakhotny_c196.ui.assessments.assessmentAdd;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

public class AssessmentAddViewModel extends AndroidViewModel {

    private AppRepository repository;

    public AssessmentAddViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void saveAssessment(Assessment assessment) {
        repository.insertAssessment(assessment);
    }


}




