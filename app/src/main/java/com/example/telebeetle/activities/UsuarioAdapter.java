package com.example.telebeetle.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    FirebaseDatabase database;

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        Usuario user;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
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

        database =  FirebaseDatabase.getInstance();

        Usuario user = listaUsuarios.get(position);
        holder.user = user;
        ImageView foto = holder.itemView.findViewById(R.id.imageView18);
        Picasso.get().load(user.getProfile()).into(foto);

        TextView textViewNombre = holder.itemView.findViewById(R.id.textView42);
        textViewNombre.setText(user.getNombres() + " " + user.getApellidos());

        TextView textViewCodigo = holder.itemView.findViewById(R.id.textView43);
        textViewCodigo.setText(user.getCodigo());


        ImageView ban = holder.itemView.findViewById(R.id.bannn);
        ban.setOnClickListener(view -> {
            user.setEnable(false);
            buttonSendEmail(user.getCorreo());
            Intent intent = new Intent(getContext(),GeneralViewActivity.class);
            getContext().startActivity(intent);

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
            mimeMessage.setText("Hola Usuario, \n\nUsted ha sido baneado de la apliacion por incumplir las normas. \n\n Telebeetle");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        Toast.makeText(getContext(),"Correo enviado",Toast.LENGTH_SHORT).show();
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
