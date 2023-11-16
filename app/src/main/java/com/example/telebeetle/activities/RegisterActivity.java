package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.telebeetle.databinding.ActivityAfterGoogleBinding;
import com.example.telebeetle.databinding.ActivityRegisterBinding;
import com.example.telebeetle.services.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //array de String para las opciones de condicion de alumno

    DatabaseReference databaseReference;

    String nombreCompleto, apellidos, condicion, codigo, contrasena, correo, nuevaContra;

    EditText textNombre, textCodigo, textContrasenia, textCorreo, textNuevaContra, textApellidos;

    AutoCompleteTextView textRol;
    String[] opcionesCondicion = new String[]{
            "Alumno",
            "Egresado",
    };

    Button botonRegister;

    int valido = 0;

    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        botonRegister = findViewById(R.id.buttonRegistro);
        textNombre = findViewById(R.id.textNombreCompleto);
        textApellidos = findViewById(R.id.textApellidos);
        textCodigo = findViewById(R.id.textCodigo);
        textCorreo = findViewById(R.id.textCorreo);
        textContrasenia = findViewById(R.id.textContrasenia);
        textNuevaContra = findViewById(R.id.textNuevaContra);

        /* condiciones de la contraseña*/
         binding.iconPass.setOnClickListener(view -> {

             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("Condiciones de la contraseña");
             builder.setMessage("Requisitos para la contraseña, \n" +
                     "Para ingresar su contraseña nueva, considere las siguientes restricciones:\n" +
                     "• Que la contraseña contenga al menos ocho caracteres.\n" +
                     "• Que la contraseña contenga al menos un número.\n" +
                     "• Que la contraseña contenga al menos un carácter especial (@#$%&*).\n" +
                     "• Que la contraseña contenga al menos una mayúscula.");

             builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                 }
             });

             AlertDialog dialog = builder.create();
             dialog.show();

         });


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

        Regex regex =new Regex();


        botonRegister.setOnClickListener(view -> {
            valido = 0;

            nombreCompleto = textNombre.getText().toString();
            apellidos = binding.textApellidos.getText().toString();
            codigo = textCodigo.getText().toString();
            correo = textCorreo.getText().toString();
            contrasena = textContrasenia.getText().toString();
            condicion = autoCompleteTextView.getText().toString(); //condicion: alumno o egresado
            nuevaContra = textNuevaContra.getText().toString();

            if (!nombreCompleto.isEmpty() && !apellidos.isEmpty() && !codigo.isEmpty() && !condicion.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()
                    && !nuevaContra.isEmpty()  ){

                if (!regex.inputisValid(nombreCompleto) ) {
                    textNombre.setError("Ingrese por lo menos un nombre");
                    valido++;
                }

                if (!regex.inputisValid(apellidos) ) {
                    textApellidos.setError("Ingrese por lo menos un apellido");
                    valido++;
                }

                if (!regex.codigoValid(textCodigo.getText().toString())){
                    textCodigo.setError("Ingrese un codigo PUCP valido");
                    valido++;
                }

                if (!regex.emailValid(textCorreo.getText().toString())){
                    textCorreo.setError("Ingrese un correo pucp valido");
                    valido++;
                }

                if (!regex.contrasenaisValid(textContrasenia.getText().toString())){
                    textContrasenia.setError("Ingrese una contraseña que cumpla con las condiciones");
                    valido++;
                }
                if(!contrasena.equalsIgnoreCase(nuevaContra)){
                    textNuevaContra.setError("Las contraseñas deben ser iguales");
                    valido++;
                }

                if(valido==0){
                    Usuario nuevoUsuario  = new Usuario();
                    nuevoUsuario.setNombres(nombreCompleto);
                    nuevoUsuario.setApellidos(apellidos);
                    nuevoUsuario.setCodigo(codigo);
                    nuevoUsuario.setCorreo(correo);
                    nuevoUsuario.setContrasena(contrasena);
                    nuevoUsuario.setRol(condicion);

                    databaseReference = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir");
                    databaseReference.child(codigo).setValue(nuevoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            textNombre.setText("");
                            binding.textApellidos.setText("");
                            textCodigo.setText("");
                            textCorreo.setText("");
                            textRol.setText("");
                            textContrasenia.setText("");
                            textNuevaContra.setText("");
                            Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                        }
                    });
                }



            }else{
                Toast.makeText(RegisterActivity.this, "Se deben rellenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}