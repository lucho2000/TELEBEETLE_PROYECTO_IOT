package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityCrearBinding;
import com.example.telebeetle.viewmodels.CrearActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CrearActivity extends AppCompatActivity {

    Button botonSiguiente, botonCancelar;

    ImageView previsualizaImagen;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    StorageReference storageReference;

    List<Usuario> listaDelegado;

    ActivityCrearBinding binding;

    FirebaseStorage storage;
    String codigoDelegado;

    Actividad actividad;

    Uri urlImagen;

    Usuario delegadoAdd;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CrearActivityViewModel crearActivityViewModel = new ViewModelProvider(CrearActivity.this).get(CrearActivityViewModel.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        TextInputEditText editTextActividad = findViewById(R.id.editTextActividad);

        AutoCompleteTextView  autoCompleteTextViewCategoria = findViewById(R.id.categoria);

        String[] opcionesMenu = {"Masculino", "Femenino", "Mixto"}; //opciones del menu

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad");

        listaDelegado = new ArrayList<>();

        DelegadosAdapter delegadosAdapter1 = new DelegadosAdapter();
        delegadosAdapter1.setListaDelegados(listaDelegado);
        delegadosAdapter1.setContext(CrearActivity.this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesMenu);

        autoCompleteTextViewCategoria.setAdapter(adapter); //el adapter llena el text view con las opciones

        ImageView iconoImagen  = findViewById(R.id.imageView7);
        botonSiguiente = findViewById(R.id.buttonSiguiente);
        botonCancelar = findViewById(R.id.buttonCancelar);
        previsualizaImagen = findViewById(R.id.imageViewPrevisualiza);

        binding.recyclerView3.setAdapter(delegadosAdapter1);
        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(CrearActivity.this));
        list = new ArrayList<>();

        //binding.recyclerView3.addItemDecoration(new DividerItemDecoration(CrearActivity.this, DividerItemDecoration.VERTICAL));

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Actividad actividadga = dataSnapshot.getValue(Actividad.class);
                        list.add(actividadga.getDelegado());
                    }
                    //llenar lista de delegados
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    if(usuario.getRol().equalsIgnoreCase("usuario") && usuario.getEnable().equals(true)){
                                        if(!list.contains(dataSnapshot.getKey())){
                                            usuario.setUidUsuario(dataSnapshot.getKey());
                                            delegadoAdd = usuario;
                                            listaDelegado.add(usuario);
                                        }
                                    }
                                }
                                delegadosAdapter1.notifyDataSetChanged();
                                if (binding.recyclerView3.getAdapter()!= null && binding.recyclerView3.getAdapter().getItemCount() == 0){
                                    //vacio
                                    Log.d("msg-test", "llega sin informacion");
                                    binding.recyclerView3.setVisibility(View.GONE);
                                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                                } else {
                                    binding.textNoRegistros.setVisibility(View.GONE);
                                    binding.recyclerView3.setVisibility(View.VISIBLE);

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


        //ir a la galeria
        iconoImagen.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        botonSiguiente.setOnClickListener(view -> {

            String nombreActividad = editTextActividad.getText().toString();
            String categoria = autoCompleteTextViewCategoria.getText().toString();
            crearActivityViewModel.getUid().observe(CrearActivity.this, uid -> {
                codigoDelegado = uid;
            });

            if (!nombreActividad.isEmpty() && !categoria.isEmpty() && urlImagen!=null && codigoDelegado!=null) {

                StorageReference carpetaFotosActividadesRef = storageReference.child("Fotos Actividades");
                StorageReference fotoRef = carpetaFotosActividadesRef.child(new Date().toString());

                fotoRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                actividad = new Actividad();
                                actividad.setNombreActividad(nombreActividad);
                                actividad.setCategoria(categoria);
                                actividad.setImagen(uri.toString());
                                actividad.setEstado(true);
                                actividad.setDelegado(codigoDelegado);
                                databaseReference2.push().setValue(actividad).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        databaseReference.child(codigoDelegado).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    Usuario user = snapshot.getValue(Usuario.class);
                                                    user.setRol("delegado_actividad");
                                                    databaseReference.child(snapshot.getKey()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(CrearActivity.this, "Actividad creada exitosamente", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CrearActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });

            }else{
                Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
            }


        });


        botonCancelar.setOnClickListener(view -> {
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

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            // La imagen seleccionada se encuentra en data.getData()
            urlImagen = data.getData();
            Picasso.get().load(urlImagen).into(previsualizaImagen);
            previsualizaImagen.setImageURI(urlImagen);
            //Intent intent  = new Intent(CrearActivity.this, EscogerDelegadoActivity.class);
            //intent.putExtra("uri_extra", path);
            Log.d("msg-test", "Selected URI: " + urlImagen);
            Log.d("msg-test", "uri.getLastPathSegment(): " + urlImagen.getLastPathSegment());
            /*StorageReference carpetaFotosActividadesRef = storageReference.child("fotosActividades/" +uri.getLastPathSegment());

            UploadTask uploadTask = carpetaFotosActividadesRef.putFile(uri);

            uploadTask.addOnFailureListener(exception -> {
                exception.printStackTrace();
                //
            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Toast.makeText(CrearActivity.this, "Archivo subido correctamente", Toast.LENGTH_SHORT).show();

                carpetaFotosActividadesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("link", String.valueOf(uri));

                        databaseReference.setValue(hashMap);
                    }
                });
            });*/


        }
    }

}