package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.telebeetle.databinding.ActivityIntermedioBinding;

public class Intermedio extends AppCompatActivity {
    ActivityIntermedioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntermedioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}