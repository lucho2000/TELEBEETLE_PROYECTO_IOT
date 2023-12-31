package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.DonacionesAdapter;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.QRDonarActivity;
import com.example.telebeetle.databinding.FragmentDonationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.List;


public class DonationFragment extends Fragment {

    FragmentDonationBinding binding;

    DatabaseReference databaseReference2;

    List<Donacion> donaciones;

    FirebaseAuth firebaseAuth ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDonationBinding.inflate(inflater,container,false);

        cargarlistaDonaciones();


        binding.buttonDonar.setOnClickListener(view -> {

            Intent intent = new Intent(container.getContext(), QRDonarActivity.class);
            startActivity(intent);

        });


        return binding.getRoot();
    }


    public List<Donacion> listarDonacionesHardcoded (){

        List<Donacion> listDona  = new ArrayList<>();

        Donacion dona1 = new Donacion("Donacion para la AITEL","10 de Octubre, 2023");
        Donacion dona2 = new Donacion("Donacion para la AITEL","11 de Octubre, 2023");
        Donacion dona3 = new Donacion("Donacion para la AITEL","12 de Octubre, 2023");
        Donacion dona4 = new Donacion("Donacion para la AITEL","13 de Octubre, 2023");

        listDona.add(dona1);
        listDona.add(dona2);
        listDona.add(dona3);
        listDona.add(dona4);

        return listDona;

    }

    public void cargarlistaDonaciones(){
        donaciones = new ArrayList<>();
        DonacionesAdapter donacionesAdapter = new DonacionesAdapter();
        donacionesAdapter.setListDonaciones(donaciones);
        donacionesAdapter.setContext(getActivity().getApplicationContext());
        binding.rvDonaciones.setAdapter(donacionesAdapter);
        binding.rvDonaciones.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        String uidActual = firebaseAuth.getCurrentUser().getUid();

        databaseReference2 = FirebaseDatabase.getInstance().getReference("donaciones");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donaciones.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String keyDonacion = dataSnapshot.getKey();
                    Donacion donacion = dataSnapshot.getValue(Donacion.class);

                    if(donacion.getUidDonante().equalsIgnoreCase(uidActual) &&
                       donacion.getAccepted().equals(true)){ //filtrando para que liste las donaciones validadas que el usuario hizo
                        donaciones.add(donacion);

                    }

                }
                donacionesAdapter.notifyDataSetChanged();
                if (binding.rvDonaciones.getAdapter()!= null && binding.rvDonaciones.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    binding.rvDonaciones.setVisibility(View.GONE);
                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoRegistros.setVisibility(View.GONE);
                    binding.rvDonaciones.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}