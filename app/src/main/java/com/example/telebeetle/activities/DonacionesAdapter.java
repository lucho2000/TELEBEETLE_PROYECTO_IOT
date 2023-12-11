package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

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
        textViewFechaYHora.setText("Fecha: " + a);

        TextView textViewPagado = holder.itemView.findViewById(R.id.textView28);
        textViewPagado.setText("Monto: S./" + dona.getMonto());

        ImageView imageView = holder.itemView.findViewById(R.id.imageView8);
        Picasso.get().load(dona.getImagenCaptura()).into(imageView);



        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailDonationActivity.class);
                //intent.putExtra("imageURL")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("donacion", dona);
                context.startActivity(intent);

            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return listDonaciones.size();
    }


    public class DonacionViewHolder extends RecyclerView.ViewHolder{

        Donacion donacion;
        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);


            LinearLayout cardLayout = itemView.findViewById(R.id.linearLayout2);
            cardLayout.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailDonationActivity2.class);
                    //intent.putExtra("imageURL")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("donacion", donacion);
                    context.startActivity(intent);
                }
            }));

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
