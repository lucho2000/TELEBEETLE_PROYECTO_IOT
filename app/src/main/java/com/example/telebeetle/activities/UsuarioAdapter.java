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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{

    private List<Usuario> listaUsuarios;
    private Context context;


    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        Usuario user;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            Button banear = itemView.findViewById(R.id.buttonBanear);
            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir");
            banear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference2 = FirebaseDatabase.getInstance().getReference("usuarios");

                    user.setEnable(false); //si se cambia, ya no deberia entrar, pero se deberia borrar del auth tambien
                    databaseReference2.child(user.getUidUsuario()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("msg-test","enable del usuario: "+ user.getEnable());
                            //user.setEnable(false); //si se cambia, ya no deberia entrar, pero se deberia borrar del auth tambien
                            int position = getBindingAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) { //lo remuevo o le doy al enable false
                                removeItem(position);
                            }

                            Toast.makeText(context, "Usuario borrado de la aplicacion", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "No se pudo borrar al usuario", Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            });

        }
    }


    @NonNull
    @Override
    public UsuarioAdapter.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_listar_usuarios, parent, false);
        return new UsuarioAdapter.UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.UsuarioViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
        holder.user = user;
        ImageView foto = holder.itemView.findViewById(R.id.imageView18);
        Picasso.get().load(user.getProfile()).into(foto);

        TextView textViewNombre = holder.itemView.findViewById(R.id.textView42);
        textViewNombre.setText(user.getNombres() + " " + user.getApellidos());

        TextView textViewCodigo = holder.itemView.findViewById(R.id.textView43);
        textViewCodigo.setText(user.getCodigo());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void removeItem(int position) {
        listaUsuarios.remove(position);
        notifyItemRemoved(position);
    }
}
