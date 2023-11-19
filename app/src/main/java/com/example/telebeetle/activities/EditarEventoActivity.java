package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityEditarEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class EditarEventoActivity extends AppCompatActivity {

    TextInputLayout textInputLayout, textInputLayoutTimePicker;
    TextInputEditText editTextDatePicker, editTextTime;
    ActivityEditarEventoBinding binding;

    FirebaseDatabase database;
    Evento eventoObjEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //obtener intent con id del evento.
        Intent intent = getIntent();
        String idEvento = intent.getStringExtra("uidEvento");


        editTextDatePicker = binding.editTextDate;
        editTextTime = binding.HoraActividad;

        database = FirebaseDatabase.getInstance();
        rellenarConDataCamposEditarEvento(idEvento);

        textInputLayout = binding.FechaTextField;
        textInputLayoutTimePicker = binding.HoraActividadTextField;
        editTextDatePicker.setEnabled(false);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterialDesignDatePicker();
            }
        });
        editTextTime.setEnabled(false);
        textInputLayoutTimePicker.setEndIconOnClickListener(v -> {
            showTimePicker();
        });

        binding.botonEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarDataEventoFirebase(idEvento);
            }
        });

        binding.botonCancelarEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void showTimePicker(){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(0) // Initial hour value
                .setMinute(0) // Initial minute value
                .setTitleText("Select Time")
                .build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();

                // Create a Calendar instance and set the selected time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                // Create a SimpleDateFormat instance with the desired 12-hour format
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                // Format the selected time
                String selectedTime = dateFormat.format(calendar.getTime());
                // Handle the selected time
                editTextTime.setText(selectedTime);
            }
        });

        materialTimePicker.show(getSupportFragmentManager(), "tag");

    }

    public void showMaterialDesignDatePicker(){

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
                editTextDatePicker.setText(MessageFormat.format("{0}",date));
            }
        });
        materialDatePicker.show(getSupportFragmentManager(),"tag");
    }

    public void rellenarConDataCamposEditarEvento(String idEvento) {
        TextInputEditText nombreEvento = binding.nombreEvento;
        EditText descripcionEvento = binding.editTextDescripcion;
        DatabaseReference eventosData = database.getReference("evento");
        eventosData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equalsIgnoreCase(idEvento)) {
                        eventoObjEditar = dataSnapshot.getValue(Evento.class);
                        if (eventoObjEditar != null) {
                            Log.d("msg-test-firebase",eventoObjEditar.getFecha());
                            editTextDatePicker.setText(eventoObjEditar.getFecha());
                            nombreEvento.setText(eventoObjEditar.getEtapa()); //revisar atributos objeto evento
                            //descripcionEvento.setText(eventoObjEditar.getDescripcion());
                            descripcionEvento.setText(eventoObjEditar.getDescripcion());
                            editTextTime.setText(eventoObjEditar.getHora());
                        } else {
                            //objeto nulo
                            Log.d("msg-test-firebase","objeto evento nulo. no match with ID");
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("msg-test-firebase","Ocurrio un error al obtener evento especifico (con id) en firebase. EditarEventoActivity");
            }
        });
    }

    public void editarDataEventoFirebase(String idEvento) {
        String nombreEvento = binding.nombreEvento.getText().toString();
        String descripcionEvento = binding.editTextDescripcion.getText().toString();
        if(!nombreEvento.isEmpty() && !descripcionEvento.isEmpty()){
            DatabaseReference eventosData = database.getReference("evento");
            HashMap<String, Object> eventoUpdate = new HashMap<>();
            eventoUpdate.put("etapa",nombreEvento);
            eventoUpdate.put("fecha",editTextDatePicker.getText().toString());
            eventoUpdate.put("descripcion", descripcionEvento);
            eventoUpdate.put("hora", editTextTime.getText().toString());
            //eventoUpdate.put("lugar",);

            eventosData.child(idEvento).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        rellenarConDataCamposEditarEvento(idEvento);
                        Toast.makeText(EditarEventoActivity.this,"Success to update event",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditarEventoActivity.this,"Failed to update event",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public void mostrarDateDialog(){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(),"datepicker");
    }

    public void respuestaDateDialog(int year, int month, int day ){
        Log.d("msg-test","year selected: " + year);
        Log.d("msg-test","month selected: " + month);
        Log.d("msg-test","day selected: " + day);
    }*/

}