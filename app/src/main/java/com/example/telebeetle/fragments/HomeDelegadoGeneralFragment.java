package com.example.telebeetle.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.ActivityAdapter;
import com.example.telebeetle.activities.SolicitudesRegistroAdapter;
import com.example.telebeetle.activities.ValidarDonacionesActivity;
import com.example.telebeetle.databinding.FragmentHomeDelegadoGeneralBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeDelegadoGeneralFragment extends Fragment {

    FragmentHomeDelegadoGeneralBinding binding;
    ActivityAdapter activityAdapter;
    SolicitudesRegistroAdapter solicitudesRegistroAdapter;
    List<Usuario> solicitudesRegistro;
    List<Actividad> activityList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeDelegadoGeneralBinding.inflate(inflater,container,false);
        binding.goSolicitudes.setOnClickListener(view -> {
           showSheetSolicitudes();
        });
        binding.goDonaciones.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), ValidarDonacionesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().getApplicationContext().startActivity(intent);
        });
        return binding.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "evento"
        activityList = new ArrayList<>();
        activityAdapter = new ActivityAdapter();
        activityAdapter.setListActivities(activityList);
        activityAdapter.setContext(getActivity().getApplicationContext());
        binding.rvActividades.setAdapter(activityAdapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        //codigo para extraer la data de firebase y mostrarla en el recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activityList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uidActividad = dataSnapshot.getKey();
                    Actividad activity = dataSnapshot.getValue(Actividad.class);
                    activity.setUidActividad(uidActividad);
                    activityList.add(activity);
                }
                activityAdapter.notifyDataSetChanged();
                if (binding.rvActividades.getAdapter()!= null && binding.rvActividades.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    binding.rvActividades.setVisibility(View.GONE);
                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoRegistros.setVisibility(View.GONE);
                    binding.rvActividades.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSheetSolicitudes(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.solicitudes_registro_bottom_sheet_layout);
        ImageView cancelButton2 = dialog.findViewById(R.id.cancelButton2);
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir"); //datos de firebase de la coleccion de "evento"
        solicitudesRegistro = new ArrayList<>();
        solicitudesRegistroAdapter = new SolicitudesRegistroAdapter();
        solicitudesRegistroAdapter.setUsuarioList(solicitudesRegistro);
        solicitudesRegistroAdapter.setContext(getActivity().getApplicationContext());

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_solicitudesRegistro);
        recyclerView.setAdapter(solicitudesRegistroAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //validacion del recycler view vacio

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                solicitudesRegistro.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uidUsuario = dataSnapshot.getKey();
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    usuario.setUidUsuario(uidUsuario);
                    solicitudesRegistro.add(usuario);
                }
                solicitudesRegistroAdapter.notifyDataSetChanged();
                if (recyclerView.getAdapter()!= null && recyclerView.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    recyclerView.setVisibility(View.GONE);
                    dialog.findViewById(R.id.textNoRegistros).setVisibility(View.VISIBLE);
                } else {
                    dialog.findViewById(R.id.textNoRegistros).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomMenuDialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}