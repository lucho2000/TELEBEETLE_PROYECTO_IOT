package com.example.telebeetle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.telebeetle.dto.Route;
import com.example.telebeetle.services.RouteService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.telebeetle.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Double latitud;
    private Double longitud;
    private Double latitudFinal;
    private Double longituFinal;
    RouteService routeService;
    private String api_key;
    private String start;
    private String start2 = "-77.08196490782349,-12.072866208225097";
    private String end;
    private PolylineOptions polylineOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        latitud = intent.getDoubleExtra("latitud", 1.0);
        longitud = intent.getDoubleExtra("longitud", 1.0);
        latitudFinal = intent.getDoubleExtra("latitudFinal",1.0);
        longituFinal = intent.getDoubleExtra("longitudFinal",1.0);
        Log.d("msg-test","latitud en maps: " + latitud);
        Log.d("msg-test","longitud en maps: " + longitud);

        routeService = new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RouteService.class);
        start = longitud + "," + latitud;

        end = longituFinal + "," + latitudFinal;
        api_key = BuildConfig.ROUTE_API_KEY;
        fetchRoute();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng position = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(position));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.addPolyline(polylineOptions);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
    public boolean tieneInternet(){
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.d("msg-test","Internet: " + tieneInternet);
        return tieneInternet;
    }
    public void fetchRoute(){
       if(tieneInternet()){
           routeService.getRoute(api_key,start,end).enqueue(new Callback<Route>() {
               @Override
               public void onResponse(Call<Route> call, Response<Route> response) {
                    if(response.isSuccessful()){
                        Log.d("msg-test", "Siu");
                        drawRoute(response.body());
                    }else{
                        Log.d("msg-test", "Nel");
                    }
               }

               @Override
               public void onFailure(Call<Route> call, Throwable t) {

               }
           });
       }
    }

    public void drawRoute(Route route){
        polylineOptions = new PolylineOptions();
        List<List<Double>> coordinates = route.getList().get(0).getGeometry().getCoordinates();
        for(List<Double> c : coordinates){
            LatLng ga =new LatLng(c.get(1), c.get(0));
            polylineOptions.add(ga);
        }
    }
}