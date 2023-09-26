package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.telebeetle.databinding.ActivityDetallesEvento1Binding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.fragments.OpcionesApoyar;

public class DetallesEvento1 extends AppCompatActivity {
    ActivityDetallesEvento1Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesEvento1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView arrow = findViewById(R.id.left_arrow);
        arrow.setOnClickListener(view -> {
            this.finish();
        });

        binding.Apoyar.setOnClickListener(view -> {
            getSupportFragmentManager().
                    beginTransaction().
                    setReorderingAllowed(true).
                    addToBackStack(null).
                    add(R.id.fragmentContainerView, OpcionesApoyar.class, null).commit();
        });

    }
}