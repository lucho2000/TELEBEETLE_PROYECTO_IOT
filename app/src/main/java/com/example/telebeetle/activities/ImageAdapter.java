package com.example.telebeetle.activities;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.telebeetle.R;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private final Context context;

    public ImageAdapter(Context context, List<String> imageUrls) {
        super(context, 0, imageUrls);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Glide.with(context)
                .load(getItem(position))
                .into(imageView);

        return imageView;
    }
}

