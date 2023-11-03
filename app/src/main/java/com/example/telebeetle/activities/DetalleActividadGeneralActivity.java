package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetalleActividadGeneralActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad_general);
        Button verApoyos = findViewById(R.id.verApoyos);
        verApoyos.setOnClickListener(view -> {
            Intent intent = new Intent(DetalleActividadGeneralActivity.this, verApoyosActivity.class);
            startActivity(intent);
        });
        Button verSolicitudes = findViewById(R.id.verSolicitudes);
        verSolicitudes.setOnClickListener(view -> {
            showSheetSolicitudes();
        });

    }
    private void showSheetSolicitudes(){
        final Dialog dialog = new Dialog(DetalleActividadGeneralActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.solicitudes_equipo_bottom_sheet_layout);
        ImageView cancelButton2 = dialog.findViewById(R.id.cancelButton2);
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        List<Usuario> listaUsuarios = new ArrayList<>();

        SolicitudesRegistroAdapter solicitudesRegistroAdapter = new SolicitudesRegistroAdapter();
        solicitudesRegistroAdapter.setUsuarioList(listaUsuarios);
        solicitudesRegistroAdapter.setContext(DetalleActividadGeneralActivity.this);

        RecyclerView solicitudes = dialog.findViewById(R.id.rv_solicitudesRegistro);
        solicitudes.setAdapter(solicitudesRegistroAdapter);
        solicitudes.setLayoutManager(new LinearLayoutManager(DetalleActividadGeneralActivity.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    listaUsuarios.add(usuario);
                }
                solicitudesRegistroAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomMenuDialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}