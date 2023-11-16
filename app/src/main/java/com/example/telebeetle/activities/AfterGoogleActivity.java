package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityAfterGoogleBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.services.Regex;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AfterGoogleActivity extends AppCompatActivity {

    ActivityAfterGoogleBinding binding;
    DatabaseReference databaseReference;
    String nombres, apellidos, codigo, condicion;
    EditText nombreText, apellidoText, codigoText;
    String[] opcionesCondicion = new String[]{
            "Alumno",
            "Egresado",
    };

    Button botonRegister;
    int valido = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterGoogleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        botonRegister = findViewById(R.id.buttonRegistro);
        nombreText = findViewById(R.id.textNombreCompleto);
        apellidoText = findViewById(R.id.textApellidos);
        codigoText = findViewById(R.id.textCodigo);
        AutoCompleteTextView autoCompleteTextView =  findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesCondicion);
        autoCompleteTextView.setAdapter(adapter);

        Regex regex =new Regex();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios_por_admitir");

        botonRegister.setOnClickListener(view -> {
            valido = 0;
            nombres = nombreText.getText().toString();
            apellidos = apellidoText.getText().toString();
            codigo = codigoText.getText().toString();
            condicion = autoCompleteTextView.getText().toString(); //condicion: alumno o egresado

            if (!condicion.isEmpty() && !codigo.isEmpty() && !nombres.isEmpty() && !apellidos.isEmpty()) {
                if (!regex.inputisValid(nombres) ) {
                    nombreText.setError("Ingrese por lo menos un nombre");
                    valido++;
                }

                if (!regex.inputisValid(apellidos) ) {
                    apellidoText.setError("Ingrese por lo menos un apellido");
                    valido++;
                }

                if (!regex.codigoValid(codigo)){
                    codigoText.setError("Ingrese un codigo PUCP valido");
                    valido++;
                }
                if(valido==0){
                    // Obtiene el usuario actual de Firebase
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Crea un mapa con la información a almacenar en la base de datos
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("nombres", nombres);
                        map.put("apellidos", apellidos);
                        map.put("correo", user.getEmail());
                        map.put("profile", user.getPhotoUrl().toString());
                        map.put("codigo", codigo);
                        map.put("condicion", condicion);
                        map.put("rol", "usuario");
                        map.put("enable", true);
                        map.put("kit_teleco", false);
                        // Almacena la información en la base de datos
                        databaseReference.child(user.getUid()).setValue(map)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AfterGoogleActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                    // Los datos se guardaron con éxito
                                    Intent intent = new Intent(AfterGoogleActivity.this,WaitActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //Toast.makeText(AfterGoogleActivity.this, "Información guardada", Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {
                                    // Ocurrió un error al guardar los datos
                                    Toast.makeText(AfterGoogleActivity.this, "Error al guardar la información", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    // Notifica al usuario que los campos deben estar completos
                    Toast.makeText(AfterGoogleActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}