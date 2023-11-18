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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CrearEventoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    TextInputLayout textInputLayoutDatePicker;

    EditText editActividad, nombreEvento, descripcion, textFecha, nombreLugar;
    TextInputEditText editTextDate;

    TextView horaInicial;

    ImageView seleccionIconoHoraInicial, seleccionIconoFecha;

    int hour, minute, arch;
    NumberPicker participantes;

    DatabaseReference databaseReference;

    Button crearEvento, cancelar;

    String actividad, nombreEvento1, textDescripcion, maximoBarra, fecha, lugar, horaa;
    String maximoParticipantes="1";
    String latitud = "-12.068585769932369";

    String longitud = "-77.0781371431298";

    GoogleMap mMap;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        textInputLayoutDatePicker = findViewById(R.id.FechaTextField);
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
            actividad = editActividad.getText().toString();
            lugar = nombreLugar.getText().toString();
            nombreEvento1 = nombreEvento.getText().toString();
            textDescripcion = descripcion.getText().toString();
            fecha = editTextDate.getText().toString();
            horaa = horaInicial.getText().toString();

            if(latitud!=null && longitud!=null && lugar!=null && actividad!=null && nombreEvento1!=null && textDescripcion!=null && fecha!=null && horaa!=null && maximoParticipantes!=null){
                if (!latitud.isEmpty() && !longitud.isEmpty() && !lugar.isEmpty() && !actividad.isEmpty() && !nombreEvento1.isEmpty() && !textDescripcion.isEmpty() && !fecha.isEmpty() && !horaa.isEmpty() && !maximoParticipantes.isEmpty()) {

                    Evento evento = new Evento();
                    evento.setLugar(lugar);
                    evento.setActividad(actividad);
                    evento.setEtapa(nombreEvento1);
                    evento.setDescripcion(textDescripcion);
                    evento.setFecha(fecha);
                    evento.setHora(horaa);
                    evento.setLatitud(latitud);
                    evento.setLongitud(longitud);


                    databaseReference = FirebaseDatabase.getInstance().getReference("evento");
                    DatabaseReference newRef = databaseReference.push();
                    String event_key = newRef.getKey();
                    Log.d("msg-test-event-key", event_key);
                    newRef.setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                editActividad.setText("");
                                editTextDate.setText("");
                                nombreEvento.setText("");
                                descripcion.setText("");
                                horaInicial.setText("");
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

                // Handle the selected time
                String selectedTime = String.format("%02d:%02d", hour, minute);
                horaInicial.setText(selectedTime);
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