package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.viewmodels.CrearActivityViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

        if (d.getApellidos() != null){
            textViewNombre.setText(d.getNombres() + " " + d.getApellidos());
        } else {
            textViewNombre.setText(d.getNombres());
        }

        TextView textViewCodigo = holder.itemView.findViewById(R.id.textView43);
        textViewCodigo.setText(d.getCodigo());

        ImageView imageViewIcono =  holder.itemView.findViewById(R.id.imageView20);
        CrearActivityViewModel crearActivityViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CrearActivityViewModel.class);
        ImageView foto = holder.itemView.findViewById(R.id.imageView18);
        Picasso.get().load(d.getProfile()).into(foto);

        AtomicBoolean condicion = new AtomicBoolean(true);
        imageViewIcono.setOnClickListener(view -> {
            if(condicion.get()){
                imageViewIcono.setImageResource(R.drawable.baseline_check_24);
                crearActivityViewModel.getUid().postValue(d.getUidUsuario());
                condicion.set(false);
            }else{
                imageViewIcono.setImageResource(R.drawable.baseline_add_24);
                crearActivityViewModel.getUid().postValue(null);
                condicion.set(true);
            }
        });
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
