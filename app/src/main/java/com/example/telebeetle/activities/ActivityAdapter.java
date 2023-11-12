package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    public List<Actividad> getListActivities() {
        return listActivities;
    }

    public void setListActivities(List<Actividad> listActivities) {
        this.listActivities = listActivities;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<Actividad> listActivities;
    private Context context;

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_activities_rv_general, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Actividad a = listActivities.get(position);
        holder.activity = a;
        ImageView iv= holder.itemView.findViewById(R.id.more);
        iv.setImageResource(R.drawable.baseline_more_horiz_24);
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.imageViewDelegado);
        picasso.load(drawableResourceId)
                .resize(75,75)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        ImageView imageViewActivity = holder.itemView.findViewById(R.id.imageViewActivity);
        Picasso.get().load(a.getImagen())
                //.resize(240,120)
                //.transform(new RoundedCornersTransformation(8,0))
                .into(imageViewActivity);

        TextView nombre = holder.itemView.findViewById(R.id.nameActividad);
        nombre.setText(a.getNombreActividad());
        TextView delegado = holder.itemView.findViewById(R.id.nameDelegado);
        delegado.setText(a.getDelegado());

        iv.setOnClickListener(v -> {
            showOverflowMenu(v, a);
        });
    }

    @Override
    public int getItemCount() {
        return listActivities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder{
        Actividad activity;
        public ActivityViewHolder(@NonNull View itemView){
            super((itemView));
        }
    }

    private void showOverflowMenu(View v, Actividad a) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.opciones_evento, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item selection
                if(item.getItemId() == R.id.menu_item_option1){
                    Intent intent = new Intent(context,EditarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uidActividad", a.getUidActividad());
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
