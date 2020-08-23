package com.example.pavelplakhotny_c196.ui.terms.editTerm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.database.repository.AppRepository;

import java.util.List;

public class TermEditViewModel extends AndroidViewModel {
    private LiveData<List<Course>>  courseDialogList;
    private AppRepository repository;


    public TermEditViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        courseDialogList = repository.getUnassignedCourses();
    }

    public LiveData<List<Course>> getCourseListForTerm(Integer termId) {
        LiveData<List<Course>> mCourseList = repository.getCourseListForTerm(termId);
        return mCourseList;
    }

    public void update(Term term)
    {
        repository.updateTerm(term);
    }

    public void update(Course course)
    {
        repository.updateCourse(course);
    }

    public void addCourseToTerm(Integer id, String title){
        repository.addCourseToTerm(id,title);
    }


    public void unlinkCourseFromTerm(Integer termIdNull, int id)
    {
        repository.unlinkCourseFromTerm(termIdNull, id);
    }

    public LiveData<List<Course>>  getUnassignedCourses() {
        return courseDialogList;
    }
}