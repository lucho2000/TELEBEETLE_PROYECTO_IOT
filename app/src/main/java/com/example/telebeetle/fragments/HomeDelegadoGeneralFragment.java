package com.example.telebeetle.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.telebeetle.Entity.Activity;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.ActivityAdapter;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.SolicitudesRegistroAdapter;
import com.example.telebeetle.databinding.FragmentHomeDelegadoGeneralBinding;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;
import com.google.android.material.carousel.CarouselLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeDelegadoGeneralFragment extends Fragment {

    FragmentHomeDelegadoGeneralBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeDelegadoGeneralBinding.inflate(inflater,container,false);
        cargarListaActividades();
        binding.goSolicitudes.setOnClickListener(view -> {
           showSheetSolicitudes();
        });
        return binding.getRoot();
    }

    public List<Activity> listaActividadesHarcoede(){
       List<Activity>  activityList = new ArrayList<>();
       Activity activity1 = new Activity("Voley mixto", "Willy Huallpa");
       activityList.add(activity1);
       Activity activity2 = new Activity("Futsal varones", "Rommel Garay");
       activityList.add(activity2);
       Activity activity3 = new Activity("Barras teleenchufe", "Leonardo Abanto");
       activityList.add(activity3);
       return activityList;
    }
    public List<Usuario> listaUsuariosHarcodeado(){
        List<Usuario> usuarioList = new ArrayList<>();
        Usuario usuario1 = new Usuario("Rodrigo", "Barrios", "20202073", "a20202073@pucp.edu.pe", "Estudiante");
        usuarioList.add(usuario1);
        Usuario usuario2 = new Usuario("Julio", "Carrion", "20161234", "elmionks2pucp.edu.pe", "Egresado");
        usuarioList.add(usuario2);
        usuarioList.add(usuario2);
        usuarioList.add(usuario2);
        usuarioList.add(usuario2);
        return usuarioList;
    }

    public void cargarListaActividades(){
        List<Activity> listaActivity = listaActividadesHarcoede();

        ActivityAdapter activityAdapter = new ActivityAdapter();
        activityAdapter.setListActivities(listaActivity);
        activityAdapter.setContext(getActivity().getApplicationContext());

        binding.rvActividades.setAdapter(activityAdapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));
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

        List<Usuario> usuarioList = listaUsuariosHarcodeado();

        SolicitudesRegistroAdapter solicitudesRegistroAdapter = new SolicitudesRegistroAdapter();
        solicitudesRegistroAdapter.setUsuarioList(usuarioList);
        solicitudesRegistroAdapter.setContext(getActivity().getApplicationContext());

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_solicitudesRegistro);
        recyclerView.setAdapter(solicitudesRegistroAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomMenuDialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}