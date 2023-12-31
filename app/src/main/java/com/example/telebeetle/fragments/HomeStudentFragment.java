package com.example.telebeetle.fragments;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.EventsActivity;
import com.example.telebeetle.activities.MyPagerAdapter;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;
import com.example.telebeetle.viewmodels.SharedViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeStudentFragment extends Fragment {

    FragmentHomeStudentBinding binding;

    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeStudentBinding.inflate(inflater,container,false);
        searchView = binding.searchView;
        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.secondTab;
        MyPagerAdapter adapter = new MyPagerAdapter(requireActivity());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(adapter);
                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("En proceso");
                            break;
                        case 1:
                            tab.setText("Finalizado");
                            break;
                        case 2:
                            tab.setText("Mis eventos");
                            break;
                    }
                }).attach();
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        //Toast.makeText(getContext(), "Tab " + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                sharedViewModel.getSearchQuery().setValue(s);
                return true;
            }
        });
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}