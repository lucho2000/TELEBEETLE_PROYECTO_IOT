package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Apoyo;
import com.example.telebeetle.R;

import java.util.List;

public class ApoyoAdapter extends RecyclerView.Adapter<ApoyoAdapter.ApoyoViewHolder>   {

    private List<Apoyo> listApoyo;

    private Context context;

    public List<Apoyo> getListApoyo() {
        return listApoyo;
    }

    public void setListApoyo(List<Apoyo> listApoyo) {
        this.listApoyo = listApoyo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_apoyo, parent, false);
        return new ApoyoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApoyoViewHolder holder, int position) {
        Apoyo apoyo = listApoyo.get(position);
        holder.apoyo = apoyo;


        TextView nameApoyo = holder.itemView.findViewById(R.id.apoyoName);
        nameApoyo.setText(apoyo.getNombre());
        TextView codigoApoyo = holder.itemView.findViewById(R.id.codigoApoyo);
        codigoApoyo.setText(apoyo.getCodigo());

    }

    @Override
    public int getItemCount() {
        return listApoyo.size();
    }


    public class ApoyoViewHolder extends RecyclerView.ViewHolder{
        Apoyo apoyo;

        public ApoyoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }






}
