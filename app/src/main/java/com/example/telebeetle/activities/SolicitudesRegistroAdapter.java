package com.example.telebeetle.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SolicitudesRegistroAdapter extends RecyclerView.Adapter<SolicitudesRegistroAdapter.SolicitudRegistroViewHolder>{
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<Usuario> usuarioList;
    private Context context;

    @NonNull
    @Override
    public SolicitudRegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_solicitudes, parent, false);
        return new SolicitudRegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudRegistroViewHolder holder, int position) {
        Usuario u = usuarioList.get(position);
        holder.usuario = u;
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.perfilUsuario);
        Picasso.get().load(u.getProfile())
                .resize(55,55)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        TextView nombre = holder.itemView.findViewById(R.id.nombreUsuario);
        nombre.setText(u.getNombres() + " " + u.getApellidos());
        TextView codigo = holder.itemView.findViewById(R.id.codigo);
        codigo.setText(u.getCodigo());
        TextView correo = holder.itemView.findViewById(R.id.correoPucp);
        correo.setText(u.getCorreo());
        TextView condicion = holder.itemView.findViewById(R.id.condicionEstudio);
        condicion.setText(u.getCondicion());
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class SolicitudRegistroViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public SolicitudRegistroViewHolder(@NonNull View itemView){
            super(itemView);
            Button accept = itemView.findViewById(R.id.accept);
            accept.setOnClickListener(v -> {
                Toast.makeText(context, "Aceptado owo", Toast.LENGTH_SHORT).show();
            });
            Button deny = itemView.findViewById(R.id.deny);
            deny.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Remove the item from the dataset and update the RecyclerView
                    removeItem(position);
                }
            });

        }
    }
    public void removeItem(int position) {
        usuarioList.remove(position);
        notifyItemRemoved(position);
    }
}
