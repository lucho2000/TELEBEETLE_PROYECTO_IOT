package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.telebeetle.databinding.ActivityWaitBinding;

public class WaitActivity extends AppCompatActivity {

    ActivityWaitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.buttonWaitGoogle.setOnClickListener(v -> {
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(WaitActivity.this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WaitActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}