package com.example.telebeetle.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivitySubirFotosBinding;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class SubirFotosActivity extends AppCompatActivity {

    ActivitySubirFotosBinding binding;
    String eventoUID;


    Button select, previous, next;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView total;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubirFotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        eventoUID = (String) intent.getStringExtra("eventoUID");

        //Toolbar toolbar = findViewById(R.id.myToolbar);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        finish();
        //    }
        //});
        //binding.imageView7.setOnClickListener(v -> {
        //    pickMedias.launch(new PickVisualMediaRequest.Builder()
        //            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
        //            .build());
        //    //pickMedia.launch(new PickVisualMediaRequest.Builder()
        //    //        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
        //    //        .build());
        //});
        select = findViewById(R.id.select);
        total = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<Uri>();

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getApplicationContext());
            }
        });
        next = findViewById(R.id.next);

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(SubirFotosActivity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });

        imageView = findViewById(R.id.image);

        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("msg-test", "Selected URI: " + uri);
                    Log.d("msg-test", "uri.getLastPathSegment(): " + uri.getLastPathSegment());
                    //String mimeType = getContentResolver().getType(uri);
                    //String[] parts = mimeType.split("/");
                    //String part2 = parts[1];
                    //Log.d("msg-test", "MimeType: " + mimeType);
                    //if(firebaseUser != null){
                    //    String uid = firebaseUser.getUid();
                    //    StorageReference imagesRef = storageReference.child(uid).child(uri.getLastPathSegment() + "." + part2); //.child("images").child(uri.getLastPathSegment());

                    //    StorageMetadata metadata = new StorageMetadata.Builder()
                    //            .setCustomMetadata("distro", "arch")
                    //            .setCustomMetadata("based", "obvi")
                    //            .build();

                    //    UploadTask uploadTask = imagesRef.putFile(uri, metadata);

                    //    uploadTask.addOnFailureListener(exception -> {
                    //        exception.printStackTrace();
                    //    }).addOnSuccessListener(taskSnapshot -> {
                    //        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    //        Toast.makeText(StorageActivity.this, "Archivo subido correctamente", Toast.LENGTH_SHORT).show();
                    //    }).addOnProgressListener(snapshot -> {
                    //        long bytesTransferred = snapshot.getBytesTransferred();
                    //        long totalByteCount = snapshot.getTotalByteCount();
                    //        double porcentajeSubida = Math.round((bytesTransferred * 1.0f / totalByteCount) * 100);
                    //        binding.textViewPorc.setText(porcentajeSubida + "%");
                    //    });
                    //}else{
                    //    Toast.makeText(StorageActivity.this, "I use arch btw", Toast.LENGTH_SHORT).show();
                    //}
                } else {
                    Log.d("msg-test", "No media selected");
                }
            });
    ActivityResultLauncher<PickVisualMediaRequest> pickMedias =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(), uris -> {
                if (uris != null && !uris.isEmpty()) {
                    // Handle the selected image URIs here
                    for (Uri uri : uris) {
                        // Process each selected image URI
                        // uri contains the URI of each selected image
                        Log.d("msg-test", "Selected URI: " + uri);
                    }
                }
            });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}