package com.example.otto.ui.billing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BillingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BillingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is billing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}