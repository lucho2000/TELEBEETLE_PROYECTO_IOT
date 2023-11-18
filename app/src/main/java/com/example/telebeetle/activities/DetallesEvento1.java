package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetallesEvento1Binding;
import com.example.telebeetle.fragments.OpcionesApoyar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetallesEvento1 extends AppCompatActivity {
    ActivityDetallesEvento1Binding binding;
    private Double latitudFinal = -12.066553051720968;
    private Double longitudFinal = -77.08034059751783;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesEvento1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("Evento");
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

        binding.Apoyar.setOnClickListener(view -> {
            Bundle bundleConEventoUid = new Bundle();
            bundleConEventoUid.putString("evento_uid", evento.getUidEvento());
            Log.d("msg_test","que mrd ");
            getSupportFragmentManager().
                    beginTransaction().
                    setReorderingAllowed(true).
                    add(R.id.fragmentContainerView, OpcionesApoyar.class, bundleConEventoUid).commit();
            /*getSupportFragmentManager().
                    beginTransaction().
                    setReorderingAllowed(true).
                    add(R.id.fragmentContainerView, OpcionesApoyar.class, null).commit();*/
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
    public void deleteFragment(Fragment fragmentToDelete) {
        // Use FragmentManager to remove the specified fragment
        getSupportFragmentManager().beginTransaction().remove(fragmentToDelete).commit();
    }
    public void mostrarUbicacion(String latitudLugar, String longitudLugar,String lugar) {

        int selfPermissionFineLocation = ActivityCompat.checkSelfPermission(DetallesEvento1.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int selfPermissionCoarseLocation = ActivityCompat.checkSelfPermission(DetallesEvento1.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (selfPermissionFineLocation == PackageManager.PERMISSION_GRANTED &&
                selfPermissionCoarseLocation == PackageManager.PERMISSION_GRANTED) {
            Log.d("msg-test", "LLEGUE AQUI DBNGFSKJBDGJDS");
            //tenemos permisos
            FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(DetallesEvento1.this);
            providerClient.getLastLocation().addOnSuccessListener(DetallesEvento1.this, location -> {
                if(location != null){
                    Log.d("msg-test", "GAAAAAAAAAAAAAAAAAAA");
                    Intent intent = new Intent(DetallesEvento1.this, MapsActivity.class);
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