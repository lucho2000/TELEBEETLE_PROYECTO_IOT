package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityVerApoyosBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class verApoyosActivity extends AppCompatActivity {
    ActivityVerApoyosBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerApoyosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent1 = getIntent();
        Evento evento = (Evento) intent1.getSerializableExtra("evento");
        Log.d("msg-test", evento.getEtapa());
        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.chooseApoyo;
        MyPagerAdapter2 adapter = new MyPagerAdapter2(this, evento);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(adapter);
                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Barra");
                            break;
                        case 1:
                            tab.setText("Participantes");
                            break;
                    }
                }).attach();
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        //Toast.makeText(getContext(), "Tab " + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
                    }
                });
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
}