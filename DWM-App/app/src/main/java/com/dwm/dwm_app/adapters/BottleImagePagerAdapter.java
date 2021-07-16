package com.dwm.dwm_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ablanco.zoomy.Zoomy;
import com.dwm.dwm_app.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

public class BottleImagePagerAdapter extends PagerAdapter {

    private Context _context;
    Bitmap[] _images;
    LayoutInflater mLayoutInflater;

    public BottleImagePagerAdapter(Context context, Bitmap[] images) {
        this._context = context;
        this._images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.bottle_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.bottleImage);
        imageView.setImageBitmap(_images[position]);

        Zoomy.Builder builder = new Zoomy.Builder((Activity) _context).target(imageView);
        builder.register();

        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}