package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EventAdapter2 extends RecyclerView.Adapter<com.example.telebeetle.activities.EventAdapter2.EventViewHolder>{

    private List<Evento> listEvents;
    private Context context;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @NonNull
    @Override
    public EventAdapter2.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_events, parent, false);
        return new EventAdapter2.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter2.EventViewHolder holder, int position) {
        ImageView iv= holder.itemView.findViewById(R.id.imageView3);
        iv.setImageResource(R.drawable.baseline_location_on_24);
        ImageView iv2= holder.itemView.findViewById(R.id.imageView4);
        iv2.setImageResource(R.drawable.baseline_more_horiz_24);
        ImageView imageView = holder.itemView.findViewById(R.id.imageView2);
        Evento e = listEvents.get(position);
        holder.evento = e;
        databaseReference = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "usuarios"
        TextView actividadEvent = holder.itemView.findViewById(R.id.actividadOfEvent);
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
            Intent intent = new Intent(context, DetalleActividadGeneralActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Evento", e);
            context.startActivity(intent);
        });
        iv2.setOnClickListener(view -> {
            showOverflowMenu(view, e);
        });

    }

    @Override
    public int getItemCount() {
        return listEvents.size();
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
    private void showOverflowMenu(View v, Evento e) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.opciones_evento, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item selection
                if(item.getItemId() == R.id.menu_item_option1){
                    Intent intent = new Intent(context, EditarEventoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uidEvento", e.getUidEvento());
                    context.startActivity(intent);
                    return true;
                }else if (item.getItemId() == R.id.menu_item_option2){
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
                    databaseReference2.child(e.getUidEvento()).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Borrado de evento exitoso", Toast.LENGTH_SHORT).show();
                                    CometChatApiRest cometChatApiRest = new CometChatApiRest();
                                    cometChatApiRest.deleteGroupInCometChat(e.getUidEvento());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Borrado de evento fallido", Toast.LENGTH_SHORT).show();
                                }
                            });
                    return true;
                }else{
                    return false;
                }
            }
        });

        popupMenu.show();
    }
}