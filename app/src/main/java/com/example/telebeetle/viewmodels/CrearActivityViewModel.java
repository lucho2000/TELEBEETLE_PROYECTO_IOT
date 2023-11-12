package com.example.telebeetle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrearActivityViewModel extends ViewModel {
    private final MutableLiveData<String> codigo = new MutableLiveData<>();
    public MutableLiveData<String> getCodigo(){
        return codigo;
    }
}
