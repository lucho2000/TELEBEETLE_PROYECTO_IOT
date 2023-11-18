package com.example.telebeetle.activities;

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
import java.util.Date;

public class QRDonarActivity extends AppCompatActivity {

    ActivityQrdonarBinding binding;

    FirebaseDatabase database;

    StorageReference storageReference;

    FirebaseAuth firebaseAuth;
    Donacion donacion;

    Uri urlImagen;

    String fecha;

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


        binding.imageView2.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });


        //hacia la vista de la pantalla de espera perosnalizada
        botonEnviar.setOnClickListener(view -> {

            Integer montoInt = Integer.parseInt(binding.editTextMonto.getText().toString() );

            //se deberia sacar la condicion del usuario
            String usuarioActualUID = firebaseAuth.getCurrentUser().getUid();

            database.getReference("usuarios").child(usuarioActualUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){

                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Usuario usuario1 = dataSnapshot.getValue(Usuario.class);
                            Log.d("msg-test", "Usuario llego con nombre: " + usuario1.getNombres() + "" +usuario1.getApellidos());

                            //revisando si es egresado
                            if (usuario1.getCondicion().equalsIgnoreCase("alumno") ){

                                if(!binding.editTextMonto.getText().toString().isEmpty() && !urlImagen.toString().isEmpty()){
                                    StorageReference carpetaFotosDonacionesRef = storageReference.child("Capturas Donaciones");
                                    StorageReference fotoRef = carpetaFotosDonacionesRef.child(new Date().toString());

                                    fotoRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isSuccessful());
                                            Uri uriDownload = uriTask.getResult();
                                            donacion = new Donacion();
                                            donacion.setMonto(montoInt.toString()); //poner monto a donar
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                fecha = LocalDate.now().toString();
                                                donacion.setFecha(fecha);
                                            }
                                            donacion.setImagenCaptura(uriDownload.toString());
                                            donacion.setAccepted(false);
                                            Intent intent = new Intent(QRDonarActivity.this, ScreenEsperaActivity.class);
                                            startActivity(intent);
                                            //intent.putExtra("fecha",fecha);
                                            database.getReference("donaciones").push().setValue(donacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(QRDonarActivity.this, "Donacion realizada exitosamente", Toast.LENGTH_SHORT).show();
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
                                } else {

                                    Toast.makeText(QRDonarActivity.this, "Debe ingresar el monto", Toast.LENGTH_SHORT).show();
                                }


                            } else { //egresado
                                if (montoInt >=100){


                                } else {
                                    Toast.makeText(QRDonarActivity.this, "el monto debe pasar los 100 soles si es egresado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






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
}