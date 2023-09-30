package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;

import java.util.List;

public class DonacionesAdapter extends RecyclerView.Adapter<DonacionesAdapter.DonacionViewHolder>{

    private List<Donacion> listDonaciones;
    private Context context;

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.irv_donaciones, parent, false);
        return new DonacionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        Donacion dona = listDonaciones.get(position);
        holder.donacion = dona;

        TextView textViewNombreDonacion = holder.itemView.findViewById(R.id.donacionAsunto);
        textViewNombreDonacion.setText(dona.getAsunto());

        TextView textViewFechaYHora = holder.itemView.findViewById(R.id.fechaHora);
        textViewFechaYHora.setText(dona.getFecha() + " " + dona.getHora());
    }

    @Override
    public int getItemCount() {
        return listDonaciones.size();
    }


    public class DonacionViewHolder extends RecyclerView.ViewHolder{

        Donacion donacion;
        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public List<Donacion> getListDonaciones() {
        return listDonaciones;
    }

    public void setListDonaciones(List<Donacion> listDonaciones) {
        this.listDonaciones = listDonaciones;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }




}
