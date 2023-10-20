package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.telebeetle.databinding.ActivityEditarEventoBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditarEventoActivity extends AppCompatActivity {

    TextInputLayout textInputLayout;
    EditText editTextDatePicker;
    ActivityEditarEventoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        textInputLayout = binding.FechaTextField;
        editTextDatePicker = binding.editTextDate;
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterialDesignDatePicker();
            }
        });

    }

    public void showMaterialDesignDatePicker(){

        MaterialDatePicker<Long> materialDatePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccione fecha del evento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                editTextDatePicker.setText(MessageFormat.format("{0}",date));
            }
        });
        materialDatePicker.show(getSupportFragmentManager(),"tag");
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