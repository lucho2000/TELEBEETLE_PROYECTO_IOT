package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.telebeetle.R;
import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.storage.StorageReference;

public class EditarActivity extends AppCompatActivity {


    EditText editNombre;

    Button botonEditar;

    DatabaseReference databaseReference;

   // StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        botonEditar = findViewById(R.id.buttonEditarActividad);
        editNombre = findViewById(R.id.editTextActividad);




    }
}