package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityFinalizadoEventoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FinalizadoEventoActivity extends AppCompatActivity {

    ActivityFinalizadoEventoBinding binding;
    DatabaseReference databaseReference;
    String nombreActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalizadoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("Evento");
        //binding.nombre.setText(evento.getDelegadoActividadAsignado());
        binding.hora.setText(evento.getHora());
        binding.fecha.setText(evento.getFecha());
        binding.textView6.setText(evento.getEtapa());
        binding.lugar.setText(evento.getLugar());
        binding.textView12.setText(evento.getDescripcion());

        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
        databaseReference.child(evento.getActividad()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Actividad actividad = snapshot.getValue(Actividad.class);
                    binding.chip.setText(actividad.getNombreActividad());
                    nombreActividad = actividad.getNombreActividad();
                    databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "usuarios"
                    databaseReference.child(actividad.getDelegado()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if(snapshot2.exists()){
                                Usuario usuario = snapshot2.getValue(Usuario.class);
                                binding.nombre.setText(usuario.getNombres() + " " + usuario.getApellidos());
                                ImageView imageView = binding.fotoperfil;
                                Picasso.get().load(usuario.getProfile())
                                        .resize(75,75)
                                        .transform(new CropCircleTransformation())
                                        .into(imageView);
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
        binding.buttonFotos.setOnClickListener(v -> {
            Intent intent1 = new Intent(FinalizadoEventoActivity.this, FotosEventoActivity.class);
            intent1.putExtra("Evento", evento);
            intent1.putExtra("nombreActividad", nombreActividad);
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