package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.activities.CambioContraseniaActivity;
import com.example.telebeetle.activities.MainActivity;
import com.example.telebeetle.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileFragment extends Fragment {

   FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageView = binding.fotoperfil;
        picasso.load(drawableResourceId)
                .resize(400,400)
                .transform(new CropCircleTransformation())
                .into(imageView);


        //para ir a cambio de contraseña

        binding.imageView12.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), CambioContraseniaActivity.class );
            startActivity(intent);

        });

        binding.imageView16.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        });

        return binding.getRoot();
    }
}