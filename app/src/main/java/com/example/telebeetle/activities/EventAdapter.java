package com.example.telebeetle.activities;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Evento> listEvents;
    private Context context;
    DatabaseReference databaseReference;

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_events, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        ImageView iv= holder.itemView.findViewById(R.id.imageView3);
        iv.setImageResource(R.drawable.baseline_location_on_24);
        ImageView imageView = holder.itemView.findViewById(R.id.imageView2);
        Evento e = listEvents.get(position);
        holder.evento = e;
        TextView actividadEvent = holder.itemView.findViewById(R.id.actividadOfEvent);
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
        databaseReference.child(e.getActividad()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Actividad actividad = snapshot.getValue(Actividad.class);
                    Picasso.get().load(actividad.getImagen())
                            .resize(140,140)
                            .transform(new CropCircleTransformation())
                            .into(imageView);
                    actividadEvent.setText(actividad.getNombreActividad());
                } else {
                    Log.d("User", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", "Failed to read value.", error.toException());
            }
        });



        TextView nameEvent = holder.itemView.findViewById(R.id.nameEvento);
        nameEvent.setText(e.getEtapa());

        TextView fechaEvent = holder.itemView.findViewById(R.id.timeEvent);
        fechaEvent.setText(e.getFecha() + " " + e.getHora());

        TextView lugarEvento = holder.itemView.findViewById(R.id.lugarEvent);
        lugarEvento.setText(e.getLugar());

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
        return listEvents.size();
    }

    public void searchDataList(ArrayList<Evento> searchList){

        listEvents = searchList;
        notifyDataSetChanged();

    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        Evento evento;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public List<Evento> getListEvents() {
        return listEvents;
    }

    public void setListEvents(List<Evento> listEvents) {
        this.listEvents = listEvents;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
