package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityEsperaDonacionBinding;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityEsperaDonacion extends AppCompatActivity {

    ActivityEsperaDonacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEsperaDonacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonReturnDonaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEsperaDonacion.this, GeneralViewActivity.class );

                startActivity(intent);
                finish();
            }
        });

//        //pantalla de espera
//        TimerTask timer = new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(ActivityEsperaDonacion.this, DonacionActivity.class );
//
//                startActivity(intent);
//                finish();
//            }
//        };
//
//        //tiempo de espera
//        Timer tiempo = new Timer();
//        tiempo.schedule(timer, 5000); //5 segundos
    }
}