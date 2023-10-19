package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityVerApoyosBinding;

public class verApoyosActivity extends AppCompatActivity {

    ActivityVerApoyosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerApoyosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }


}