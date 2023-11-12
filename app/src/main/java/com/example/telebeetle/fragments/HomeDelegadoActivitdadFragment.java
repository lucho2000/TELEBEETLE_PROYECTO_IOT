package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.DetalleActividad;
import com.example.telebeetle.activities.EventHorizontalAdapter;
import com.example.telebeetle.databinding.FragmentHomeDelegadoActivitdadBinding;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeDelegadoActivitdadBinding.inflate(inflater,container,false);
        CardView cardView = binding.card;
        cardView.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), DetalleActividad.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().getApplicationContext().startActivity(intent);
        });
        ImageView iv= binding.more2;
        iv.setImageResource(R.drawable.baseline_more_horiz_24);
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageViewDelegado = binding.imageViewDelegado2;
        picasso.load(drawableResourceId)
                .resize(75,75)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        int drawableResourceId2 = R.drawable.voley;
        Picasso picasso2 = Picasso.get();
        ImageView imageViewActivity = binding.imageViewActivity2;
        picasso2.load(drawableResourceId2)
                .resize(240,120)
                .transform(new RoundedCornersTransformation(
                        8,
                        0
                ))
                .into(imageViewActivity);
        TextView nombre = binding.delegado;
        nombre.setText("Voley Damas");
        TextView delegado = binding.nameDelegado2;
        delegado.setText("Julio Aliaga");
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