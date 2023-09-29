package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.FragmentDonationBinding;


public class DonationFragment extends Fragment {

    FragmentDonationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDonationBinding.inflate(inflater,container,false);



        return binding.getRoot();
    }
}