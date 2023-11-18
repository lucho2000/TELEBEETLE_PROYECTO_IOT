package com.example.telebeetle.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SolicitudesRegistroAdapter extends RecyclerView.Adapter<SolicitudesRegistroAdapter.SolicitudRegistroViewHolder>{
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<Usuario> usuarioList;
    private Context context;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @NonNull
    @Override
    public SolicitudRegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_solicitudes, parent, false);
        return new SolicitudRegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudRegistroViewHolder holder, int position) {
        Usuario u = usuarioList.get(position);
        holder.usuario = u;
        ImageView imageViewDelegado = holder.itemView.findViewById(R.id.perfilUsuario);
        Picasso.get().load(u.getProfile())
                .resize(55,55)
                .transform(new CropCircleTransformation())
                .into(imageViewDelegado);
        TextView nombre = holder.itemView.findViewById(R.id.nombreUsuario);
        nombre.setText(u.getNombres() + " " + u.getApellidos());
        TextView codigo = holder.itemView.findViewById(R.id.codigo);
        codigo.setText(u.getCodigo());
        TextView correo = holder.itemView.findViewById(R.id.correoPucp);
        correo.setText(u.getCorreo());
        TextView condicion = holder.itemView.findViewById(R.id.condicionEstudio);
        condicion.setText(u.getCondicion());
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class SolicitudRegistroViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public SolicitudRegistroViewHolder(@NonNull View itemView){
            super(itemView);
            Button accept = itemView.findViewById(R.id.accept);
            databaseReference2 = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir"); //datos de firebase de la coleccion de "evento"
            accept.setOnClickListener(v -> {
                databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "evento"
                databaseReference.child(usuario.getUidUsuario()).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //una vez aceptado su solicitud para ser user de nuestra app, se le aniade como usuario en la api de chat
                        CometChatApiRest cometChatApiRest = new CometChatApiRest();
                        cometChatApiRest.createUserinCometChat(usuario.getUidUsuario(),usuario.getNombres() + " " + usuario.getApellidos());

                        databaseReference2.child(usuario.getUidUsuario()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Exito al aceptar solicitud de usuario", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fallo al aceptar solicitud de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            Button deny = itemView.findViewById(R.id.deny);
            deny.setOnClickListener(v -> {
                databaseReference2.child(usuario.getUidUsuario()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Exito al denegar solicitud de usuario", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fallo al denegar solicitud de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
                //int position = getBindingAdapterPosition();
                //if (position != RecyclerView.NO_POSITION) {
                //    removeItem(position);
                //}
            });
        }
    }
    public void removeItem(int position) {
        usuarioList.remove(position);
        notifyItemRemoved(position);
    }
}