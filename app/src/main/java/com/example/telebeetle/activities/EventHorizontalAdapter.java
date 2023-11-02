package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        int drawableResourceId2 = R.drawable.voley;
        Picasso picasso2 = Picasso.get();
        ImageView imageViewActivity = holder.itemView.findViewById(R.id.imageViewEvento);
        picasso2.load(drawableResourceId2)
                .resize(240,120)
                .transform(new RoundedCornersTransformation(
                        8,
                        0
                ))
                .into(imageViewActivity);
        Evento e = eventoList.get(position);
        holder.evento = e;

        TextView nombre = holder.itemView.findViewById(R.id.nameActividad);
        nombre.setText(e.getActividad());
        TextView nombreEvento = holder.itemView.findViewById(R.id.nombreEvent);
        nombreEvento.setText(e.getEtapa());
        TextView fechaHora = holder.itemView.findViewById(R.id.fechaHora);
        fechaHora.setText(e.getFecha() + " " + e.getHora());
        TextView lugar = holder.itemView.findViewById(R.id.lugarEvent);
        lugar.setText(e.getLugar());
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
