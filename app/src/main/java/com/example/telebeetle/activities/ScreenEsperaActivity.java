package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.telebeetle.R;

import java.util.Timer;
import java.util.TimerTask;

public class ScreenEsperaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_espera);

        //pantalla de espera
        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ScreenEsperaActivity.this, ActivityEsperaDonacion.class );

                startActivity(intent);
                finish();
            }
        };

        //tiempo de espera
        Timer tiempo = new Timer();
        tiempo.schedule(timer, 5000); //5 segundos
    }
}