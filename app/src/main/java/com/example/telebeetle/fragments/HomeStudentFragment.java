package com.example.telebeetle.fragments;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.EventsActivity;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeStudentFragment extends Fragment {

    FragmentHomeStudentBinding binding;

    SearchView searchView;
    EventAdapter eventAdapter;
    DatabaseReference databaseReference;
    List<Evento> listaEvents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeStudentBinding.inflate(inflater,container,false);





        return binding.getRoot();
    }


    @Override
    public void onResume() {

        super.onResume();

        searchView = binding.searchView;
        searchView.clearFocus();

        databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"



        listaEvents = new ArrayList<>();

        eventAdapter = new EventAdapter();
        eventAdapter.setListEvents(listaEvents);
        eventAdapter.setContext(getActivity().getApplicationContext());
        binding.rvEvents.setAdapter(eventAdapter);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //codigo para extraer la data de firebase y mostrarla en el recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEvents.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    listaEvents.add(evento);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });



    }

    public void searchList(String text){

        ArrayList<Evento> searchList = new ArrayList<>();
        for(Evento evento: listaEvents){
            if(evento.getActividad().toLowerCase().contains(text.toLowerCase())){
                searchList.add(evento);

            }
        }

        eventAdapter.searchDataList(searchList);

    }
}