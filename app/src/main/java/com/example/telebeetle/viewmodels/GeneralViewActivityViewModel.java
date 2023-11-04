package com.example.telebeetle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.telebeetle.Entity.Usuario;

public class GeneralViewActivityViewModel extends ViewModel {

    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    public MutableLiveData<Usuario> getUsuario() {
        return usuario;
    }


}
