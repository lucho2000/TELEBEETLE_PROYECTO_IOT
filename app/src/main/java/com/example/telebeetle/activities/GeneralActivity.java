package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.telebeetle.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GeneralActivity extends AppCompatActivity {

    BarChart barChart;
    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);


        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);





        // Data con respecto a los usuarios que se registran a la plataforma
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        BarEntry barEntry = new BarEntry((float)10.0 ,14);
        BarEntry barEntry1 = new BarEntry((float)11.0,16);
        BarEntry barEntry2 = new BarEntry((float)12.0,20);
        BarEntry barEntry3 = new BarEntry((float)13.0,22);
        BarEntry barEntry4 = new BarEntry((float)14.0,28);
        BarEntry barEntry5 = new BarEntry((float)15.0,34);
        BarEntry barEntry6 = new BarEntry((float)16.0,36);
        BarEntry barEntry7 = new BarEntry((float)17.0,36);
        BarEntry barEntry8 = new BarEntry((float)18.0,40);

        barEntries1.add(barEntry);
        barEntries1.add(barEntry1);
        barEntries1.add(barEntry2);
        barEntries1.add(barEntry3);
        barEntries1.add(barEntry4);
        barEntries1.add(barEntry5);
        barEntries1.add(barEntry6);
        barEntries1.add(barEntry7);
        barEntries1.add(barEntry8);


        //Participantes por actividades
        ArrayList<PieEntry> pieEntries1 = new ArrayList<>();
        PieEntry pieEntry = new PieEntry((float)20.0,"Voley Damas");
        PieEntry pieEntry1 = new PieEntry((float)50.0,"Futsal Varones");
        PieEntry pieEntry2 = new PieEntry((float)5.0,"Ajedrez");
        PieEntry pieEntry3 = new PieEntry((float)30.0,"Futsal Damas");
        PieEntry pieEntry4 = new PieEntry((float)15.0,"Voley Varones");
        PieEntry pieEntry5 = new PieEntry((float)10.0,"Atletismo");
        PieEntry pieEntry6 = new PieEntry((float)22.0,"Basquet Varones");

        pieEntries1.add(pieEntry);
        pieEntries1.add(pieEntry1);
        pieEntries1.add(pieEntry2);
        pieEntries1.add(pieEntry3);
        pieEntries1.add(pieEntry4);
        pieEntries1.add(pieEntry5);
        pieEntries1.add(pieEntry6);








        BarDataSet barDataSet = new BarDataSet(barEntries1,"Nuevos usuarios en Octubre");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(1000);
        barChart.getDescription().setText("Nuevos usuarios en la plataforma");
        barChart.getDescription().setTextColor(Color.BLUE);
        PieDataSet pieDataSet = new PieDataSet(pieEntries1,"Actividades");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(1000,1000);
        pieChart.getDescription().setEnabled(false);


        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}