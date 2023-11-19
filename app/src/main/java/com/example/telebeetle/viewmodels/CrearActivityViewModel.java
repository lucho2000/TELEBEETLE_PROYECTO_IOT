package com.example.telebeetle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrearActivityViewModel extends ViewModel {
    private final MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<String> getUid(){
        return uid;
    }
}
