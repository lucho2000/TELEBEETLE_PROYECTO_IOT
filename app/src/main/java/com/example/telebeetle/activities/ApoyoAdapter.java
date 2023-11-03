package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Apoyo;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ApoyoAdapter extends RecyclerView.Adapter<ApoyoAdapter.ApoyoViewHolder>   {

    private List<Usuario> listApoyo;

    private Context context;



    @NonNull
    @Override
    public ApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_apoyo, parent, false);
        return new ApoyoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApoyoViewHolder holder, int position) {
        Usuario usuario = listApoyo.get(position);
        holder.usuario = usuario;


        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.fotoperfil);
        picasso.load(drawableResourceId)
                .resize(75,75)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        TextView nameApoyo = holder.itemView.findViewById(R.id.nombre);
        nameApoyo.setText(usuario.getNombres() + " " + usuario.getApellidos());
        TextView codigoApoyo = holder.itemView.findViewById(R.id.codigoApoyo);
        codigoApoyo.setText(usuario.getCodigo());

    }

    @Override
    public int getItemCount() {
        return listApoyo.size();
    }


    public class ApoyoViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;

        public ApoyoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public List<Usuario> getListApoyo() {
        return listApoyo;
    }

    public void setListApoyo(List<Usuario> listApoyo) {
        this.listApoyo = listApoyo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



}
