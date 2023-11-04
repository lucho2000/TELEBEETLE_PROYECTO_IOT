package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.telebeetle.BuildConfig;
import com.example.telebeetle.R;
import com.example.telebeetle.dto.Route;
import com.example.telebeetle.services.RouteService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.telebeetle.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Double latitud;
    private Double longitud;
    private Double latitudFinal;
    private Double longituFinal;
    private String lugar;
    RouteService routeService = new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RouteService.class);
    private String api_key;
    private String start;
    private String start2 = "-77.08196490782349,-12.072866208225097";
    private String end;
    private String end2 = "-122.09137734038724,37.423858347988535";
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
        lugar = intent.getStringExtra("lugar");
        Log.d("msg-test","latitud en maps: " + latitud);
        Log.d("msg-test","longitud en maps: " + longitud);

        start = longitud + "," + latitud;
        end = longituFinal + "," + latitudFinal;
        api_key = BuildConfig.ROUTE_API_KEY;
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ImageView arrow = findViewById(R.id.left_arrow);
        arrow.setOnClickListener(view -> {
            this.finish();
        });
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                ArrayList<Marker> markers = new ArrayList<>();
                LatLng position = new LatLng(latitud, longitud);
                LatLng position2 = new LatLng(latitudFinal,longituFinal);

                Drawable yourDrawable = ContextCompat.getDrawable(MapsActivity.this, R.drawable.telito);
                int targetWidth = 64;  // Set your target width
                int targetHeight = 64; // Set your target height
                Bitmap resizedBitmap = resizeDrawable(yourDrawable, targetWidth, targetHeight);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title("Telito")
                        .snippet("Marker Snippet")
                        .icon(icon);

                Marker marker1 = mMap.addMarker(markerOptions);
                Marker marker2 = mMap.addMarker(new MarkerOptions().position(position2).title(lugar));
                markers.add(marker1);
                markers.add(marker2);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 200; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
                fetchRoute();
                // Add a marker in Sydney and move the camera
                //LatLng sydney = new LatLng(-34, 151);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });


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
                        assert response.body() != null;
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
        mMap.addPolyline(polylineOptions);
    }
    public Bitmap resizeDrawable(Drawable drawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}