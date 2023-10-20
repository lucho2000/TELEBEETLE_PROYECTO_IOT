package com.example.telebeetle.activities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Evento> listEvents;
    private Context context;

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_events, parent, false);
        CardView cardView = view.findViewById(R.id.card);
        cardView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, DetallesEvento1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

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
}
