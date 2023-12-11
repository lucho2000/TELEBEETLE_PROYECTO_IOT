package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetailDonation2Binding;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DetailDonationActivity2 extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    ActivityDetailDonation2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailDonation2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Donacion donacion = (Donacion) intent.getSerializableExtra("donacion");

        binding.textView47.setText("S./ " + donacion.getMonto());

        Picasso.get().load(donacion.getImagenCaptura()).into(binding.imageView9);


        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}