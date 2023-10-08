package com.example.telebeetle.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.example.telebeetle.R;
import com.google.android.material.textfield.TextInputEditText;

public class CrearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        TextInputEditText editTextActividad = findViewById(R.id.editTextActividad);

        AutoCompleteTextView  autoCompleteTextViewCategoria = findViewById(R.id.categoria);

        String[] opcionesMenu = {"Masculino", "Femenino", "Mixto"}; //opciones del menu

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesMenu);

        autoCompleteTextViewCategoria.setAdapter(adapter); //el adapter llena el text view con las opciones

        ImageView iconoImagen  = findViewById(R.id.imageView7);


        iconoImagen.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            // La imagen seleccionada se encuentra en data.getData()
            Uri path = data.getData();
        }
    }
}