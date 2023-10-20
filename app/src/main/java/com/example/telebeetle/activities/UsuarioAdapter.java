package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{

    private List<Usuario> listaUsuarios;
    private Context context;

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        Usuario user;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public UsuarioAdapter.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_listar_usuarios, parent, false);
        return new UsuarioAdapter.UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.UsuarioViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
        holder.user = user;

        TextView textViewNombre = holder.itemView.findViewById(R.id.textView42);
        textViewNombre.setText(user.getNombres() + " " + user.getApellidos());

        TextView textViewCodigo = holder.itemView.findViewById(R.id.textView43);
        textViewCodigo.setText(user.getCodigo());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
