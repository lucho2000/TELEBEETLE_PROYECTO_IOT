package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //array de String para las opciones de condicion de alumno

    DatabaseReference databaseReference;

    String nombreCompleto, nombres, apellidos, rol, codigo, contrasena, correo, nuevaContra;

    EditText textNombre, textCodigo, textContrasenia, textCorreo, textNuevaContra;

    AutoCompleteTextView textRol;
    String[] opcionesCondicion = new String[]{
            "Alumno",
            "Egresado",
    };

    Button botonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        botonRegister = findViewById(R.id.buttonRegistro);
        textNombre = findViewById(R.id.textNombreCompleto);
        textCodigo = findViewById(R.id.textCodigo);
        textCorreo = findViewById(R.id.textCorreo);
        textContrasenia = findViewById(R.id.textContrasenia);
        textNuevaContra = findViewById(R.id.textNuevaContra);

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
        AutoCompleteTextView autoCompleteTextView =  findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesCondicion);
        autoCompleteTextView.setAdapter(adapter);

        botonRegister.setOnClickListener(view -> {

            nombreCompleto = textNombre.getText().toString();
            codigo = textCodigo.getText().toString();
            correo = textCorreo.getText().toString();
            contrasena = textContrasenia.getText().toString();
            rol = autoCompleteTextView.getText().toString();
            nuevaContra = textNuevaContra.getText().toString();

            if (!nombreCompleto.isEmpty() && !codigo.isEmpty() && !rol.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()
                    && !nuevaContra.isEmpty() && contrasena.equalsIgnoreCase(nuevaContra) ){

                Usuario nuevoUsuario  = new Usuario();
                nuevoUsuario.setNombres(nombreCompleto);
                nuevoUsuario.setCodigo(codigo);
                nuevoUsuario.setCorreo(correo);
                nuevoUsuario.setContrasena(contrasena);
                nuevoUsuario.setRol(rol);

                databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                databaseReference.child(codigo).setValue(nuevoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        textNombre.setText("");
                        textCodigo.setText("");
                        textCorreo.setText("");
                        textRol.setText("");
                        textContrasenia.setText("");
                        textNuevaContra.setText("");
                        Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }
}