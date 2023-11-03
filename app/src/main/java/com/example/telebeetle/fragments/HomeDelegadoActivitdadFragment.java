package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.telebeetle.Entity.Activity;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.DetalleActividad;
import com.example.telebeetle.activities.DetalleActividadGeneralActivity;
import com.example.telebeetle.activities.EventHorizontalAdapter;
import com.example.telebeetle.databinding.FragmentHomeDelegadoActivitdadBinding;
import com.example.telebeetle.databinding.FragmentHomeDelegadoGeneralBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class HomeDelegadoActivitdadFragment extends Fragment {
    FragmentHomeDelegadoActivitdadBinding binding;
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
        cargarListaEventos();
        return binding.getRoot();
    }
    public List<Evento> listaEventHardcodeado(){
        List<Evento> eventoList = new ArrayList<>();
        for(int i=0; i<10; i++){
            Evento event = new Evento("Voley Damas", "Curatos de Final", "01/11/23", "Polideportivo");
            event.setHora(i+":00 pm");
            eventoList.add(event);
        }
        return eventoList;
    }
    public void cargarListaEventos(){
        List<Evento> eventoList = listaEventHardcodeado();
        EventHorizontalAdapter eventHorizontalAdapter = new EventHorizontalAdapter();
        eventHorizontalAdapter.setEventoList(eventoList);
        eventHorizontalAdapter.setContext(getActivity().getApplicationContext());

        binding.rvEventos.setAdapter(eventHorizontalAdapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));
    }

}