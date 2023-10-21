package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CrearEventoActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutDatePicker;

    EditText editActividad, nombreEvento, descripcion, textFecha;
    TextInputEditText editTextDate;

    TextView horaInicial;

    ImageView fotoEvento;

    int hour, minute;

    DatabaseReference databaseReference;

    Button crearEvento, cancelar;

    String actividad, nombreEvento1, textDescripcion, maximoBarra, maximoParticipantes, fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        textInputLayoutDatePicker = findViewById(R.id.FechaTextField);
        editTextDate = findViewById(R.id.editTextDate); //fecha
        editActividad = findViewById(R.id.nombreActividad); //actividad debe jalarse de la base de datos, y luego dentro se crea el evento
        nombreEvento = findViewById(R.id.nombreEvento);
        descripcion = findViewById(R.id.editTextComentario);
        //textFecha = findViewById(R.id.editTextDate);

        horaInicial = findViewById(R.id.textViewHoraInicial);

        crearEvento = findViewById(R.id.botonCrearEvento);
        cancelar = findViewById(R.id.botonCancelarCrearEvento);

        //pasarlos a string


        textInputLayoutDatePicker.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        //para el date picker
        //MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Selecciona la fecha")
               // .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();


        //datePicker.show(supportFragmentManager, "tag");



        crearEvento.setOnClickListener(view -> {

            actividad = editActividad.getText().toString();
            nombreEvento1 = nombreEvento.getText().toString();
            textDescripcion = descripcion.getText().toString();
            fecha = editTextDate.getText().toString();

            if ( !actividad.isEmpty() && !nombreEvento1.isEmpty() && !textDescripcion.isEmpty() && !fecha.isEmpty() ) {

                Evento evento = new Evento();
                evento.setActividad(actividad);
                evento.setEtapa(nombreEvento1);
                evento.setDescripcion(textDescripcion);
                evento.setFecha(fecha);
                evento.setLugar("Polideportivo"); //combobox: polideportivo o cancha de minas

                databaseReference = FirebaseDatabase.getInstance().getReference("evento");
                databaseReference.push().setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            editActividad.setText("");
                            editTextDate.setText("");
                            nombreEvento.setText("");
                            descripcion.setText("");
                            Toast.makeText(CrearEventoActivity.this, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }


        });

        cancelar.setOnClickListener(view -> {
            finish();
        });


    }


    public void showDatePicker() {
        MaterialDatePicker<Long> materialDatePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccione fecha del evento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); //importante para cuando seleccionas una fecha, no devuelva la fecha anterior durante el formateo

                String date = dateFormat.format(new Date(selection));
                //String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).setTimeZone(TimeZone.getDefault()).format(new Date(selection));
                editTextDate.setText(MessageFormat.format("{0}",date));
            }
        });
        materialDatePicker.show(getSupportFragmentManager(),"tag");
    }

}