package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityCambioContrasenia2Binding;
import com.example.telebeetle.databinding.ActivityCambioContraseniaBinding;
import com.example.telebeetle.fragments.ProfileFragment;
import com.example.telebeetle.services.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class CambioContrasenia2Activity extends AppCompatActivity {


    ActivityCambioContrasenia2Binding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambioContrasenia2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Regex regex = new Regex();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        binding.iconPass2.setOnClickListener(view -> {

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


        binding.buttonAceptar2.setOnClickListener(v -> {
            String passNew = binding.contrasenaNew.getEditText().getText().toString(); //neuva contraseña
            String confirmarPassNew = binding.contrasenaNew2.getEditText().getText().toString(); //confirmar contraseña
            String contrasenaActual = binding.contrasenaActual.getEditText().getText().toString();

            Log.d("msg-test",passNew);
            if( !passNew.equals(contrasenaActual) &&  regex.contrasenaisValid(passNew) && passNew.equals(confirmarPassNew) ){

                user.updatePassword(passNew)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                Query emailQuery = usersRef.orderByChild("correo").equalTo(user.getEmail());

                                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                          if (dataSnapshot.exists()) {
                                              //char[] hashEnter = password.toCharArray();
                                              for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                  if (userSnapshot.child("correo").getValue(String.class).equalsIgnoreCase(user.getEmail())) {
                                                      //&& BCrypt.verifyer().verify(hashEnter,passDB).verified){
                                                      //Log.d("msg-test","Lo que obtuve de la db: "+passDB);
                                                      //Log.d("msg-test","Lo que ingrese: "+hashEnter);
                                                      String codigo = userSnapshot.child("codigo").getValue(String.class);
                                                      String correo = userSnapshot.child("correo").getValue(String.class);
                                                      String nombres = userSnapshot.child("nombres").getValue(String.class);
                                                      String apellidos = userSnapshot.child("apellidos").getValue(String.class);
                                                      //String contrasena = userSnapshot.child("contrasena").getValue(String.class);
                                                      String condicion = userSnapshot.child("condicion").getValue(String.class);
                                                      String rol = userSnapshot.child("rol").getValue(String.class);
                                                      Boolean enable = userSnapshot.child("enable").getValue(Boolean.class);
                                                      Boolean kitTele = userSnapshot.child("kit_teleco").getValue(Boolean.class);
                                                      String profile = userSnapshot.child("profile").getValue(String.class);
                                                      Usuario user = new Usuario(codigo, correo, nombres, apellidos, condicion, enable, kitTele, rol, profile);
                                                      Toast.makeText(CambioContrasenia2Activity.this,"Contraseña cambiada correctamente",Toast.LENGTH_SHORT).show();
                                                      Intent intent = new Intent(CambioContrasenia2Activity.this, GeneralViewActivity.class);
                                                      intent.putExtra("usuario", user);
                                                      startActivity(intent);
                                                      finish();
                                                      break;
                                                  }
                                              }
                                          }
                                      }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("msg-test","Error al volver a general de cambio de contraseña");
                                    }
                                });


                                /*Toast.makeText(CambioContrasenia2Activity.this,"Contraseña cambiada correctamente",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CambioContrasenia2Activity.this,GeneralViewActivity.class);
                                startActivity(intent);
                                finish();*/

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CambioContrasenia2Activity.this,"Hubo error al cambiar la contraseña",Toast.LENGTH_SHORT).show();
                                Log.d("msg-test", "error" + e.toString());
                            }
                        });



                        /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(CambioContrasenia2Activity.this,"Contraseña cambiada correctamente",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CambioContrasenia2Activity.this,GeneralViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(CambioContrasenia2Activity.this,"Hubo error al cambiar la contraseña",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

            }else{
                Toast.makeText(CambioContrasenia2Activity.this,"Revise si las contraseñas son iguales, además deben cumplir los requisitos",Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonCancelar2.setOnClickListener(v->{

            //Intent intent = new Intent(CambioContrasenia2Activity.this, ProfileFragment.class);
            //startActivity(intent);
            finish();

        });







    }
}