package com.example.telebeetle.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.CambioContrasenia2Activity;
import com.example.telebeetle.activities.CambioContraseniaActivity;
import com.example.telebeetle.activities.CondicionesActivity;
import com.example.telebeetle.activities.GeneralViewActivity;
import com.example.telebeetle.activities.MainActivity;
import com.example.telebeetle.databinding.FragmentProfileBinding;
import com.example.telebeetle.viewmodels.GeneralViewActivityViewModel;
//import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileFragment extends Fragment {

   FragmentProfileBinding binding;
   FirebaseAuth firebaseAuth;

   Uri urlImagen;

   StorageReference storageReference;

   DatabaseReference databaseReference;

    ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        imageView = binding.fotoperfil;
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        //firebaseAuth = FirebaseAuth.getInstance();
       // FirebaseUser user = firebaseAuth.getCurrentUser();
       //Log.d("msg-test",user.getEmail());
        GeneralViewActivityViewModel generalViewActivityViewModel = new ViewModelProvider(requireActivity()).get(GeneralViewActivityViewModel.class);
        generalViewActivityViewModel.getUsuario().observe(getViewLifecycleOwner(),usuario -> {
            //Log.d("msg-test",usuario.getNombres());
            //Log.d("msg-test",usuario.getCorreo());
            //Log.d("msg-test",usuario.getCondicion());
            String nombreApellido = usuario.getNombres() + " " + usuario.getApellidos();
            binding.textView28.setText(nombreApellido);
            binding.textView29.setText(usuario.getCorreo());
            binding.textView30.setText(usuario.getCodigo());
            //Log.d("msg-test", usuario.getProfile());
            Picasso.get().load(usuario.getProfile())
                    .resize(400,400)
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        });
        String provideID2 = FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(FirebaseAuth.getInstance().getCurrentUser().getProviderData().size() - 1).getProviderId();
        if (provideID2.equals("google.com")) {
            binding.changePass.setVisibility(View.INVISIBLE);
        }
        //para ir a cambio de contraseña

        binding.imageView12.setOnClickListener(view -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            Log.d("msg-test", String.valueOf(user.getProviderData()));

            String provideID = user.getProviderData().get(user.getProviderData().size() - 1).getProviderId();

            Log.d("msg-test", "Provider: " + user.getProviderData().get(user.getProviderData().size() - 1).getProviderId());


            if (provideID.equals("password")) {
                Intent intent = new Intent(getActivity(), CambioContrasenia2Activity.class );
                startActivity(intent);
            } else if (provideID.equals("google.com")) {
                Toast.makeText(getActivity(),"Usted esta usando su cuenta google",Toast.LENGTH_SHORT).show();
                Log.d("msg-test","Google");
            } else{
                Log.d("msg-test","providern't");
            }



        });

        binding.imageView16.setOnClickListener(view -> {


            /*AuthUi.getInstance().signOut(getContext())
                            .addOnCompleteListener(task -> {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            });*/


            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
            CometChatUIKit.logout(new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {
                    Log.d("msg-test-logout-chat", "Logout chat completed successfully");
                }
                @Override
                public void onError(CometChatException e) {
                    Log.d("msg-test-logout-chat", "Logout chat failed with exception: " + e.getMessage());
                }
            });
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();



        });

        binding.imageView14.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), CondicionesActivity.class);
            startActivity(intent);


        });

        ActivityResultLauncher<PickVisualMediaRequest> launcher  =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), urlImagen -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (urlImagen != null) {
                        Log.d("msg-test", "Selected URI: " + urlImagen);
                        subirImagenAFirebase(urlImagen);

                    } else {
                        Log.d("msg-test", "No media selected");
                    }

                });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });




        return binding.getRoot();
    }

    public void subirImagenAFirebase(Uri urlImagen){
        StorageReference carpetaFotosDonacionesRef = storageReference.child("fotos perfiles");
        StorageReference fotoRef = carpetaFotosDonacionesRef.child(new Date().toString());
        String UIDusuarioActual = firebaseAuth.getUid();
        fotoRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());

                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlDescarga = uri.toString();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                        databaseReference.child(UIDusuarioActual).child("profile").setValue(urlDescarga);

                    }
                });


            }
        });

//        Picasso.get().load()
//                .resize(400,400)
//                .transform(new CropCircleTransformation())
//                .into(binding.fotoperfil);

    }

    @Override
    public void onResume() {
        super.onResume();
        databaseReference= FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String urlProfile = snapshot.getValue(String.class);
                Picasso.get().load(urlProfile)
                        .resize(400,400)
                        .transform(new CropCircleTransformation())
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}