package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetalleActividadGeneralBinding;
import com.example.telebeetle.databinding.ActivityDetallesEvento1Binding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetalleActividadGeneralActivity extends AppCompatActivity {

    ActivityDetalleActividadGeneralBinding binding;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleActividadGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent1 = getIntent();
        Evento evento = (Evento) intent1.getSerializableExtra("Evento");
        //binding.nombre.setText(evento.getDelegadoActividadAsignado());
        binding.chip.setText(evento.getActividad());
        binding.hora.setText(evento.getHora());
        binding.fecha.setText(evento.getFecha());
        binding.textView6.setText(evento.getEtapa());
        binding.lugar.setText(evento.getLugar());
        binding.textView12.setText(evento.getDescripcion());
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageView = binding.fotoperfil;
        picasso.load(drawableResourceId)
                .resize(75,75)
                .transform(new CropCircleTransformation())
                .into(imageView);
        Button verApoyos = findViewById(R.id.verApoyos);
        verApoyos.setOnClickListener(view -> {
            Intent intent = new Intent(DetalleActividadGeneralActivity.this, verApoyosActivity.class);
            startActivity(intent);
        });
        Button verSolicitudes = findViewById(R.id.verSolicitudes);
        verSolicitudes.setOnClickListener(view -> {
            showSheetSolicitudes();
        });
        binding.goMapa.setOnClickListener(view -> {
            mostrarUbicacion(evento.getLatitud(), evento.getLongitud(), evento.getLugar());
        });
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
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
    public void mostrarUbicacion(String latitudLugar, String longitudLugar,String lugar) {

        int selfPermissionFineLocation = ActivityCompat.checkSelfPermission(DetalleActividadGeneralActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int selfPermissionCoarseLocation = ActivityCompat.checkSelfPermission(DetalleActividadGeneralActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (selfPermissionFineLocation == PackageManager.PERMISSION_GRANTED &&
                selfPermissionCoarseLocation == PackageManager.PERMISSION_GRANTED) {
            Log.d("msg-test", "LLEGUE AQUI DBNGFSKJBDGJDS");
            //tenemos permisos
            FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(DetalleActividadGeneralActivity.this);
            providerClient.getLastLocation().addOnSuccessListener(DetalleActividadGeneralActivity.this, location -> {
                if(location != null){
                    Log.d("msg-test", "GAAAAAAAAAAAAAAAAAAA");
                    Intent intent = new Intent(DetalleActividadGeneralActivity.this, MapsActivity.class);
                    intent.putExtra("latitud", location.getLatitude());
                    intent.putExtra("longitud", location.getLongitude());
                    intent.putExtra("latitudFinal", Double.parseDouble(latitudLugar));
                    intent.putExtra("longitudFinal", Double.parseDouble(longitudLugar));
                    intent.putExtra("lugar", lugar);
                    startActivity(intent);
                }else {
                    Log.d("msg-test", "IIIIIIIIIIIIIIIIIIIII");
                }
            });
        } else {
            //no tenemos permisos, se deben solicitar
            Log.d("msg-test", "LLEGUE nooooooooooooooooo");
            locationPermissionLauncher.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

    }
    ActivityResultLauncher<String[]> locationPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean fineLocationGranted = result.get(android.Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean coarseLocationGranted = result.get(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                if (fineLocationGranted != null && fineLocationGranted) {
                    Log.d("msg", "Permiso de ubicación precisa concedido");
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    Log.d("msg", "Permiso de ubicación aproximada concedido");
                } else {
                    Log.d("msg", "Ningún permiso concedido");
                }
            }
    );
}