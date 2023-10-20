package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Activity;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.ActivityAdapter;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.databinding.FragmentHomeDelegadoGeneralBinding;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;
import com.google.android.material.carousel.CarouselLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class HomeDelegadoGeneralFragment extends Fragment {

    FragmentHomeDelegadoGeneralBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeDelegadoGeneralBinding.inflate(inflater,container,false);
        cargarListaActividades();
        return binding.getRoot();
    }

    public List<Activity> listaActividadesHarcoede(){
       List<Activity>  activityList = new ArrayList<>();
       Activity activity1 = new Activity("Voley mixto", "Willy Huallpa");
       activityList.add(activity1);
       Activity activity2 = new Activity("Futsal varones", "Rommel Garay");
       activityList.add(activity2);
       Activity activity3 = new Activity("Barras teleenchufe", "Leonardo Abanto");
       activityList.add(activity3);
       return  activityList;
    }

    public void cargarListaActividades(){
        List<Activity> listaActivity = listaActividadesHarcoede();

        ActivityAdapter activityAdapter = new ActivityAdapter();
        activityAdapter.setListActivities(listaActivity);
        activityAdapter.setContext(getActivity().getApplicationContext());

        binding.rvActividades.setAdapter(activityAdapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));
    }
}