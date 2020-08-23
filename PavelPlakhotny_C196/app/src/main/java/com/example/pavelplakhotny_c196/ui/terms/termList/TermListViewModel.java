package com.example.pavelplakhotny_c196.ui.terms.termList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.AppDatabase;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;
import com.example.pavelplakhotny_c196.databinding.FragmentEditTermBinding;

import java.util.List;

public class TermListViewModel extends AndroidViewModel {
    private LiveData<List<Term>> mTerms;
    private AppRepository repository;
    private FragmentEditTermBinding binding;

    public TermListViewModel(@NonNull Application application) {
    super(application);
    repository = AppRepository.getInstance(application.getApplicationContext());
    mTerms = repository.getAllTerms();

    }

    public void getTermById(int termId) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Term term= repository.getTermForId(termId);
            }
        });
    }


    public LiveData<List<Term>> getTermList() {
                return mTerms;
    }

    public void insert(Term term)
    {
        repository.insertTerm(term);
    }

    public void delete(Term term)
    {
        repository.deleteTerm(term);
    }

    public int checkIfCoursesAssignedToTerm(Integer id) {
        int count = repository.checkIfCoursesAssignedToTerm(id);
        return count;
    }

    public void update(Term term)
    {
        repository.updateTerm(term);
    }



    public void getCoursesByTerm(int termId) {repository.getCourseListForTerm(termId);}

}