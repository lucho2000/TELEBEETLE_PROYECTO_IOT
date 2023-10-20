package com.example.telebeetle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {

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
}
