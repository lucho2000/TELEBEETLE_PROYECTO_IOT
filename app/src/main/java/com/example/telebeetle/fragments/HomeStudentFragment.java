package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;



public class HomeStudentFragment extends Fragment {

    FragmentHomeStudentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeStudentBinding.inflate(inflater,container,false);












        return binding.getRoot();
    }
}