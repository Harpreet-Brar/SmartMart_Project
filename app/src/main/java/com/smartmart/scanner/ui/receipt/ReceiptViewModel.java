package com.smartmart.scanner.ui.receipt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ReceiptViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReceiptViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is receipt fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}