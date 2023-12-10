package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetailDonationBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailDonationActivity extends AppCompatActivity {


    FirebaseDatabase db = FirebaseDatabase.getInstance();

    ActivityDetailDonationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        Donacion donacion = (Donacion) intent.getSerializableExtra("donacion");

        binding.textView47.setText("S./ " + donacion.getMonto());

        Picasso.get().load(donacion.getImagenCaptura()).into(binding.imageView9);



        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.button5.setOnClickListener(view -> {

            DatabaseReference myRef = db.getReference("donaciones_por_validar/"+donacion.getKeyDonacion());
            myRef.removeValue();
            //myRef.setValue(false);
            Toast.makeText(DetailDonationActivity.this,"La donacion no ha sido aceptada",Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DetailDonationActivity.this,ValidarDonacionesActivity.class);
            startActivity(intent1);
            finish();

        });
        binding.button3.setOnClickListener(view -> {


            DatabaseReference donacionesRef = db.getReference("donaciones");
            DatabaseReference donacionPorValidarRef = db.getReference("donaciones_por_validar").child(donacion.getKeyDonacion());
            donacionPorValidarRef.child("accepted").setValue(true);
            donacion.setAccepted(true);
            donacionPorValidarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();
                    donacionesRef.child(donacion.getKeyDonacion()).setValue(dataSnapshot.getValue());
                    Toast.makeText(DetailDonationActivity.this, "La donacion ha sido aceptada", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(DetailDonationActivity.this, ValidarDonacionesActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("msg-test", String.valueOf(databaseError));
                }
            });
        });




    }
}