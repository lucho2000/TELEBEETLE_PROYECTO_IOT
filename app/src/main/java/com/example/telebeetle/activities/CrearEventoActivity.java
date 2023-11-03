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

    ImageView seleccionIconoHoraInicial, seleccionIconoFecha;

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

        seleccionIconoHoraInicial = findViewById(R.id.imageView17);

        seleccionIconoFecha = findViewById(R.id.imageView25);

        crearEvento = findViewById(R.id.botonCrearEvento);
        cancelar = findViewById(R.id.botonCancelarCrearEvento);

        //pasarlos a string


        seleccionIconoFecha.setOnClickListener(view -> {
            showDatePicker();
        });
        //para el date picker
        //MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Selecciona la fecha")
               // .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();


        //para la seleccion de hora inicial y final

        seleccionIconoHoraInicial.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting our hour, minute.
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // on below line we are initializing our Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(CrearEventoActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // on below line we are setting selected time
                            // in our text view.
                            horaInicial.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);
            // at last we are calling show to
            // display our time picker dialog.
            timePickerDialog.show();
        });


        crearEvento.setOnClickListener(view -> {

            actividad = editActividad.getText().toString();
            nombreEvento1 = nombreEvento.getText().toString();
            textDescripcion = descripcion.getText().toString();
            fecha = editTextDate.getText().toString();


            if ( !actividad.isEmpty() && !nombreEvento1.isEmpty() && !textDescripcion.isEmpty() && !fecha.isEmpty() && !horaInicial.getText().toString().isEmpty() ) {

                Evento evento = new Evento();
                evento.setActividad(actividad);
                evento.setEtapa(nombreEvento1);
                evento.setDescripcion(textDescripcion);
                evento.setFecha(fecha);
                evento.setLugar("Polideportivo");
                evento.setHora(horaInicial.getText().toString());//combobox: polideportivo o cancha de minas

                databaseReference = FirebaseDatabase.getInstance().getReference("evento");
                databaseReference.push().setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            editActividad.setText("");
                            editTextDate.setText("");
                            nombreEvento.setText("");
                            descripcion.setText("");
                            horaInicial.setText("");
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
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(CrearEventoActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
        }

    }