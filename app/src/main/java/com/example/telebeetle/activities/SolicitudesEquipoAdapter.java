package com.example.telebeetle.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import com.example.telebeetle.fragments.EsperaParticipante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SolicitudesEquipoAdapter extends RecyclerView.Adapter<SolicitudesEquipoAdapter.SolicitudEquipoViewHolder>{

    CometChatApiRest cometChatApiRest = new CometChatApiRest();
    public HashMap<String,String> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(HashMap<String, String> participantes) {
        this.participantes = participantes;
    }

    public String getEventoUID() {
        return eventoUID;
    }

    public void setEventoUID(String eventoUID) {
        this.eventoUID = eventoUID;
    }

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

    private HashMap<String,String> participantes;

    public HashMap<String, String> getNel() {
        return nel;
    }

    public void setNel(HashMap<String, String> nel) {
        this.nel = nel;
    }

    private HashMap<String, String> nel;
    private String eventoUID;
    private List<Usuario> usuarioList;
    private Context context;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @NonNull
    @Override
    public SolicitudesEquipoAdapter.SolicitudEquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_solicitudes, parent, false);
        return new SolicitudEquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesEquipoAdapter.SolicitudEquipoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Usuario u = usuarioList.get(position);
        final String[] keyUser = {null};
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
        Button accept = holder.itemView.findViewById(R.id.accept);
        accept.setOnClickListener(v -> {
            String text = "Estimado usuario "+u.getNombres()+" "+u.getApellidos()+". Se ha aceptado su solicitud para unirse al equipo";
            Set<String> keys = nel.keySet();
            for (String key : keys) {
                if(!key.equalsIgnoreCase("ga") && nel.get(key).equals(u.getUidUsuario())){ //validando
                    keyUser[0] = key;
                    break;
                }
            }
            nel.remove(keyUser[0]);
            participantes.put(UUID.randomUUID().toString(), u.getUidUsuario());
            HashMap<String, Object> eventoUpdate = new HashMap<>();
            eventoUpdate.put("listaApoyosParticipantesValidados", participantes);
            eventoUpdate.put("listaApoyosParticipantes", nel);
            //removiendo al usuario de la lista de barras del evento
            databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
            databaseReference.child(eventoUID).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addUsertoEventoGroupCometChat(eventoUID, u.getUidUsuario());
                    buttonSendEmail(u.getCorreo(),text);
                    Toast.makeText(context, "Exito al aceptar solicitud de participante", Toast.LENGTH_SHORT).show();
                    removeItem(position);
                }
            });
        });
        Button deny = holder.itemView.findViewById(R.id.deny);
        deny.setOnClickListener(v -> {
            String text = "Estimado usuario "+u.getNombres()+" "+u.getApellidos()+".Su solicitud para unirse al equipo ha sido denegada";
            Set<String> keys = nel.keySet();
            for (String key : keys) {
                if(!key.equalsIgnoreCase("ga") && nel.get(key).equals(u.getUidUsuario())){ //validando
                    keyUser[0] = key;
                    break;
                }
            }
            nel.remove(keyUser[0]);
            HashMap<String, Object> eventoUpdate = new HashMap<>();
            eventoUpdate.put("listaApoyosParticipantes", nel);
            //removiendo al usuario de la lista de barras del evento
            databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
            databaseReference.child(eventoUID).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    buttonSendEmail(u.getCorreo(),text);
                    Toast.makeText(context, "Exito al denegar solicitud de participante", Toast.LENGTH_SHORT).show();
                    removeItem(position);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }


    public class SolicitudEquipoViewHolder extends  RecyclerView.ViewHolder{
        Usuario usuario;
        public SolicitudEquipoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
    public void removeItem(int position) {
        usuarioList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    public void addUsertoEventoGroupCometChat(String evento_uid, String userid) {
        ArrayList<String> listaUsersCometChat = new ArrayList<>();
        listaUsersCometChat.add(userid);
        String[] listaUsersCometChatArreglo = listaUsersCometChat.toArray(new String[0]);
        //String listaUsersCometChatString = Arrays.toString(listaUsersCometChatArreglo);
        // Crear una cadena con corchetes y comillas
        String listaUsersCometChatString = "[" + String.join(", ", Arrays.stream(listaUsersCometChatArreglo).map(s -> "\"" + s + "\"").toArray(String[]::new)) + "]";
        Log.d("msg_test","addUsertoEventoGroupCometChat");
        cometChatApiRest.addMemberToGroupEventCometChat(evento_uid,listaUsersCometChatString);
    }
    public void buttonSendEmail(String correoaEnviar,String text){

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

            mimeMessage.setSubject("Solicitud Respondida");
            mimeMessage.setText(text);

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
