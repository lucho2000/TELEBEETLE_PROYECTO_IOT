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
import android.widget.Button;
import android.widget.ImageView;

import com.example.telebeetle.R;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

public class CrearActivity extends AppCompatActivity {

    Button botonSiguiente, botonCancelar;

    ImageView previsualizaImagen;

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
        botonSiguiente = findViewById(R.id.buttonSiguiente);
        botonCancelar = findViewById(R.id.buttonCancelar);
        previsualizaImagen = findViewById(R.id.imageViewPrevisualiza);
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


            if (!nombreActividad.isEmpty() && !categoria.isEmpty()) {
                Intent intent  = new Intent(CrearActivity.this, EscogerDelegadoActivity.class);
                intent.putExtra("nombre", nombreActividad);
                intent.putExtra("categoria", categoria);
                //intent.putExtra("iamgen", )
                startActivity(intent);
            }


        });


        botonCancelar.setOnClickListener(view -> {

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            // La imagen seleccionada se encuentra en data.getData()
            Uri path = data.getData();
            Picasso.get().load(path).into(previsualizaImagen);
            previsualizaImagen.setImageURI(path);
            Intent intent  = new Intent(CrearActivity.this, EscogerDelegadoActivity.class);
            intent.putExtra("imagen", path);

        }
    }
}