package com.dwm.dwm_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.dwm.dwm_app.R;
import com.dwm.dwm_app.infrastructure.ViewHelper;
import com.dwm.dwm_app.server_connection.WineSearchServices;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

public class BottleImagePagerAdapter extends PagerAdapter {

    private Context _context;
    Bitmap[] _images;
    String[] _names;
    WineListItem _wine;
    LayoutInflater mLayoutInflater;

    private static final int FRONT_IMAGE = 0;
    private static final int BACK_IMAGE = 1;

    public BottleImagePagerAdapter(Context context, Bitmap[] images, String[] names, WineListItem wine) {
        this._context = context;
        this._images = images;
        this._names = names;
        this._wine = wine;
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

        imageView.setOnClickListener(view -> {
            View popupView = mLayoutInflater.inflate(R.layout.popup_image, null);

            ViewGroup root = (ViewGroup) ((Activity)_context).getWindow().getDecorView().getRootView();
            ViewHelper.applyDim(root, 0.7f);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) _context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            int width = (int)(deviceWidth * 0.9);
            int height = (int)(deviceHeight * 0.9);
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(((Activity)_context).findViewById(R.id.mainLayout), Gravity.CENTER, 0, 0);

            setImage(position, popupView);

            popupView.findViewById(R.id.nextIcon).setOnClickListener(view12 -> {
                popupView.findViewById(R.id.popupLoadingPanel).setVisibility(View.VISIBLE);
                setImage(BACK_IMAGE, popupView);
            });
            popupView.findViewById(R.id.previousIcon).setOnClickListener(view12 -> {
                popupView.findViewById(R.id.popupLoadingPanel).setVisibility(View.VISIBLE);
                setImage(FRONT_IMAGE, popupView);
            });

            ImageView closeIcon = popupView.findViewById(R.id.closeIcon);
            closeIcon.setOnClickListener(view1 -> {
                popupWindow.dismiss();
            });

            popupWindow.setOnDismissListener(() -> {
                ViewHelper.clearDim(root);
            });
        });

        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private void setImage(int position, View popupView) {
        new Thread(() -> {
            Bitmap bottleImage = WineSearchServices.getBottleImageType(_wine, "big", "png", _names[position]);
            if (bottleImage == null) {
                ViewHelper.showToast((Activity) _context, _context.getResources().getString(R.string.internet_error));
                popupView.findViewById(R.id.popupLoadingPanel).setVisibility(View.GONE);
                ViewHelper.toggleLoadingAnimation((Activity) _context, View.GONE);
                return;
            }
            ((Activity)_context).runOnUiThread(() -> {
                SubsamplingScaleImageView popupImageView = popupView.findViewById(R.id.popupBottleImage);
                popupImageView.setImage(ImageSource.bitmap(bottleImage));
                if (position == FRONT_IMAGE) {
                    popupView.findViewById(R.id.nextIcon).setVisibility(View.VISIBLE);
                    popupView.findViewById(R.id.previousIcon).setVisibility(View.GONE);
                }
                else {
                    popupView.findViewById(R.id.nextIcon).setVisibility(View.GONE);
                    popupView.findViewById(R.id.previousIcon).setVisibility(View.VISIBLE);
                }
                popupView.findViewById(R.id.popupLoadingPanel).setVisibility(View.GONE);
            });

        }).start();
    }
}