package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.DetalleActividad;
import com.example.telebeetle.activities.EventHorizontalAdapter;
import com.example.telebeetle.databinding.FragmentHomeDelegadoActivitdadBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class HomeDelegadoActivitdadFragment extends Fragment {
    FragmentHomeDelegadoActivitdadBinding binding;
    EventHorizontalAdapter eventHorizontalAdapter;
    DatabaseReference databaseReference;
    List<Evento> listaEvents;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    String uid;
    String uidActividad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeDelegadoActivitdadBinding.inflate(inflater,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "usuarios"
        uid =firebaseAuth.getCurrentUser().getUid();
        databaseReference2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Usuario user = snapshot.getValue(Usuario.class);
                    CardView cardView = binding.card;
                    cardView.setOnClickListener(view1 -> {
                        Intent intent = new Intent(getActivity().getApplicationContext(), DetalleActividad.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().getApplicationContext().startActivity(intent);
                    });
                    ImageView imageViewDelegado = binding.imageViewDelegado2;
                    Picasso.get().load(user.getProfile())
                            .resize(75,75)
                            .transform(new CropCircleTransformation())
                            .into(imageViewDelegado);
                    TextView delegado = binding.nameDelegado2;
                    delegado.setText(user.getNombres() + " " + user.getApellidos());

                    databaseReference3 = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Actividad actividad1 = dataSnapshot.getValue(Actividad.class);
                                if(actividad1.getDelegado().equalsIgnoreCase(uid)){
                                    ImageView imageViewActivity = binding.imageViewActivity2;
                                    Picasso.get().load(actividad1.getImagen())
                                            .resize(240,120)
                                            .transform(new RoundedCornersTransformation(
                                                    8,
                                                    0
                                            ))
                                            .into(imageViewActivity);
                                    TextView nombre = binding.delegado;
                                    nombre.setText(actividad1.getNombreActividad());
                                    uidActividad = dataSnapshot.getKey();
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else{
                    Log.d("User", "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", "Failed to read value.", error.toException());
            }
        });

        return binding.getRoot();
    }
    @Override

    public void onResume() {
        super.onResume();
        databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
        listaEvents = new ArrayList<>();
        eventHorizontalAdapter = new EventHorizontalAdapter();
        eventHorizontalAdapter.setEventoList(listaEvents);
        eventHorizontalAdapter.setContext(getActivity().getApplicationContext());
        binding.rvEventos.setAdapter(eventHorizontalAdapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        //codigo para extraer la data de firebase y mostrarla en el recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEvents.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    evento.setUidEvento(dataSnapshot.getKey());
                    listaEvents.add(evento);
                }
                eventHorizontalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}