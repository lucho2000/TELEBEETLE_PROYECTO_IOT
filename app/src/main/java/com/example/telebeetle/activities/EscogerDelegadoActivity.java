package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityEscogerDelegadoBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.dto.DelegadoDto;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EscogerDelegadoActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    StorageReference storageReference;
    ActivityEscogerDelegadoBinding binding;
    List<Usuario> listaDelegado;

    Button crearActividad;

    EditText editTextNombreAct;

    String urlImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEscogerDelegadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crearActividad = findViewById(R.id.buttonCrearActividad);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        listaDelegado = new ArrayList<>();

        DelegadoDto delegadoDto = new DelegadoDto();


        DelegadosAdapter delegadosAdapter1 = new DelegadosAdapter();
        delegadosAdapter1.setListaDelegados(listaDelegado);
        delegadosAdapter1.setContext(EscogerDelegadoActivity.this);


        binding.rvDelegadosActividad.setAdapter(delegadosAdapter1);
        binding.rvDelegadosActividad.setLayoutManager(new LinearLayoutManager(EscogerDelegadoActivity.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        listaDelegado.add(usuario);
                    }
                    delegadosAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        crearActividad.setOnClickListener(view -> {
            //String nombreActividad = editTextNombreAct.getText().toString();
            Intent intent = getIntent();
            if (intent != null) {
                String nombreActRecibido = intent.getStringExtra("nombre");
                String categoriaAct = intent.getStringExtra("categoria");
                String urlImagenRecibida = intent.getStringExtra("imagen");

                Uri uriImagen = Uri.parse(urlImagenRecibida);

                storageReference = FirebaseStorage.getInstance().getReference().child(nombreActRecibido).child(uriImagen.getLastPathSegment());

                /*storageReference.putFile(uriImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                       while (!uriTask.isComplete());
                       Uri url = uriTask.getResult();
                       urlImagen = url.toString();

                    }
                });*/


                databaseReference = FirebaseDatabase.getInstance().getReference("actividad");

                Actividad actividad = new Actividad();
                actividad.setNombreActividad(nombreActRecibido);
                actividad.setCategoria(categoriaAct);
                //actividad.setImagen(urlImagen);

                databaseReference.push().setValue(actividad).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EscogerDelegadoActivity.this, "Actividad creada exitosamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EscogerDelegadoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }



}