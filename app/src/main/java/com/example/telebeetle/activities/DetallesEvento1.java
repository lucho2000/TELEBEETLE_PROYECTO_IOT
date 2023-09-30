package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetallesEvento1Binding;
import com.example.telebeetle.fragments.OpcionesApoyar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class DetallesEvento1 extends AppCompatActivity {
    ActivityDetallesEvento1Binding binding;
    private Double latitudFinal = -12.066553051720968;
    private Double longitudFinal = -77.08034059751783;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesEvento1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView arrow = findViewById(R.id.left_arrow);
        arrow.setOnClickListener(view -> {
            this.finish();
        });

        binding.Apoyar.setOnClickListener(view -> {
            getSupportFragmentManager().
                    beginTransaction().
                    setReorderingAllowed(true).
                    add(R.id.fragmentContainerView, OpcionesApoyar.class, null).commit();
        });

        binding.goMapa.setOnClickListener(view -> {
            mostrarUbicacion();
        });

    }
    public void deleteFragment(Fragment fragmentToDelete) {
        // Use FragmentManager to remove the specified fragment
        getSupportFragmentManager().beginTransaction().remove(fragmentToDelete).commit();
    }
    public void mostrarUbicacion() {

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
                    intent.putExtra("latitudFinal", latitudFinal);
                    intent.putExtra("longitudFinal", longitudFinal);
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
                    mostrarUbicacion();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    Log.d("msg", "Permiso de ubicación aproximada concedido");
                } else {
                    Log.d("msg", "Ningún permiso concedido");
                }
            }
    );

}