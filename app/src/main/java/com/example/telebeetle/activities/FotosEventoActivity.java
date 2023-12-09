package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityFotosEventoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FotosEventoActivity extends AppCompatActivity {

    ActivityFotosEventoBinding binding;
    DatabaseReference databaseReference;
    Evento evento;
    String nombreActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFotosEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        evento = (Evento) intent.getSerializableExtra("Evento");
        nombreActividad = intent.getStringExtra("nombreActividad");
        binding.chip.setText(nombreActividad);
        binding.textView6.setText(evento.getEtapa());
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

    @Override
    public void onResume(){
        super.onResume();
        GridView gridView = findViewById(R.id.gridView);
        List<String> imageUrls = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("evento");
        databaseReference.child(evento.getUidEvento()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Evento evento1 = snapshot.getValue(Evento.class);
                    Set<String> keys = evento1.getRutasFotosEventos().keySet();
                    for (String key : keys) {
                        if(!key.equalsIgnoreCase("ga")){
                            imageUrls.add(evento1.getRutasFotosEventos().get(key));
                        }
                    }
                    ImageAdapter imageAdapter = new ImageAdapter(FotosEventoActivity.this, imageUrls);
                    gridView.setAdapter(imageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}