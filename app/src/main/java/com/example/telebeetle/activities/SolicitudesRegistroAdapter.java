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
        Button accept = view.findViewById(R.id.accept);
        accept.setOnClickListener(v -> {
            Toast.makeText(this.getContext(), "Aceptado owo", Toast.LENGTH_SHORT).show();
        });
        Button deny = view.findViewById(R.id.deny);
        deny.setOnClickListener(v -> {
            Toast.makeText(this.getContext(), "Denegado owo", Toast.LENGTH_SHORT).show();
        });
        return new SolicitudRegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudRegistroViewHolder holder, int position) {
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.perfilUsuario);
        picasso.load(drawableResourceId)
                .resize(55,55)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        Usuario u = usuarioList.get(position);
        holder.usuario = u;
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
        }
    }
}
