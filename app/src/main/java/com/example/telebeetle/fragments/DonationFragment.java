package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.DonacionesAdapter;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.QRDonarActivity;
import com.example.telebeetle.databinding.FragmentDonationBinding;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.List;


public class DonationFragment extends Fragment {

    FragmentDonationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDonationBinding.inflate(inflater,container,false);

        cargarlistaDonaciones();


        binding.buttonDonar.setOnClickListener(view -> {

            Intent intent = new Intent(container.getContext(), QRDonarActivity.class);
            startActivity(intent);

        });


        return binding.getRoot();
    }


    public List<Donacion> listarDonacionesHardcoded (){

        List<Donacion> listDona  = new ArrayList<>();

        Donacion dona1 = new Donacion("Donacion para la AITEL","10 de Octubre, 2023","15:00");
        Donacion dona2 = new Donacion("Donacion para la AITEL","11 de Octubre, 2023","14:00");
        Donacion dona3 = new Donacion("Donacion para la AITEL","12 de Octubre, 2023","16:00");
        Donacion dona4 = new Donacion("Donacion para la AITEL","13 de Octubre, 2023","18:00");

        listDona.add(dona1);
        listDona.add(dona2);
        listDona.add(dona3);
        listDona.add(dona4);

        return listDona;

    }

    public void cargarlistaDonaciones(){
        List<Donacion> listaDonaciones = listarDonacionesHardcoded();
        DonacionesAdapter donacionesAdapter = new DonacionesAdapter();
        donacionesAdapter.setListDonaciones(listaDonaciones);
        donacionesAdapter.setContext(getActivity().getApplicationContext());
        binding.rvDonaciones.setAdapter(donacionesAdapter);
        binding.rvDonaciones.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

}