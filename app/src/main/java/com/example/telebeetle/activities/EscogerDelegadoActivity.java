package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityEscogerDelegadoBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.dto.DelegadoDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EscogerDelegadoActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ActivityEscogerDelegadoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEscogerDelegadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        List<Usuario> listaDelegado = new ArrayList<>();

        DelegadoDto delegadoDto = new DelegadoDto();


        DelegadosAdapter delegadosAdapter1 = new DelegadosAdapter();
        delegadosAdapter1.setListaDelegados(listaDelegado);
        delegadosAdapter1.setContext(EscogerDelegadoActivity.this);


        binding.rvDelegadosActividad.setAdapter(delegadosAdapter1);
        binding.rvDelegadosActividad.setLayoutManager(new LinearLayoutManager(EscogerDelegadoActivity.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    listaDelegado.add(usuario);
                }
                delegadosAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}