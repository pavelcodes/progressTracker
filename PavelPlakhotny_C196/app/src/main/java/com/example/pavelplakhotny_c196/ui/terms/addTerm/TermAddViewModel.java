package com.example.pavelplakhotny_c196.ui.terms.addTerm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

public class TermAddViewModel extends AndroidViewModel {
    private AppRepository repository;

    public TermAddViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void saveTerm(Term term) {
        repository.insertTerm(term);
    }

    }

