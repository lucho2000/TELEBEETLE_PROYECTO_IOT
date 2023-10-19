package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityGeneralViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class GeneralViewActivity extends AppCompatActivity {
    ActivityGeneralViewBinding binding;

    String appID = "24272539e9e89a98"; // Replace with your App ID
    String region = "US"; // Replace with your App Region ("eu" or "us")
    String authKey= "1b88d312bf176863d22000e0ed856a1717bc8c06"; // Replace with your App ID

    String uidSample= "72411493"; // Replace with your sample UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCometChat();

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

    private void initCometChat(){
        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder()
                .setRegion(region)
                .setAppId(appID)
                .setAuthKey(authKey)
                .subscribePresenceForAllUsers().build();

        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successString) {
                CometChatUIKit.login(uidSample, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("cometchat-test-msg", "Login Successful : " + user.toString());
                    }
                    @Override
                    public void onError(CometChatException e) {
                        Log.e("cometchat-test-msg", "Login Failed : " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(CometChatException e) {

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
                Intent intent = new Intent(GeneralViewActivity.this, CrearActivity.class);
                startActivity(intent);
                dialog.dismiss();
                //Toast.makeText(GeneralViewActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();



            }
        });

        layoutListarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        //Estadisticas del delegado general
        layoutEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GeneralViewActivity.this, GeneralActivity.class);
                startActivity(intent);

                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(GeneralViewActivity.this, );
                //startActivity(intent);


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