package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityGeneralViewBinding;
import com.example.telebeetle.fragments.ChatFragment;
import com.example.telebeetle.viewmodels.GeneralViewActivityViewModel;
import com.example.telebeetle.viewmodels.GroupChatViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class GeneralViewActivity extends AppCompatActivity {
    ActivityGeneralViewBinding binding;

    String appID = "24272539e9e89a98"; // Replace with your App ID
    String region = "US"; // Replace with your App Region ("eu" or "us")
    String authKey= "1b88d312bf176863d22000e0ed856a1717bc8c06"; // Replace with your App ID

    String uidSample= "72411493"; // Replace with your sample UID

    String rol_delegado_general="delegado_general";
    String rol_delegado_actividad="delegado_actividad";
    String rol_alumno="Alumno";

    String rol_egresado="Egresado";

    String rol_selected = "usuario";

    DatabaseReference databaseReference;

    Usuario usuario;
    private int menuToChoose = 0;

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(GeneralViewActivity.this, "notifications not allowed", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                Toast.makeText(GeneralViewActivity.this, "FCM notifications allowed", Toast.LENGTH_SHORT).show();
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                Toast.makeText(GeneralViewActivity.this, "FCM notifications not allowed buy user. Allow them to use FCM notifications in chat", Toast.LENGTH_SHORT).show();
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void createChatNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "canal de notificaciones FCM - Cometchat";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    NavArgument guidValueNav;
    NavArgument nameNav;
    NavArgument group_descriptionNav;
    NavArgument group_typeNav;
    NavArgument group_ownerNav;
    NavArgument member_countNav;
    NavArgument flagNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        initCometChat(firebaseAuth.getCurrentUser().getUid());
        createChatNotificationChannel();


        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); //datos de firebase de la coleccion de "usuarios"
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    rol_selected = user.getRol();
                    GeneralViewActivityViewModel generalViewActivityViewModel = new ViewModelProvider(GeneralViewActivity.this).get(GeneralViewActivityViewModel.class);
                    generalViewActivityViewModel.getUsuario().setValue(user);
                    BottomNavigationView bottomNavigationView =binding.bottomNavigationView;
                    if (rol_selected.equals(rol_delegado_general)) {

                        // Obtener la información sobre el fragmento del Intent
                       /* Intent intent = getIntent();
                        if (intent != null) {
                            String fragmentTag = intent.getStringExtra("FRAGMENT_TAG");
                            // Verificar el tag del fragmento y mostrar el fragmento correspondiente
                            if ("chatFragment".equals(fragmentTag)) {
                                loadChatFragment();
                            }
                        }*/
                        // Cargar otro fragmento o realizar otra lógica según sea necesario
                        menuToChoose = R.menu.general_bottom_menu;
                        bottomNavigationView.getMenu().clear();
                        bottomNavigationView.inflateMenu(menuToChoose);

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_general_view);
                        NavController navController = navHostFragment.getNavController();

                        NavInflater inflater = navController.getNavInflater();
                        NavGraph graph = inflater.inflate(R.navigation.nav_graph_general);
                        //graph.addArgument("argument", NavArgument)
                        graph.setStartDestination(R.id.homeDelegadoGeneralFragment);

                        navHostFragment.getNavController().setGraph(graph);
                        //navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());

                        NavigationUI.setupWithNavController(bottomNavigationView, navController);

                        askNotificationPermission();
                        binding.fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showBottomMenuFabDialog();
                            }
                        });

                    } else if (rol_selected.equals(rol_delegado_actividad)) {
                        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) binding.fab.getLayoutParams();
                        p.setAnchorId(View.NO_ID);
                        binding.fab.setLayoutParams(p);
                        binding.fab.setVisibility(View.GONE);
                        menuToChoose = R.menu.del_actividad_bottom_menu;
                        bottomNavigationView.getMenu().clear();
                        bottomNavigationView.inflateMenu(menuToChoose);

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_general_view);
                        NavController navController = navHostFragment.getNavController();

                        NavInflater inflater = navController.getNavInflater();
                        NavGraph graph = inflater.inflate(R.navigation.nav_graph_general);
                        //graph.addArgument("argument", NavArgument)
                        graph.setStartDestination(R.id.homeDelegadoActivitdadFragment);

                        navHostFragment.getNavController().setGraph(graph);
                        //navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());

                        NavigationUI.setupWithNavController(bottomNavigationView, navController);

                        //invalidateOptionsMenu();
                        askNotificationPermission();

                    } else { //rol_usuario fue seleccionado aqui
                        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) binding.fab.getLayoutParams();
                        p.setAnchorId(View.NO_ID);
                        binding.fab.setLayoutParams(p);
                        binding.fab.setVisibility(View.GONE);
                        menuToChoose = R.menu.student_bottom_menu;

                        // Obtener la información sobre el fragmento del Intent
                       Intent intent = getIntent();
                        Log.d("msg-test-guidValue","AQUIIIFNSAKNFJASNFNASKF");
                        if (intent != null && intent.getStringExtra("FRAGMENT_TAG")!=null) {
                            String fragmentTag = intent.getStringExtra("FRAGMENT_TAG");
                            // Verificar el tag del fragmento y mostrar el fragmento correspondiente
                            if ("chatFragment".equals(fragmentTag)) {
                                bottomNavigationView.getMenu().clear();
                                bottomNavigationView.inflateMenu(menuToChoose);

                                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_general_view);
                                NavController navController = navHostFragment.getNavController();

                                NavInflater inflater = navController.getNavInflater();
                                NavGraph graph = inflater.inflate(R.navigation.nav_graph_general);
                                //graph.addArgument("argument", NavArgument)

                                Intent messageIntent = getIntent();
                                //Bundle bundle = new Bundle();
                                String guidValue = messageIntent.getStringExtra("guid");
                                String name = messageIntent.getStringExtra("name");
                                String group_description = messageIntent.getStringExtra("group_description");
                                String group_type = messageIntent.getStringExtra("group_type");
                                String group_owner = messageIntent.getStringExtra("group_owner");
                                int member_count = messageIntent.getIntExtra("member_count",0);
                               // bundle.putString("guid", guidValue);
                                Log.d("msg-test-guidValue",guidValue);
                                Log.d("msg-test-membercount", String.valueOf(member_count));




                                /*Group group = new Group();
                                group.setGuid(guidValue);
                                group.setName(name);
                                group.setDescription(group_description);
                                group.setGroupType(group_type);
                                group.setOwner(group_owner);
                                group.setMembersCount(member_count);
                                group.setHasJoined(true);

                                GroupChatViewModel groupChatViewModel = new ViewModelProvider(GeneralViewActivity.this).get(GroupChatViewModel.class);
                                groupChatViewModel.getGroup().setValue(group);*/

                                /*groupChatViewModel.getGuid().setValue(guidValue);
                                groupChatViewModel.getName().setValue(name);
                                groupChatViewModel.getGroup_description().setValue(group_description);
                                groupChatViewModel.getGroup_type().setValue(group_type);
                                groupChatViewModel.getGroup_owner().setValue(group_owner);
                                groupChatViewModel.getMember_count().setValue(member_count);*/




                                /*
                                SharedPreferences sharedPreferences = getSharedPreferences("share_preferences_conf",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("guid",guidValue);
                                edit.putString("name",name);
                                edit.putString("group_description",group_description);
                                edit.putString("group_type",group_type);
                                edit.putString("group_owner",group_owner);
                                edit.putString("member_count",member_count);

                                edit.apply();*/


                                // Crear una nueva instancia de ChatFragment con argumentos
                                //ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.chatFragment);
                                /*Bundle args = new Bundle();
                                args.putString("ARG_guid", guidValue);
                                args.putString("ARG_name", name);
                                args.putString("ARG_description", group_description);
                                args.putString("ARG_type", group_type);
                                args.putString("ARG_owner", group_owner);
                                args.putInt("ARG_memberscount", member_count);*/

                                //ChatFragment chatFragment = ChatFragment.newInstance(guidValue, name,group_description,group_type,group_owner,member_count);
                                //Log.d("msg-test-iddestination", String.valueOf(chatFragment.getId()));
                                //ChatFragment chatFragment = new ChatFragment();
                                //chatFragment.setArguments(args);

                                // Establecer el startDestination en el ChatFragment
                                // Buscar la acción que lleva al ChatFragment
                                //NavAction action = graph.getAction(R.id.chatFragment);
                            // Establecer el nuevo startDestination usando la acción


                                // Crear argumentos de navegación con valores predeterminados
                                guidValueNav = new NavArgument.Builder().setDefaultValue(guidValue).build();
                                nameNav = new NavArgument.Builder().setDefaultValue(name).build();
                                group_descriptionNav = new NavArgument.Builder().setDefaultValue(group_description).build();
                                group_typeNav = new NavArgument.Builder().setDefaultValue(group_type).build();
                                group_ownerNav = new NavArgument.Builder().setDefaultValue(group_owner).build();
                                member_countNav = new NavArgument.Builder().setDefaultValue(member_count).build();
                                flagNav = new NavArgument.Builder().setDefaultValue(true).build();

                                graph.setStartDestination(R.id.chatFragment);
                                // Agregar argumentos al gráfico de navegación
                                graph.addArgument("ARG_guid", guidValueNav);
                                graph.addArgument("ARG_name", nameNav);
                                graph.addArgument("ARG_description", group_descriptionNav);
                                graph.addArgument("ARG_type", group_typeNav);
                                graph.addArgument("ARG_owner", group_ownerNav);
                                graph.addArgument("ARG_memberscount", member_countNav);
                                graph.addArgument("flag", flagNav);
                                //flagExternoParaChat = true;


                                //graph.addArgument("argument", NavArgument)
                                //graph.setStartDestination(R.id.chatFragment);

                                navHostFragment.getNavController().setGraph(graph);
                                //navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());

                                NavigationUI.setupWithNavController(bottomNavigationView, navController);


                                navHostFragment.getNavController().setGraph(graph);
                                //navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());

                                NavigationUI.setupWithNavController(bottomNavigationView, navController);

                                askNotificationPermission();
                            }
                        } else {
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(menuToChoose);

                            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_general_view);
                            NavController navController = navHostFragment.getNavController();

                            NavInflater inflater = navController.getNavInflater();
                            NavGraph graph = inflater.inflate(R.navigation.nav_graph_general);
                            //graph.addArgument("argument", NavArgument)
                            graph.setStartDestination(R.id.homeStudentFragment);

                            navHostFragment.getNavController().setGraph(graph);
                            //navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());

                            NavigationUI.setupWithNavController(bottomNavigationView, navController);

                            askNotificationPermission();
                        }
                        // Cargar otro fragmento o realizar otra lógica según sea necesario



                    }
                } else {
                    Log.d("User", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User", "Failed to read value.", error.toException());
            }
        });


        /*firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String firebaseAuthUserUID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        //Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        if (Objects.requireNonNull(dataSnapshot.getKey()).equalsIgnoreCase(firebaseAuthUserUID)) {
                            Usuario usuarioDabase = dataSnapshot.getValue(Usuario.class);
                            rol_selected = usuarioDabase.getCondicion();
                            Log.d("msg-test", rol_selected);
                            Log.d("msg-test", usuarioDabase.getNombres());
                            GeneralViewActivityViewModel generalViewActivityViewModel = new ViewModelProvider(GeneralViewActivity.this).get(GeneralViewActivityViewModel.class);
                            generalViewActivityViewModel.getUsuario().setValue(usuarioDabase);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //Log.d("msg-test1",rol_selected);




    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToChoose, menu);
        return true;
    }*/


    private void initCometChat(String uidUser){

        String uidUserLowerCase = uidUser.toLowerCase();
        Log.d("msg-test",uidUserLowerCase);

        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder()
                .setRegion(region)
                .setAppId(appID)
                .setAuthKey(authKey)
                .subscribePresenceForAllUsers().build();

        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successString) {
                CometChatUIKit.login(uidUserLowerCase, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("cometchat-test-msg", "Login Successful : " + user.toString());

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("msg-test-cloud-messaging", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        // Get new FCM registration token
                                        String token = task.getResult();

                                        // Log and toast
                                        // tring msg = getString(R.string.msg_token_fmt, token);
                                        //Log.d("msg-test-cloud-messaging", msg);
                                        //Toast.makeText(GeneralViewActivity.this, token, Toast.LENGTH_SHORT).show();
                                        CometChat.registerTokenForPushNotification(token, new CometChat.CallbackListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                Log.e("onSuccessPN: ", s);
                                            }

                                            @Override
                                            public void onError(CometChatException e) {
                                                Log.e("onErrorPN: ", e.getMessage());
                                            }
                                        });
                                    }
                                });


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
        //LinearLayout layoutListarEventos = dialog.findViewById(R.id.layoutListarEventos);
        //LinearLayout layoutCrearEvento = dialog.findViewById(R.id.layoutCrearEvento);
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

                Intent intent = new Intent(GeneralViewActivity.this, ListarUsuariosActivity.class);
                startActivity(intent);

                dialog.dismiss();
                //Toast.makeText(GeneralViewActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        //Estadisticas del delegado general
        layoutEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Usuario> listaAlumnos = new ArrayList<>();
                        List<Usuario> listaEgresados = new ArrayList<>();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Usuario usuario = snapshot1.getValue(Usuario.class);

                            if (usuario != null) {
                                if ("Alumno".equalsIgnoreCase(usuario.getCondicion())) {
                                    listaAlumnos.add(usuario);
                                } else if ("Egresado".equalsIgnoreCase(usuario.getCondicion())) {
                                    listaEgresados.add(usuario);
                                }
                            }
                        }

                        Intent intent = new Intent(GeneralViewActivity.this, GeneralActivity.class);
                        intent.putExtra("listaAlumnos", (Serializable) listaAlumnos);
                        intent.putExtra("listaEgresados", (Serializable) listaEgresados);

                        startActivity(intent);

                        dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //Toast.makeText(GeneralViewActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(GeneralViewActivity.this, );
                //startActivity(intent);


            }
        });

        /*layoutListarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralViewActivity.this, Intermedio.class);
                startActivity(intent);
                dialog.dismiss();
                Toast.makeText(GeneralViewActivity.this,"Listar Eventos",Toast.LENGTH_SHORT).show();

            }
        });*/

        /*layoutCrearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GeneralViewActivity.this, CrearEventoActivity.class);
                startActivity(intent);
                dialog.dismiss();
                //Toast.makeText(GeneralViewActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();


            }
        });*/


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


    boolean flagExternoParaChat = false;
    @Override
    public void onResume() {
        super.onResume();

        // Crear un Bundle para los argumentos
        Bundle args = new Bundle();


        // Obtener el NavController desde el BottomNavigationView
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_general_view);
        Intent intent = getIntent();

        if (intent.getStringExtra("FRAGMENT_TAG")!=null && guidValueNav!=null && !flagExternoParaChat) {
            String defaultguidValueNav = (String) guidValueNav.getDefaultValue();
            String guidValue = intent.getStringExtra("guid");
            String name = intent.getStringExtra("name");
            String group_description = intent.getStringExtra("group_description");
            String group_type = intent.getStringExtra("group_type");
            String group_owner = intent.getStringExtra("group_owner");
            int member_count = intent.getIntExtra("member_count",0);

            if (defaultguidValueNav.equalsIgnoreCase(guidValue)) {
                args.putString("ARG_guid", (String) guidValueNav.getDefaultValue());
                args.putString("ARG_name", (String) nameNav.getDefaultValue());
                args.putString("ARG_description", (String) group_descriptionNav.getDefaultValue());
                args.putString("ARG_type", (String) group_typeNav.getDefaultValue());
                args.putString("ARG_owner", (String) group_ownerNav.getDefaultValue());
                args.putInt("ARG_memberscount", (Integer) member_countNav.getDefaultValue());
                args.putBoolean("flag", false);

                // Navegar a la acción o destino del ChatFragment con los argumentos
                navController.navigate(R.id.chatFragment, args);

                Log.d("msg-test","onResume:  push notification, el mismo grupo");

            } else {

                args.putString("ARG_guid", guidValue);
                args.putString("ARG_name", name);
                args.putString("ARG_description", group_description);
                args.putString("ARG_type", group_type);
                args.putString("ARG_owner", group_owner);
                args.putInt("ARG_memberscount", member_count);
                args.putBoolean("flag", false);


                guidValueNav = new NavArgument.Builder().setDefaultValue(guidValue).build();
                nameNav = new NavArgument.Builder().setDefaultValue(name).build();
                group_descriptionNav = new NavArgument.Builder().setDefaultValue(group_description).build();
                group_typeNav = new NavArgument.Builder().setDefaultValue(group_type).build();
                group_ownerNav = new NavArgument.Builder().setDefaultValue(group_owner).build();
                member_countNav = new NavArgument.Builder().setDefaultValue(member_count).build();
                flagNav = new NavArgument.Builder().setDefaultValue(true).build();
                navController.navigate(R.id.chatFragment, args);
                Log.d("msg-test","onResume:  push notification, diferente grupo");

            }
            intent.removeExtra("FRAGMENT_TAG");
        } else {
            Log.d("msg-test","onResume: pero sin push notification"); //aun incompleto el flujo para ingresar al chat. se puede pero hay ciertos problemitas
        }


    }
}