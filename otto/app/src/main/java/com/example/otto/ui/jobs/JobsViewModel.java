package com.example.otto.ui.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JobsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is jobs fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}