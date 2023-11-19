package com.example.telebeetle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public MutableLiveData<String> getSearchQuery() {
        return searchQuery;
    }
}
