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

import com.example.telebeetle.Entity.Activity;
import com.example.telebeetle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    public List<Activity> getListActivities() {
        return listActivities;
    }

    public void setListActivities(List<Activity> listActivities) {
        this.listActivities = listActivities;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<Activity> listActivities;
    private Context context;

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_activities_rv_general, parent, false);
        ImageView more = view.findViewById(R.id.more);
        more.setOnClickListener(this::showOverflowMenu);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ImageView iv= holder.itemView.findViewById(R.id.more);
        iv.setImageResource(R.drawable.baseline_more_horiz_24);
        int drawableResourceId = R.drawable.juiocesaraliagamachuca;
        Picasso picasso = Picasso.get();
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.imageViewDelegado);
        picasso.load(drawableResourceId)
                .resize(75,75)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        int drawableResourceId2 = R.drawable.voley;
        Picasso picasso2 = Picasso.get();
        ImageView imageViewActivity = holder.itemView.findViewById(R.id.imageViewActivity);
        picasso2.load(drawableResourceId2)
                .resize(240,120)
                .transform(new RoundedCornersTransformation(
                        8,
                        0
                ))
                .into(imageViewActivity);
        Activity a = listActivities.get(position);
        holder.activity = a;

        TextView nombre = holder.itemView.findViewById(R.id.nameActividad);
        nombre.setText(a.getNombre());
        TextView delegado = holder.itemView.findViewById(R.id.nameDelegado);
        delegado.setText(a.getDelegado());

    }

    @Override
    public int getItemCount() {
        return listActivities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder{
        Activity activity;
        public ActivityViewHolder(@NonNull View itemView){
            super((itemView));
        }
    }

    private void showOverflowMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.opciones_evento, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item selection
                if(item.getItemId() == R.id.menu_item_option1){
                    Intent intent = new Intent(context,EditarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
