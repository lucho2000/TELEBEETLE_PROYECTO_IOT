package com.example.telebeetle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.telebeetle.databinding.ActivityGeneralViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class GeneralViewActivity extends AppCompatActivity {
    ActivityGeneralViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView =binding.bottomNavigationView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home, R.id.donation, R.id.fab, R.id.chat,R.id.profile)
                .build();*/
        //NavController navController = Navigation.findNavController(binding.navHostFragmentActivityGeneralView);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_general_view);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showBottomMenuFabDialog();
            }
        });
    }


    private void showBottomMenuFabDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_bottom_sheet_layout);

        LinearLayout layoutCrearActividad = dialog.findViewById(R.id.layoutCrearActividad);
        LinearLayout layoutListarUsuarios = dialog.findViewById(R.id.layoutListarUsuarios);
        LinearLayout layoutEstadisticas = dialog.findViewById(R.id.layoutEstadisticas);
        LinearLayout layoutListarEventos = dialog.findViewById(R.id.layoutListarEventos);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        layoutCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        layoutListarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        layoutEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        layoutListarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomMenuDialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}