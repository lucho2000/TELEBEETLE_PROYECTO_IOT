package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{

    private List<Usuario> listaUsuarios;
    private Context context;


    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;


    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        Usuario user;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
           /* Button banear = itemView.findViewById(R.id.bann);
            //databaseReference = FirebaseDatabase.getInstance().getReference("usuarios_por_admitir");
            banear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference2 = FirebaseDatabase.getInstance().getReference("usuarios");

                    user.setEnable(false); //si se cambia, ya no deberia entrar
                    databaseReference2.child(user.getUidUsuario()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("msg-test","enable del usuario: "+ user.getEnable());

                            int position = getBindingAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) { //lo remuevo, pero se vuelve a cargar lo mismo de nuevo
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
            */
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


        //ImageView ban = holder.itemView.findViewById(R.id.bannn);
        Button banear = holder.itemView.findViewById(R.id.banear);

        banear.setOnClickListener(view -> {

            databaseReference =  FirebaseDatabase.getInstance().getReference("usuarios");
            user.setEnable(false);
            databaseReference.child(user.getUidUsuario()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

//                    int position = holder.getBindingAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) { //lo remuevo, pero se vuelve a cargar lo mismo de nuevo
//                        //removeItem(position);
//                    }

                    Toast.makeText(context, "Usuario baneado de la aplicacion", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "No se pudo borrar al usuario", Toast.LENGTH_SHORT).show();
                }
            });

            buttonSendEmail(user.getCorreo());

            //Intent intent = new Intent(getContext(),GeneralViewActivity.class);
            //getContext().startActivity(intent);


        });



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

    public void buttonSendEmail(String correoaEnviar){

        try {
            String stringSenderEmail = "telesystemclinic@gmail.com";
            String stringReceiverEmail = correoaEnviar;
            String stringPasswordSenderEmail = "qyxm eonv gmql dqak";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Baneado(a) de TeleBeetle");
            mimeMessage.setText("Hola Usuario, \n\nUsted ha sido baneado de la aplicacion por incumplir las normas. \n\n Telebeetle");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        //Toast.makeText(getContext(),"Correo enviado",Toast.LENGTH_SHORT).show();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        Log.d("msg-test", String.valueOf(e));
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
            Log.d("msg-test", String.valueOf(e));
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.d("msg-test", String.valueOf(e));
        }
    }


}
