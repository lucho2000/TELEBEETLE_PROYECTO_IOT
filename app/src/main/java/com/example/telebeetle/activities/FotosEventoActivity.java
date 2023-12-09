package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityFotosEventoBinding;

public class FotosEventoActivity extends AppCompatActivity {

    ActivityFotosEventoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFotosEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("Evento");

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(FotosEventoActivity.this, SubirFotosActivity.class);
            intent1.putExtra("Evento", evento);
            startActivity(intent1);
        });

        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}