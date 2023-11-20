package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetailDonationBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DetailDonationActivity extends AppCompatActivity {


    FirebaseDatabase db = FirebaseDatabase.getInstance();

    ActivityDetailDonationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        Donacion donacion = (Donacion) intent.getSerializableExtra("donacion");

        binding.textView47.setText(donacion.getMonto());

        Picasso.get().load(donacion.getImagenCaptura()).into(binding.imageView9);



        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.button5.setOnClickListener(view -> {

            DatabaseReference myRef = db.getReference("donaciones/"+donacion.getKeyDonacion()+"/accepted");
            myRef.setValue(false);
            Toast.makeText(DetailDonationActivity.this,"La donacion no ha sido aceptada",Toast.LENGTH_SHORT).show();

        });
        binding.button3.setOnClickListener(view -> {

            DatabaseReference myRef = db.getReference("donaciones/"+donacion.getKeyDonacion()+"/accepted");
            myRef.setValue(true);
            Toast.makeText(DetailDonationActivity.this,"La donacion ha sido aceptada",Toast.LENGTH_SHORT).show();

        });




    }
}