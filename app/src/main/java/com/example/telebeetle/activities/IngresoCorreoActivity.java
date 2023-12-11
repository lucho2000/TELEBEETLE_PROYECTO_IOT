package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityIngresoCorreoBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.services.Regex;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class IngresoCorreoActivity extends AppCompatActivity {

    Button btnReset,btnBack;
    EditText edtEmail;
    FirebaseAuth mAuth;

    String strEmail;

    ActivityIngresoCorreoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngresoCorreoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        btnBack = findViewById(R.id.buttonCancelar);
        btnReset = findViewById(R.id.buttonContinuar);

        edtEmail = binding.correo2.getEditText();

        mAuth = FirebaseAuth.getInstance();

        Regex regex = new Regex();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(regex.emailValid(edtEmail.getText().toString())){
                    ResetPassword();
                }else{
                    edtEmail.setError("El correo no puede estar vacio");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    /*

        Button buttonContinuar = findViewById(R.id.buttonContinuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresoCorreoActivity.this, CodigoCorreoActivity.class);
                startActivity(intent);
            }
        });

        Button buttonCancelar= findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresoCorreoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        */


    }

    private void ResetPassword(){


        mAuth.sendPasswordResetEmail(edtEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(IngresoCorreoActivity.this,"Link de cambio de contraseña ha sido enviado al correo ingresado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(IngresoCorreoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IngresoCorreoActivity.this,"Error"+e,Toast.LENGTH_SHORT).show();
            }
        });

    }

}