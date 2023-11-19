package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.telebeetle.Entity.Apoyo;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityVerApoyosBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class verApoyosActivity extends AppCompatActivity {

    ActivityVerApoyosBinding binding;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerApoyosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cargarApoyos();

        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void cargarApoyos(){

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        List<Usuario> listaUsuarios = new ArrayList<>();

        ApoyoAdapter apoyoAdapter = new ApoyoAdapter();
        apoyoAdapter.setListApoyo(listaUsuarios);
        apoyoAdapter.setContext(verApoyosActivity.this);


        binding.recyclerView2.setAdapter(apoyoAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(verApoyosActivity.this));


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    listaUsuarios.add(usuario);
                }
                apoyoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}