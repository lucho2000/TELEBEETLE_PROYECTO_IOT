package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.telebeetle.R;

public class DonacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);

        Button botonDonar = findViewById(R.id.buttonDonar);

        //hacia la vista del QR
        botonDonar.setOnClickListener(view -> {

            Intent intent = new Intent(DonacionActivity.this, QRDonarActivity.class);
            startActivity(intent);
        });
    }
}