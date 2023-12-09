package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityFinalizarEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalizarEventoActivity extends AppCompatActivity {

    TextInputLayout actividadInput, eventoInput;
    EditText nombreActividad,  descripcion;
    Evento eventoGo = new Evento();
    Actividad actividadGo = new Actividad();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference;
    ActivityFinalizarEventoBinding binding;
    ArrayList<String> eventos = new ArrayList<>();
    ArrayList<String> uidEventos = new ArrayList<>();
    String actividadStr, eventoStr, descripcionStr;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalizarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actividadInput = binding.actividad;
        eventoInput = binding.EventoTextField;
        nombreActividad = binding.NombreActividadTextField;
        descripcion = binding.editTextComentario;
        AutoCompleteTextView autoCompleteTextView = binding.nombreEvento;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FinalizarEventoActivity.this, android.R.layout.simple_dropdown_item_1line, eventos);
        autoCompleteTextView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Actividad actividad1 = dataSnapshot.getValue(Actividad.class);
                    if(actividad1.getDelegado().equals(firebaseAuth.getCurrentUser().getUid())){
                        actividadGo = actividad1;
                        actividadGo.setUidActividad(dataSnapshot.getKey());
                        nombreActividad.setText(actividadGo.getNombreActividad());
                        nombreActividad.setEnabled(false);
                        break;
                    }
                    databaseReference = FirebaseDatabase.getInstance().getReference("evento");
                    eventos.clear();
                    uidEventos.clear();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                Evento evento1 = dataSnapshot1.getValue(Evento.class);
                                if(actividadGo.getUidActividad().equals(evento1.getActividad())){
                                    eventos.add(evento1.getEtapa());
                                    uidEventos.add(dataSnapshot1.getKey());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.botonFinalizarEvento.setOnClickListener(v -> {
            actividadStr = binding.NombreActividadTextField.getText().toString();
            eventoStr = autoCompleteTextView.getText().toString();
            descripcionStr = descripcion.getText().toString();
            if (actividadStr != null && eventoStr != null && descripcionStr != null && !actividadStr.isEmpty() && !eventoStr.isEmpty() && !descripcionStr.isEmpty()) {
                DatabaseReference eventosData = database.getReference("evento");

                HashMap<String, Object> eventoUpdate = new HashMap<>();
                eventoUpdate.put("descripcion", descripcionStr);
                eventoUpdate.put("estado", "Finalizado");
                String uid = new String();
                for(int i=0; i<eventos.size(); i++){
                    if(eventoStr.equalsIgnoreCase(eventos.get(i))){
                        uid = uidEventos.get(i);
                        break;
                    }
                }

                eventosData.child(uid).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            descripcion.setText("");
                            Toast.makeText(FinalizarEventoActivity.this,"Evento finalizado con exito",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FinalizarEventoActivity.this,"Error al finalizar evento",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
            }
        });
        binding.botonCancelarFinalizarEvento.setOnClickListener(view -> {
            finish();
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