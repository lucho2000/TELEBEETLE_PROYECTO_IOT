package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    //array de String para las opciones de condicion de alumno
    String[] opcionesCondicion = new String[]{
            "Alumno",
            "Egresado",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* LINKEO HACIA INICIO DE SESION */
        TextView iniciarSesionTextView = findViewById(R.id.iniciarSesionTextView);
        iniciarSesionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA INICIO DE SESION  */



        //llenado con las opciones con un Adapter
        AutoCompleteTextView autoCompleteTextView =  findViewById(R.id.condicion);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesCondicion);
        autoCompleteTextView.setAdapter(adapter);
    }
}