package com.example.pavelplakhotny_c196.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pavelplakhotny_c196.database.repository.AppRepository;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Progress Tracker");
    }

    public LiveData<String> getText() {
        return mText;
    }


}