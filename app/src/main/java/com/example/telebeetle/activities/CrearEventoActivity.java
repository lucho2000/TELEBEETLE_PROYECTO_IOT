package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CrearEventoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    TextInputLayout textInputLayoutDatePicker, textInputLayoutTimePicker;

    EditText editActividad, nombreEvento, descripcion, textFecha, nombreLugar;
    TextInputEditText editTextDate, editTextTime;



    int hour, minute, arch;
    NumberPicker participantes;

    DatabaseReference databaseReference;

    Button crearEvento, cancelar;

    String actividad, nombreEvento1, textDescripcion, maximoBarra, fecha, lugar, horaa;
    String maximoParticipantes="1";
    String latitud = "-12.068585769932369";

    String longitud = "-77.0781371431298";

    GoogleMap mMap;

    ArrayList<String> actividades = new ArrayList<>();
    ArrayList<String> uidActividades = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad");
        actividades.clear();
        uidActividades.clear();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Actividad actividad1 = dataSnapshot.getValue(Actividad.class);
                    actividades.add(actividad1.getNombreActividad());
                    uidActividades.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        AutoCompleteTextView autoCompleteTextView =  findViewById(R.id.NombreActividadTextField);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, actividades);
        autoCompleteTextView.setAdapter(adapter);
        textInputLayoutDatePicker = findViewById(R.id.FechaTextField);
        textInputLayoutTimePicker = findViewById(R.id.HoraActividadTextField);
        participantes = findViewById(R.id.numberPicker2);
        participantes.setMinValue(1);
        participantes.setMaxValue(100);
        participantes.setValue(1);
        participantes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                arch = participantes.getValue();
            }
        });
        nombreLugar = findViewById(R.id.LugarActividad);
        editTextDate = findViewById(R.id.editTextDate); //fecha
        nombreEvento = findViewById(R.id.nombreEvento);
        descripcion = findViewById(R.id.editTextComentario);
        editTextTime = findViewById(R.id.HoraActividad);
        //textFecha = findViewById(R.id.editTextDate);




        crearEvento = findViewById(R.id.botonCrearEvento);
        cancelar = findViewById(R.id.botonCancelarCrearEvento);

        //pasarlos a string

        editTextDate.setEnabled(false);
        textInputLayoutDatePicker.setEndIconOnClickListener(view -> {
            showDatePicker();
        });
        //para el date picker
        //MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Selecciona la fecha")
               // .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();


        //para la seleccion de hora inicial y final

        editTextTime.setEnabled(false);
        textInputLayoutTimePicker.setEndIconOnClickListener(view -> {
            showTimePicker();
        });

        NestedScrollView scroll = (NestedScrollView) findViewById(R.id.scroll);
        MySupportMapFragment mSupportMapFragment;
        mSupportMapFragment = (MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaLugarActividad);
        if(mSupportMapFragment != null)
            mSupportMapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scroll.requestDisallowInterceptTouchEvent(true);
                }
            });
        assert mSupportMapFragment != null;
        mSupportMapFragment.getMapAsync(this);

        crearEvento.setOnClickListener(view -> {
            maximoParticipantes = String.valueOf(arch);
            actividad = autoCompleteTextView.getText().toString();
            lugar = nombreLugar.getText().toString();
            nombreEvento1 = nombreEvento.getText().toString();
            textDescripcion = descripcion.getText().toString();
            fecha = editTextDate.getText().toString();
            horaa = editTextTime.getText().toString();

            if(latitud!=null && longitud!=null && lugar!=null && actividad!=null && nombreEvento1!=null && textDescripcion!=null && fecha!=null && horaa!=null && maximoParticipantes!=null){
                if (!latitud.isEmpty() && !longitud.isEmpty() && !lugar.isEmpty() && !actividad.isEmpty() && !nombreEvento1.isEmpty() && !textDescripcion.isEmpty() && !fecha.isEmpty() && !horaa.isEmpty() && !maximoParticipantes.isEmpty()) {

                    Evento evento = new Evento();
                    evento.setLugar(lugar);
                    for(int i=0; i<actividades.size(); i++){
                        if(actividad.equalsIgnoreCase(actividades.get(i))){
                            evento.setActividad(uidActividades.get(i));
                            break;
                        }
                    }
                    evento.setEtapa(nombreEvento1);
                    evento.setDescripcion(textDescripcion);
                    evento.setFecha(fecha);
                    evento.setHora(horaa);
                    evento.setLatitud(latitud);
                    evento.setLongitud(longitud);
                    evento.setEstado("En proceso");


                    databaseReference = FirebaseDatabase.getInstance().getReference("evento");
                    DatabaseReference newRef = databaseReference.push();
                    String event_key = newRef.getKey();
                    Log.d("msg-test-event-key", event_key);
                    newRef.setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                editTextDate.setText("");
                                nombreEvento.setText("");
                                descripcion.setText("");
                                editTextTime.setText("");
                                nombreLugar.setText("");
                                CometChatApiRest cometChatApiRest = new CometChatApiRest();
                                cometChatApiRest.crearGrupoEventoCometChat(event_key,nombreEvento1,firebaseAuth.getCurrentUser().getUid(),textDescripcion);

                                Toast.makeText(CrearEventoActivity.this, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }


                        }
                    });
                }else{
                    Log.d("msg", latitud);
                    Log.d("msg", longitud);
                    Log.d("msg", lugar);
                    Log.d("msg", actividad);
                    Log.d("msg", nombreEvento1);
                    Log.d("msg", textDescripcion);
                    Log.d("msg", fecha);
                    Log.d("msg", horaa);
                    Log.d("msg", maximoParticipantes);
                    Toast.makeText(CrearEventoActivity.this, "Faltan datos por rellenar", Toast.LENGTH_SHORT).show();
                }
            }



        });

        cancelar.setOnClickListener(view -> {
            finish();
        });


        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void showDatePicker() {
        // Get the current date
        Calendar today = Calendar.getInstance();

        // Create an instance of CalendarConstraints.Builder
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Set the minimum date to today
        constraintsBuilder.setStart(today.getTimeInMillis());

        // Build the CalendarConstraints
        CalendarConstraints calendarConstraints = constraintsBuilder.build();
        MaterialDatePicker<Long> materialDatePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccione fecha del evento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calendarConstraints)
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng pucp = new LatLng(-12.068585769932369, -77.0781371431298);
        mMap.addMarker(new MarkerOptions().position(pucp).title("PUCP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pucp,16));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
       latitud = String.valueOf(latLng.latitude);
       longitud = String.valueOf(latLng.longitude);
       Log.d("msg", "Latitud: " + latitud);
       Log.d("msg", "Longitud: " + longitud);

       mMap.clear();
       LatLng lugarEvent = new LatLng(latLng.latitude, latLng.longitude);
       mMap.addMarker(new MarkerOptions().position(lugarEvent));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        latitud = String.valueOf(latLng.latitude);
        longitud = String.valueOf(latLng.longitude);
        Log.d("msg", "Latitud: " + latitud);
        Log.d("msg", "Longitud: " + longitud);
        mMap.clear();
        LatLng lugarEvent = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(lugarEvent));
    }
}