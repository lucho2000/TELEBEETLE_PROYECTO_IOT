package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.viewmodels.GeneralViewActivityViewModel;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.io.CharArrayReader;
import java.util.HashMap;
import java.util.Objects;

//import at.favre.lib.crypto.bcrypt.BCrypt;

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

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, GeneralViewActivity.class);
            //intent.putExtra("usuario",user);
            startActivity(intent);
            finish();
        }else{
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

            database = FirebaseDatabase.getInstance();

            //verificacion credenciales para loguearse
            binding.button.setOnClickListener(view -> {
                //final Boolean found = false;
                String email = binding.emailLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();
                //String hash = BCrypt.withDefaults().hashToString(12,password.toCharArray());

                //Log.d("msg-test", hash);

                //BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);

                //Log.d("msg-test", String.valueOf(result));
                if(!email.isEmpty() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("msg-test", "signInWithEmail:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                        Query emailQuery = usersRef.orderByChild("correo").equalTo(user.getEmail());
                                        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    //char[] hashEnter = password.toCharArray();
                                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                        if(userSnapshot.child("correo").getValue(String.class).equalsIgnoreCase(user.getEmail())){
                                                            //&& BCrypt.verifyer().verify(hashEnter,passDB).verified){
                                                            //Log.d("msg-test","Lo que obtuve de la db: "+passDB);
                                                            //Log.d("msg-test","Lo que ingrese: "+hashEnter);
                                                            //String codigo = userSnapshot.child("codigo").getValue(String.class);
                                                            //String correo = userSnapshot.child("correo").getValue(String.class);
                                                            //String nombres = userSnapshot.child("nombres").getValue(String.class);
                                                            //String apellidos = userSnapshot.child("apellidos").getValue(String.class);
                                                            ////String contrasena = userSnapshot.child("contrasena").getValue(String.class);
                                                            //String  condicion = userSnapshot.child("condicion").getValue(String.class);
                                                            //String rol = userSnapshot.child("rol").getValue(String.class);
                                                            //Boolean enable = userSnapshot.child("enable").getValue(Boolean.class);
                                                            //Boolean kitTele = userSnapshot.child("kit_teleco").getValue(Boolean.class);
                                                            //String profile = userSnapshot.child("profile").getValue(String.class);
                                                            //Usuario user = new Usuario(codigo,correo,nombres,apellidos,condicion,enable,kitTele, rol, profile);
                                                            Intent intent = new Intent(MainActivity.this, GeneralViewActivity.class);
                                                            //intent.putExtra("usuario",user);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    DatabaseReference usersRef2 = FirebaseDatabase.getInstance().getReference().child("usuarios_por_admitir");
                                                    Query emailQuery2 = usersRef2.orderByChild("correo").equalTo(user.getEmail());
                                                    emailQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()) {
                                                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                                    if(userSnapshot.child("correo").getValue(String.class).equalsIgnoreCase(user.getEmail())){
                                                                        Intent intent = new Intent(MainActivity.this, WaitActivity.class);
                                                                        startActivity(intent);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle any errors
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("msg-test", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


               /* firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(taskAuth -> {
                    if(taskAuth.isSuccessful()){
                        /*DatabaseReference databaseReference = database.getReference("usuarios");

                        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                    DataSnapshot snapshot = task.getResult();
                                    if (snapshot.exists()){
                                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                            //Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                            if (Objects.requireNonNull(dataSnapshot.getKey()).equalsIgnoreCase(Objects.requireNonNull(taskAuth.getResult().getUser()).getUid())) {
                                                Usuario usuarioDabase = dataSnapshot.getValue(Usuario.class);
                                                Intent intent = new Intent(MainActivity.this, GeneralViewActivity.class);
                                                intent.putExtra("usuario",usuarioDabase);
                                                startActivity(intent);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });




                    }else{
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                    }
                });*/

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
                disconnectGoogleAccount();
            });
        }



    }



    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    private void disconnectGoogleAccount() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // Realiza la desconexión de la cuenta de Google
                });
    }


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
                            /*FirebaseUser user = firebaseAuth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();

                            map.put("name",user.getDisplayName());
                            map.put("correo",user.getEmail());
                            map.put("profile",user.getPhotoUrl().toString());
                            // todo Cambiar el nombre de usuarios y la pagina q redirige
                            database.getReference().child("usuarios").child(user.getUid()).setValue(map);*/

                            /*Intent intent = new Intent(MainActivity.this,AfterGoogleActivity.class);
                            startActivity(intent);*/
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            checkUserInDatabase(user);

                        }else{
                            Log.d("msg-test","Algo paso.");
                        }

                    }
                });

    }


    private void checkUserInDatabase(FirebaseUser user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
        usersRef.orderByChild("correo").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d("msg-test", String.valueOf(user));
                    System.out.println(dataSnapshot);*/


                    // El correo electrónico está en la base de datos, el usuario puede continuar
                    // redirigiendo a la actividad que desees.
                    Usuario usu = new Usuario();
                    for(DataSnapshot childSnapshot1 : dataSnapshot.getChildren()){
                        String codigo = childSnapshot1.child("codigo").getValue(String.class);
                        String correo = childSnapshot1.child("correo").getValue(String.class);
                        String nombres = childSnapshot1.child("nombres").getValue(String.class);
                        String apellidos = childSnapshot1.child("apellidos").getValue(String.class);
                        //String contrasena = childSnapshot1.child("contrasena").getValue(String.class);
                        String  condicion = childSnapshot1.child("condicion").getValue(String.class);
                        Boolean enable = childSnapshot1.child("enable").getValue(Boolean.class);
                        Boolean kitTele = childSnapshot1.child("kit_teleco").getValue(Boolean.class);
                        String profile = childSnapshot1.child("profile").getValue(String.class);
                        String rol = childSnapshot1.child("rol").getValue(String.class);



                        usu = new Usuario(nombres, apellidos, codigo, correo, condicion);
                        usu.setEnable(enable);
                        usu.setRecibidoKitTeleco(kitTele);
                        usu.setProfile(profile);
                        usu.setRol(rol);
                    }



                    Intent intent = new Intent(MainActivity.this, GeneralViewActivity.class);
                    //intent.putExtra("usuario",dataSnapshot.getValue(Usuario.class));
                    //Log.d("msg-test",dataSnapshot.getValue(Usuario.class).getCondicion());
                    intent.putExtra("usuario",usu);
                    startActivity(intent);
                } else {
                    DatabaseReference usersRef2 = FirebaseDatabase.getInstance().getReference().child("usuarios_por_admitir");
                    Query emailQuery2 = usersRef2.orderByChild("correo").equalTo(user.getEmail());
                    emailQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    if(userSnapshot.child("correo").getValue(String.class).equalsIgnoreCase(user.getEmail())){
                                        Intent intent = new Intent(MainActivity.this, WaitActivity.class);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }else{
                                // El correo electrónico no está en la base de datos, el usuario se dirige a una actividad diferente.
                                Intent intent = new Intent(MainActivity.this, AfterGoogleActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Maneja errores de lectura de la base de datos si es necesario.
                Log.d("msg-test", "Hubo un error");
            }
        });

    }
}