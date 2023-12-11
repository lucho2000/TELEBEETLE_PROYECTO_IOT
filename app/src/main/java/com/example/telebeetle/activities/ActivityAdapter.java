package com.example.telebeetle.activities;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.fragments.OpcionApoyando;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;

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
        TextView nombre = holder.itemView.findViewById(R.id.nameActividad);
        nombre.setText(a.getNombreActividad());
        TextView delegado = holder.itemView.findViewById(R.id.nameDelegado);
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "usuarios"
        databaseReference.child(a.getDelegado()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    ImageView imageViewDelegado = holder.itemView.findViewById(R.id.imageViewDelegado);
                    Picasso.get().load(user.getProfile())
                            .resize(75,75)
                            .transform(new CropCircleTransformation())
                            .into(imageViewDelegado);
                    delegado.setText(user.getNombres() + " " + user.getApellidos());
                } else {
                    Log.d("User", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", "Failed to read value.", error.toException());
            }
        });
        ImageView iv= holder.itemView.findViewById(R.id.more);
        iv.setImageResource(R.drawable.baseline_more_horiz_24);
        iv.setOnClickListener(v -> {
            showOverflowMenu(v, a);
        });
        ImageView imageViewActivity = holder.itemView.findViewById(R.id.imageViewActivity);
        Picasso.get().load(a.getImagen())
                //.resize(240,120)
                //.transform(new RoundedCornersTransformation(8,0))
                .into(imageViewActivity);


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
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.opciones_evento, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_item_option1){
                    Intent intent = new Intent(context,EditarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uidActividad", a.getUidActividad());
                    context.startActivity(intent);
                    return true;
                }else if (item.getItemId() == R.id.menu_item_option2){
                    //showDialog(a.getNombreActividad());
                    final boolean[] wenas = {false};
                    databaseReference3 = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                Evento evento1 = dataSnapshot.getValue(Evento.class);
                                if(evento1.getActividad().equals(a.getUidActividad())){
                                    wenas[0] = true;
                                    break;
                                }
                            }
                            if(wenas[0]){
                                Toast.makeText(context, "No es posible borrar la actividad, pues cuenta con eventos vinculados", Toast.LENGTH_LONG).show();
                            }else{
                                databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "evento"
                                databaseReference2.child(a.getUidActividad()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "usuarios"
                                                databaseReference.child(a.getDelegado()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            Usuario user = snapshot.getValue(Usuario.class);
                                                            HashMap<String, Object> eventoUpdate = new HashMap<>();
                                                            eventoUpdate.put("rol", "usuario");
                                                            databaseReference.child(snapshot.getKey()).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(context, "Borrado de actividad exitoso", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Log.d("User", "User not found");
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.d("User", "Failed to read value.", error.toException());
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Borrado de actividad fallido", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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
    private void showDialog(String nombre){
        new MaterialAlertDialogBuilder(context, R.style.Base_Theme_TeleBeetle)
                .setTitle("Borrar Actividad")
                .setMessage("Se borrará la actividad " + nombre)
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

}
