package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CrearEventoActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutDatePicker;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);


        textInputLayoutDatePicker = findViewById(R.id.FechaTextField);
        editTextDate = findViewById(R.id.editTextDate);

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
}