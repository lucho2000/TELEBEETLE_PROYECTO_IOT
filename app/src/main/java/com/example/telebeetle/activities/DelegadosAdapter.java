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

public class DelegadosAdapter extends RecyclerView.Adapter<DelegadosAdapter.DelegadoViewHolder>{

    private List<Usuario> listaDelegados;
    private Context context;

    @NonNull
    @Override
    public DelegadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_escogerdelegado, parent, false);
        return new DelegadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DelegadoViewHolder holder, int position) {

        Usuario d = listaDelegados.get(position);
        holder.delegado = d;
        //holder.delegado.getNombre();

        TextView textViewNombre = holder.itemView.findViewById(R.id.textView42);
        textViewNombre.setText(d.getNombres() + " " + d.getApellidos());

        TextView textViewCodigo = holder.itemView.findViewById(R.id.textView43);
        textViewCodigo.setText(d.getCodigo());


    }

    @Override
    public int getItemCount() {
        return listaDelegados.size();
    }

    public class DelegadoViewHolder extends RecyclerView.ViewHolder{

        Usuario delegado;
        public DelegadoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public List<Usuario> getListaDelegados() {
        return listaDelegados;
    }

    public void setListaDelegados(List<Usuario> listaDelegados) {
        this.listaDelegados = listaDelegados;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
