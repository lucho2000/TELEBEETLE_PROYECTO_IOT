package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class EventHorizontalAdapter extends RecyclerView.Adapter<EventHorizontalAdapter.EventoViewHolder>{
    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<Evento> eventoList;
    private Context context;
    DatabaseReference databaseReference;

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_events_horizontal, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        ImageView iv= holder.itemView.findViewById(R.id.imageView3);
        iv.setImageResource(R.drawable.baseline_location_on_24);
        ImageView imageViewActivity = holder.itemView.findViewById(R.id.imageViewEvento);
        Evento e = eventoList.get(position);
        holder.evento = e;
        TextView nombre = holder.itemView.findViewById(R.id.nameActividad);
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
        databaseReference.child(e.getActividad()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Actividad actividad = snapshot.getValue(Actividad.class);
                    Picasso.get().load(actividad.getImagen())
                            .resize(240,120)
                            .transform(new RoundedCornersTransformation(
                                    8,
                                    0
                            ))
                            .into(imageViewActivity);
                    nombre.setText(actividad.getNombreActividad());
                } else {
                    Log.d("User", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", "Failed to read value.", error.toException());
            }
        });

        TextView nombreEvento = holder.itemView.findViewById(R.id.nombreEvent);
        nombreEvento.setText(e.getEtapa());
        TextView fechaHora = holder.itemView.findViewById(R.id.fechaHora);
        fechaHora.setText(e.getFecha() + " " + e.getHora());
        TextView lugar = holder.itemView.findViewById(R.id.lugarEvent);
        lugar.setText(e.getLugar());
        CardView cardView = holder.itemView.findViewById(R.id.card);
        cardView.setOnClickListener(view1 -> {
            if(e.getEstado().equalsIgnoreCase("finalizado")){
                Intent intent = new Intent(context, FinalizadoEventoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Evento", e);
                context.startActivity(intent);
            }else{
                Intent intent = new Intent(context, DetallesEvento1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Evento", e);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
