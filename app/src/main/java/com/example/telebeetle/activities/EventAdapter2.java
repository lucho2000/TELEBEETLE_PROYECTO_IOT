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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EventAdapter2 extends RecyclerView.Adapter<com.example.telebeetle.activities.EventAdapter2.EventViewHolder>{

    private List<Evento> listEvents;
    private Context context;

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
        int drawableResourceId = R.drawable.telito;
        Picasso picasso = Picasso.get();
        ImageView imageView = holder.itemView.findViewById(R.id.imageView2);
        picasso.load(drawableResourceId)
                .resize(140,140)
                .transform(new CropCircleTransformation())
                .into(imageView);

        Evento e = listEvents.get(position);
        holder.evento = e;

        TextView actividadEvent = holder.itemView.findViewById(R.id.actividadOfEvent);
        actividadEvent.setText(e.getActividad());

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
                    return true;
                }else{
                    return false;
                }
            }
        });

        popupMenu.show();
    }
}