package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WaitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        Button button = findViewById(R.id.button5);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        button.setOnClickListener(v -> {
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