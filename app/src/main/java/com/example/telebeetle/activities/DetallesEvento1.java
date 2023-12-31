package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetallesEvento1Binding;
import com.example.telebeetle.fragments.DialogParticipante;
import com.example.telebeetle.fragments.EsperaParticipante;
import com.example.telebeetle.fragments.OpcionApoyando;
import com.example.telebeetle.fragments.OpcionApoyando2;
import com.example.telebeetle.fragments.OpcionesApoyar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetallesEvento1 extends AppCompatActivity implements OpcionApoyando.OnFragmentInteractionListener1, OpcionApoyando2.OnFragmentInteractionListener2, EsperaParticipante.OnFragmentInteractionListener3 {
    ActivityDetallesEvento1Binding binding;
    private Double latitudFinal = -12.066553051720968;
    private Double longitudFinal = -77.08034059751783;
    DatabaseReference databaseReference, databaseReference2;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesEvento1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("Evento");
        //binding.nombre.setText(evento.getDelegadoActividadAsignado());
        binding.hora.setText(evento.getHora());
        binding.fecha.setText(evento.getFecha());
        binding.textView6.setText(evento.getEtapa());
        binding.lugar.setText(evento.getLugar());
        binding.textView12.setText(evento.getDescripcion());
        firebaseAuth = FirebaseAuth.getInstance();
        String UIDusuarioActual = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
        databaseReference2 = FirebaseDatabase.getInstance().getReference("evento");
        databaseReference.child(evento.getActividad()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Actividad actividad = snapshot.getValue(Actividad.class);
                    binding.chip.setText(actividad.getNombreActividad());
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
        Set<String> keys = evento.getListaApoyosBarras().keySet();
        for (String key : keys) {
            if(!key.equalsIgnoreCase("ga") && evento.getListaApoyosBarras().get(key).equals(UIDusuarioActual)){ //validando
                //binding.Apoyar.setEnabled(false);
                Bundle bundle = new Bundle();
                bundle.putString("evento_uid", evento.getUidEvento());
                bundle.putSerializable("listaApoyosBarras", evento.getListaApoyosBarras());
                //binding.textView13.setVisibility(View.GONE);
                //binding.textView15.setVisibility(View.GONE);
                binding.Apoyar.setVisibility(View.GONE);


                OpcionApoyando opcionApoyandoFragment = new OpcionApoyando();
                opcionApoyandoFragment.setOnFragmentInteractionListener((OpcionApoyando.OnFragmentInteractionListener1) this);
                opcionApoyandoFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmentContainerView, opcionApoyandoFragment, "opcionApoyandoTag")
                        .commit();


                /*getSupportFragmentManager().
                        beginTransaction().
                        setReorderingAllowed(true).
                        add(R.id.fragmentContainerView, OpcionApoyando.class, bundle).commit();*/
                break;
            }
        }
        Set<String> keys2 = evento.getListaApoyosParticipantes().keySet();
        for (String key : keys2) {
            if(!key.equalsIgnoreCase("ga") && evento.getListaApoyosParticipantes().get(key).equals(UIDusuarioActual)){ //validando
                //binding.Apoyar.setEnabled(false);
                Bundle bundle = new Bundle();
                bundle.putString("evento_uid", evento.getUidEvento());
                bundle.putSerializable("listaNoValidados", evento.getListaApoyosParticipantes());
                //binding.textView13.setVisibility(View.GONE);
                //binding.textView15.setVisibility(View.GONE);
                binding.Apoyar.setVisibility(View.GONE);

                EsperaParticipante esperaParticipante = new EsperaParticipante();
                esperaParticipante.setOnFragmentInteractionListener((EsperaParticipante.OnFragmentInteractionListener3) this);
                esperaParticipante.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmentContainerView, esperaParticipante, "esperaParticipanteTag")
                        .commit();

                /*getSupportFragmentManager().
                        beginTransaction().
                        setReorderingAllowed(true).
                        add(R.id.fragmentContainerView, EsperaParticipante.class, bundle).commit();*/
                break;
            }
        }
        Set<String> keys3 = evento.getListaApoyosParticipantesValidados().keySet();
        for (String key : keys3) {
            if(!key.equalsIgnoreCase("ga") && evento.getListaApoyosParticipantesValidados().get(key).equals(UIDusuarioActual)){ //validando
                //binding.Apoyar.setEnabled(false);
                Bundle bundle = new Bundle();
                bundle.putString("evento_uid", evento.getUidEvento());
                bundle.putSerializable("listaValidados", evento.getListaApoyosParticipantesValidados());
                //binding.textView13.setVisibility(View.GONE);
                //binding.textView15.setVisibility(View.GONE);
                binding.Apoyar.setVisibility(View.GONE);

                OpcionApoyando2 opcionApoyando2 = new OpcionApoyando2();
                opcionApoyando2.setOnFragmentInteractionListener((OpcionApoyando2.OnFragmentInteractionListener2) this);
                opcionApoyando2.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmentContainerView, opcionApoyando2, "opcionApoyando2Tag")
                        .commit();


                /*getSupportFragmentManager().
                        beginTransaction().
                        setReorderingAllowed(true).
                        add(R.id.fragmentContainerView, OpcionApoyando2.class, bundle).commit();*/
                break;
            }
        }

        binding.Apoyar.setOnClickListener(view -> {
            /*binding.textView13.setVisibility(View.GONE);
            binding.textView15.setVisibility(View.GONE);
            binding.Apoyar.setVisibility(View.GONE);*/
            Bundle bundleConEventoUid = new Bundle();
            bundleConEventoUid.putString("evento_uid", evento.getUidEvento());
            bundleConEventoUid.putString("nroMaxParticipantes", evento.getNroMaxParticipante());
            bundleConEventoUid.putSerializable("listaApoyosBarras", evento.getListaApoyosBarras());
            bundleConEventoUid.putSerializable("listaApoyosParticipantes", evento.getListaApoyosParticipantesValidados());
            bundleConEventoUid.putSerializable("noValidadosParticipantes", evento.getListaApoyosParticipantes());
            Log.d("msg_test","que mrd ");
            getSupportFragmentManager().
                    beginTransaction().
                    setReorderingAllowed(true).
                    add(R.id.fragmentContainerView, OpcionesApoyar.class, bundleConEventoUid).commit();
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

    public void deleteFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainerView);

        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onTipo1ButtonPressed() {
        // Acciones específicas cuando el botón del FragmentTipo1 es presionado
        mostrarElementosOcultos();
    }

    @Override
    public void onTipo2ButtonPressed() {
        // Acciones específicas cuando el botón del FragmentTipo2 es presionado
        mostrarElementosOcultos();
    }

    @Override
    public void onTipo3ButtonPressed() {
        mostrarElementosOcultos();
        // Acciones específicas cuando el botón del FragmentTipo3 es presionado
    }

    private void mostrarElementosOcultos() {
        //binding.textView13.setVisibility(View.VISIBLE);
        //binding.textView15.setVisibility(View.VISIBLE);
        binding.Apoyar.setVisibility(View.VISIBLE);
        // Código para volver a mostrar los elementos ocultos
    }
}