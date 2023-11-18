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
import com.example.telebeetle.services.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class CambioContrasenia2Activity extends AppCompatActivity {


    ActivityCambioContrasenia2Binding binding;

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
            String passNew = binding.contrasenaNew.getEditText().getText().toString();
            String passNew2 = binding.contrasenaNew2.getEditText().getText().toString();
            Log.d("msg-test",passNew);
            if(regex.contrasenaisValid(passNew) && passNew2.equalsIgnoreCase(passNew)){

                user.updatePassword(passNew)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CambioContrasenia2Activity.this,"Contraseña cambiada correctamente",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CambioContrasenia2Activity.this,GeneralViewActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CambioContrasenia2Activity.this,"Hubo error al cambiar la contraseña",Toast.LENGTH_SHORT).show();
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

            Intent intent = new Intent(CambioContrasenia2Activity.this,CambioContraseniaActivity.class);
            startActivity(intent);
            finish();

        });







    }
}