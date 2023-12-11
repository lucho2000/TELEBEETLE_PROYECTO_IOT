package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityQrdonarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QRDonarActivity extends AppCompatActivity {

    ActivityQrdonarBinding binding;

    FirebaseDatabase database;

    StorageReference storageReference;

    FirebaseAuth firebaseAuth;
    Donacion donacion;

    Uri urlImagen;

    LocalDate fecha;

    Usuario usuario1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrdonarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        Button botonEnviar = findViewById(R.id.buttonSubirImagen);

        ActivityResultLauncher<PickVisualMediaRequest> launcher  =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), urlImagen -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (urlImagen != null) {
                        Log.d("PhotoPicker", "Selected URI: " + urlImagen);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }

                });
        binding.imageView2.setOnClickListener(v -> {

            launcher.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });


        //hacia la vista de la pantalla de espera perosnalizada
        botonEnviar.setOnClickListener(view -> {

            Integer montoInt;

            try {
                montoInt = Integer.parseInt(binding.editTextMonto.getText().toString() );
            } catch (Exception e){
                montoInt = 0;

            }

            //se deberia sacar la condicion del usuario
            String usuarioActualUID = firebaseAuth.getCurrentUser().getUid();
            Log.d("msg-test", "ID USER: " +usuarioActualUID);

            if( montoInt!=null && urlImagen!=null){

                Integer finalMontoInt = montoInt;
                database.getReference("usuarios").child(usuarioActualUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){

                                String condicion = snapshot.child("condicion").getValue(String.class);
                                String nombre = snapshot.child("nombres").getValue(String.class);
                                String apellidos = snapshot.child("apellidos").getValue(String.class);

                                String donante = nombre + " " + apellidos;
                                //Usuario usuario1 = dataSnapshot.getValue(Usuario.class);
                                Log.d("msg-test", "Usuario llego con condicion: " + condicion);

                                //revisando el rol
                                if (condicion.equalsIgnoreCase("alumno") && finalMontoInt > 0){

                                    crearDonaciones(finalMontoInt, usuarioActualUID);

                                } else { //egresado
                                    if (finalMontoInt > 100){
                                        crearDonaciones(finalMontoInt, usuarioActualUID);
                                    } else {
                                        Toast.makeText(QRDonarActivity.this, "El monto debe ser mayor a 100 soles para los egresados", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            } else {
                Toast.makeText(QRDonarActivity.this, "Debe escribir el monto o adjuntar la captura de la donación ", Toast.LENGTH_SHORT).show();
            }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            // La imagen seleccionada se encuentra en data.getData()
            urlImagen = data.getData();
//            Picasso.get().load(urlImagen).into(previsualizaImagen);
//            previsualizaImagen.setImageURI(urlImagen);
            //Intent intent  = new Intent(CrearActivity.this, EscogerDelegadoActivity.class);
            //intent.putExtra("uri_extra", path);
            Log.d("msg-test", "Selected URI: " + urlImagen);
            Log.d("msg-test", "uri.getLastPathSegment(): " + urlImagen.getLastPathSegment());

        }
    }

    public void crearDonaciones(int montoInt, String uidDonante){
        StorageReference carpetaFotosDonacionesRef = storageReference.child("Capturas Donaciones");
        StorageReference fotoRef = carpetaFotosDonacionesRef.child(new Date().toString());

        fotoRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri uriDownload = uriTask.getResult();
                donacion = new Donacion();
                donacion.setMonto(String.valueOf(montoInt)); //poner monto a donar
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    fecha = LocalDate.now();
                    // Definir un formato personalizado con barras
                    DateTimeFormatter formatoConBarras = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    // Formatear la fecha con el formato personalizado
                    String fechaFormateadaConBarras = fecha.format(formatoConBarras);

                    donacion.setFecha(fechaFormateadaConBarras);
                }
                donacion.setImagenCaptura(uriDownload.toString());
                donacion.setAccepted(false);
                donacion.setUidDonante(uidDonante);
                Intent intent = new Intent(QRDonarActivity.this, ActivityEsperaDonacion.class);
                startActivity(intent);
                //intent.putExtra("fecha",fecha);
                database.getReference("donaciones_por_validar").push().setValue(donacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(QRDonarActivity.this, "Donacion realizada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mgs-test", e.toString());
                    }
                });

            }
        });

    }
}