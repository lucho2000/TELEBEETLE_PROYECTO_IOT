package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityCrearBinding;
import com.example.telebeetle.databinding.ActivityEditarBinding;
import com.example.telebeetle.databinding.ActivityEditarEventoBinding;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
//import com.google.firebase.storage.StorageReference;

public class EditarActivity extends AppCompatActivity {


    TextInputEditText editNombre;

    Button botonEditar;
    Button cancelar;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseDatabase database;
    ImageView previsualizarImagen;
    FirebaseStorage storage;
    ActivityEditarBinding binding;
    Uri urlImagen;
    Actividad actividad;


   // StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        botonEditar = findViewById(R.id.buttonEditarActividad);
        editNombre = findViewById(R.id.editTextActividad);
        cancelar = binding.button4;
        previsualizarImagen = binding.imageView19;

        Intent intent = getIntent();
        String idActividad = intent.getStringExtra("uidActividad");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        rellenarConDataCamposEditarActividad(idActividad);
        ImageView iconoImagen  = findViewById(R.id.imageView7);

        iconoImagen.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        botonEditar.setOnClickListener(v -> {

            String nombreActividad = editNombre.getText().toString();

            if (!nombreActividad.isEmpty() && urlImagen!=null) {

                databaseReference = FirebaseDatabase.getInstance().getReference("actividad");

                StorageReference carpetaFotosActividadesRef = storageReference.child("Fotos Actividades");
                StorageReference fotoRef = carpetaFotosActividadesRef.child(new Date().toString());

                fotoRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri uriDownload = uriTask.getResult();
                        HashMap<String, Object> actividadUpdate = new HashMap<>();
                        actividadUpdate.put("nombreActividad",nombreActividad);
                        actividadUpdate.put("imagen", uriDownload.toString());
                        databaseReference.child(idActividad).updateChildren(actividadUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(EditarActivity.this,"Success to update event",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EditarActivity.this,"Failed to update event",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        finish();
                    }
                });

            }
        });

        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancelar.setOnClickListener(v->{finish();});
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("msg-test", "Selected URI: " + uri);
                    Log.d("msg-test", "uri.getLastPathSegment(): " + uri.getLastPathSegment());
                    previsualizarImagen = binding.imageView19;
                    previsualizarImagen.setImageURI(uri);
                    urlImagen = uri;
                } else {
                    Log.d("msg-test", "No media selected");
                }
            });
    public void rellenarConDataCamposEditarActividad(String idActividad){
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equalsIgnoreCase(idActividad)) {
                        actividad = dataSnapshot.getValue(Actividad.class);
                        if (actividad != null) {
                            editNombre.setText(actividad.getNombreActividad());
                            Picasso.get().load(actividad.getImagen()).into(previsualizarImagen);
                        } else {
                            //objeto nulo
                            Log.d("msg-test-firebase","objeto actividad nulo. no match with ID");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("msg-test-firebase","Ocurrio un error al obtener actividad especifica (con id) en firebase. EditarActivity");
            }
        });
    }
}