package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityAfterGoogleBinding;
import com.example.telebeetle.databinding.ActivityWaitBinding;

public class WaitActivity extends AppCompatActivity {

    ActivityWaitBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonWaitGoogle.setOnClickListener(view -> {

            Intent intent = new Intent(WaitActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
}