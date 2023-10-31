package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    BeginSignInRequest signInRequest;

    GoogleApi googleApiClient;

    final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    boolean showOneTapUI = true;

    ActivityMainBinding binding;

    private GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        /* LINKEO HACIA REGISTRO DE USUARIO */
        TextView registerTextView = findViewById(R.id.registrarmeTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA REGISTRO DE USUARIO */

        /* LINKEO HACIA OLVIDE MI CONTRASENA / INGRESO CORREO */
        TextView olvideMiContraseniaTextView = findViewById(R.id.olvideMiContraseniaTextView);
        olvideMiContraseniaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IngresoCorreoActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA OLVIDE MI CONTRASENA / INGRESO CORREO */


        /* LINKEO HACIA ACTIVITY GENERAL VIEW*/
       // Button button = findViewById(R.id.button);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Intermedio.class);
                startActivity(intent);
            }
        });*/
        /* LINKEO HACIA ACTIVITY GENERAL VIEW*/


        /** Linkeo para autenticacion con firebase*/

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.button.setOnClickListener(view -> {

            String email = binding.emailLogin.getText().toString();
            String password = binding.passwordLogin.getText().toString();

            if(!email.isEmpty() && !password.isEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this, GeneralViewActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(MainActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            }
        });



        //PARA EL LOGUEO CON GOOGLE


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.button2.setOnClickListener(view -> {


            // Inicia el proceso de inicio de sesión con Google.
            /*Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQ_ONE_TAP);*/

            googleSignIn();

        });

    }



    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInClient oneTapClient = null;
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {

                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        firebaseAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            //updateUI(null);
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }*/

    //REQ_ONE_TAP

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In fue exitoso, ahora puedes usar la cuenta de Google para autenticarte en Firebase.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireAuth(account.getIdToken());
            } catch (ApiException e) {
                Log.d("msg-test","El inicio de sesión con Google falló");

            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El inicio de sesión con Google fue exitoso.
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        // Realiza cualquier acción adicional que necesites aquí.
                    } else {

                        Log.d("msg-test","El inicio de sesión con Firebase falló.");
                    }
                });
    }

    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQ_ONE_TAP);

    }

    private void fireAuth(String idToken){

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();

                            map.put("name",user.getDisplayName());
                            map.put("correo",user.getEmail());
                            map.put("profile",user.getPhotoUrl().toString());

                            database.getReference().child("usuarios").child(user.getUid()).setValue(map);

                            Intent intent = new Intent(MainActivity.this,GeneralViewActivity.class);
                            startActivity(intent);

                        }else{
                            Log.d("msg-test","Algo paso.");
                        }

                    }
                });

    }




}