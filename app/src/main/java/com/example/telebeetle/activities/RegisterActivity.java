package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityRegisterBinding;
import com.example.telebeetle.services.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    //array de String para las opciones de condicion de alumno

    DatabaseReference databaseReference;

    String nombreCompleto, apellidos, condicion, codigo, contrasena, correo, nuevaContra;

    EditText textNombre, textCodigo, textContrasenia, textCorreo, textNuevaContra, textApellido;

    String[] opcionesCondicion = new String[]{
            "Alumno",
            "Egresado",
    };

    Button botonRegister;

    int valido = 0;

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    List<String> correos = new ArrayList<>();
    List<String> codigos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        botonRegister = findViewById(R.id.buttonRegistro);
        textNombre = findViewById(R.id.textNombreCompleto);
        textApellido = findViewById(R.id.textApellidos);
        textCodigo = findViewById(R.id.textCodigo);
        textCorreo = findViewById(R.id.textCorreo);
        textContrasenia = findViewById(R.id.textContrasenia);
        textNuevaContra = findViewById(R.id.textNuevaContra);

        firebaseAuth = FirebaseAuth.getInstance();

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
                finish();
            }
        });
        /* LINKEO HACIA INICIO DE SESION  */



        //llenado con las opciones con un Adapter
        AutoCompleteTextView autoCompleteTextView =  findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesCondicion);
        autoCompleteTextView.setAdapter(adapter);

        Regex regex =new Regex();

        //Recoger correos y codigos de la base de datos
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String correo = userSnapshot.child("correo").getValue(String.class);

                    if (correo != null) {
                        correos.add(correo);
                    }
                }



                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    String codigo = userSnapShot.child("codigo").getValue(String.class);

                    if (codigo != null) {
                        codigos.add(codigo);
                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "Error al leer datos", databaseError.toException());
            }
        });



        botonRegister.setOnClickListener(view -> {

            // it works xd
           /* for (String correo : correos) {
                Log.d("msg-test", correo);
            }
            for (String codigo : codigos) {
                Log.d("msg-test", codigo);
            }*/

            firebaseAuth = FirebaseAuth.getInstance();

            valido = 0;

            nombreCompleto = textNombre.getText().toString();
            apellidos = textApellido.getText().toString();
            codigo = textCodigo.getText().toString();
            correo = textCorreo.getText().toString();
            contrasena = textContrasenia.getText().toString();
            condicion = autoCompleteTextView.getText().toString(); //condicion: alumno o egresado
            nuevaContra = textNuevaContra.getText().toString();

            if (!nombreCompleto.isEmpty() && !apellidos.isEmpty() && !codigo.isEmpty() && !condicion.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()
                    && !nuevaContra.isEmpty()  ){


                if(codigos.contains(textCodigo.getText().toString())){
                    Log.d("msg-test", textCodigo.getText().toString());
                    textCodigo.setError("El codigo ingresado ya esta registrado");
                    valido++;
                }

                if(correos.contains(textCorreo.getText().toString())){
                    Log.d("msg-test", textCorreo.getText().toString());
                    textCorreo.setError("El correo ingresado ya esta registrado");
                    valido++;
                }

                if (!regex.inputisValid(nombreCompleto) ) {
                    textNombre.setError("Ingrese por lo menos dos nombres");
                    valido++;
                }

                if (!regex.inputisValid(apellidos) ) {
                    textApellido.setError("Ingrese por lo menos dos apellidos");
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
                    nuevoUsuario.setRol("usuario");
                    nuevoUsuario.setCondicion(condicion);
                    nuevoUsuario.setEnable(true);
                    nuevoUsuario.setRecibidoKitTeleco(false);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    // Get reference to the file
                    StorageReference forestRef = storageRef.child("usuario.png");
                    forestRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        nuevoUsuario.setProfile(downloadUrl);
                        // Use the download URL
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                        nuevoUsuario.setProfile("falta imagen");
                    });
                    databaseReference = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir");
                    firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("msg-test", "createUserWithEmail:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        databaseReference.child(uid).setValue(nuevoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                textNombre.setText("");
                                                textApellido.setText("");
                                                textCodigo.setText("");
                                                textCorreo.setText("");
                                                textContrasenia.setText("");
                                                textNuevaContra.setText("");
                                                Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, WaitActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("msg-test", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Se deben rellenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}