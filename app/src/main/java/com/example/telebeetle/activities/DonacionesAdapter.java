package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DonacionesAdapter extends RecyclerView.Adapter<DonacionesAdapter.DonacionViewHolder>{

    private List<Donacion> listDonaciones;
    private Context context;

    DatabaseReference databaseReference;

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
        //textViewNombreDonacion.setText(dona.getAsunto());

        TextView textViewFechaYHora = holder.itemView.findViewById(R.id.fechaHora);
        String a = dona.getFecha() ;
        textViewFechaYHora.setText(a);

        TextView textViewDonante = holder.itemView.findViewById(R.id.textView29);
        textViewDonante.setText(dona.getUidDonante());

        TextView textViewPagado = holder.itemView.findViewById(R.id.textView28);
        textViewPagado.setVisibility(View.INVISIBLE);

        ImageView imageView = holder.itemView.findViewById(R.id.imageView8);
        imageView.setImageResource(R.drawable.yy);
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
