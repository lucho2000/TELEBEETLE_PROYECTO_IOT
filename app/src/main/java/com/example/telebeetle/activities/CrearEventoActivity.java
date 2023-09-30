package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.telebeetle.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class CrearEventoActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutDatePicker;
    EditText editTextDate;

    TextView horaInicial;

    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        textInputLayoutDatePicker = findViewById(R.id.FechaTextField);
        editTextDate = findViewById(R.id.editTextDate);

        horaInicial = findViewById(R.id.textViewHoraInicial);

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

        horaInicial.setOnClickListener(view -> {

            popTimePicker();
        });
    }


    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Manejar la fecha seleccionada aquí
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextDate.setText(selectedDate);
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }

    public void popTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                horaInicial.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Selecciona la hora");
        timePickerDialog.show();
    }
}