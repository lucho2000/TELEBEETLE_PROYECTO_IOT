package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ValidarDonacionesAdapter extends RecyclerView.Adapter<ValidarDonacionesAdapter.ValidarDonacionesViewHolder>{
    private List<Donacion> listaValidarDonaciones;

    public List<Donacion> getListaValidarDonaciones() {
        return listaValidarDonaciones;
    }

    public void setListaValidarDonaciones(List<Donacion> listaValidarDonaciones) {
        this.listaValidarDonaciones = listaValidarDonaciones;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    TextView donante;

    @NonNull
    @Override
    public ValidarDonacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_vallidar_donaciones, parent, false);

        return new ValidarDonacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValidarDonacionesViewHolder holder, int position) {
        Donacion d = listaValidarDonaciones.get(position);
        holder.donacion = d;
        int drawableResourceId = R.drawable.yape;
        Picasso picasso = Picasso.get();
        ImageView imageViewDonacion = holder.itemView.findViewById(R.id.fotoYape);
        picasso.load(d.getImagenCaptura())
                .resize(120,150)
                .into(imageViewDonacion);
        TextView fecha = holder.itemView.findViewById(R.id.fechaDonacion);
        //TextView donante = holder.itemView.findViewById(R.id.donante);
        TextView monto = holder.itemView.findViewById(R.id.monto);
        fecha.setText("Fecha: " + d.getFecha());
        //donante.setText("Donante: " + d.getUidDonante());
        monto.setText("Monto: S./ " + d.getMonto());

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("donaciones_por_validar");
        Query emailquery = databaseReference.orderByChild("uidUsuario").equalTo(d.getUidDonante());

        emailquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombres, apellidos;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                         nombres = userSnapshot.child("nombres").getValue(String.class);
                         apellidos = userSnapshot.child("apellidos").getValue(String.class);
                        donante = holder.itemView.findViewById(R.id.donante);
                        donante.setText("Donante: " + nombres + " " +apellidos);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("msg-test","error get donante");
            }
        });

        /*
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String keyDonacion = dataSnapshot.getKey();
                    Donacion donacion = dataSnapshot.getValue(Donacion.class);

                    databaseReference.child(donacion.getUidDonante()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String nombres = snapshot.child("nombres").getValue(String.class);
                            String apellidos = snapshot.child("apellidos").getValue(String.class);
                            donante = holder.itemView.findViewById(R.id.donante);
                            donante.setText("Donante: " + nombres + " " +apellidos);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listaValidarDonaciones.size();
    }

    public class ValidarDonacionesViewHolder extends RecyclerView.ViewHolder{
        Donacion donacion;
        public ValidarDonacionesViewHolder(@NonNull View itemView){
            super(itemView);
            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

//            databaseReference.child(donacion.getUidDonante()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        Usuario user = snapshot.getValue(Usuario.class);
//                        TextView donante = itemView.findViewById(R.id.donante);
//                        donante.setText("Donante: " + user.getNombres() + " " +user.getApellidos());
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });


            databaseReference2 = FirebaseDatabase.getInstance().getReference("donaciones_por_validar");
            //Button validar = itemView.findViewById(R.id.validar);
            //Button invalidar = itemView.findViewById(R.id.invalidar);

//            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    donaciones.clear();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        String keyDonacion = dataSnapshot.getKey();
//                        Donacion donacion = dataSnapshot.getValue(Donacion.class);
//
//                        databaseReference.child(donacion.getUidDonante()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String nombres = snapshot.child("nombres").getValue(String.class);
//                                String apellidos = snapshot.child("apellidos").getValue(String.class);
//                                TextView donante = itemView.findViewById(R.id.donante);
//                                donante.setText("Donante: " + nombres + " " +apellidos);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            LinearLayout cardLayout = itemView.findViewById(R.id.cardd);
            cardLayout.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailDonationActivity.class);
                    //intent.putExtra("imageURL")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("donacion", donacion);
                    context.startActivity(intent);
                }
            }));

            /*
            validar.setOnClickListener(v -> {

                databaseReference = FirebaseDatabase.getInstance().getReference("donaciones");
                donacion.setAccepted(true);
                databaseReference.child(donacion.getKeyDonacion()).setValue(donacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference2.child(donacion.getKeyDonacion()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Donacion aceptada", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                //Toast.makeText(this.getContext(), "Boton pa validar", Toast.LENGTH_SHORT).show();


            });


            invalidar.setOnClickListener(v -> {
                //Toast.makeText(this.getContext(), "Boton pa invalidar", Toast.LENGTH_SHORT).show();
                databaseReference2.child(donacion.getKeyDonacion()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Solicitud denegada correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
            });*/

        }
    }
}
