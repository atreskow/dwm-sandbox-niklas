package com.dwm.dwm_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewHelper {
    public static void setValue(int id, String string, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = string == null? "" : string;
        name.setText(text);
    }

    public static void setValue(int id, boolean bool, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = bool ? activity.getResources().getString(R.string.yes) : activity.getResources().getString(R.string.no);
        name.setText(text);
    }

    public static void setValue(int id, float decimal, Activity activity) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
        TextView name = activity.findViewById(id);
        String text = formatter.format(decimal);
        name.setText(text);
    }

    public static void setValue(int id, int number, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = String.valueOf(number);
        name.setText(text);
    }

    public static void setValue(int id, float decimal, String unit, Activity activity) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
        TextView name = activity.findViewById(id);
        String text = formatter.format(decimal) + " " + unit;
        name.setText(text);
    }

    public static void setValue(int id, int number, String unit, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = String.valueOf(number) + " " + unit;
        name.setText(text);
    }

    public static void toggleLoadingAnimation(Activity activity, int mode) {
        activity.runOnUiThread(() -> {
            activity.findViewById(R.id.loadingPanel).setVisibility(mode);
            if (mode == View.VISIBLE) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public static void showToast(Activity activity, String text) {
        activity.runOnUiThread(() -> Toast.makeText(activity, text, Toast.LENGTH_SHORT).show());
    }

    public static void finish(Activity activity) {
        activity.runOnUiThread(() -> activity.finish());
    }

    public static void setupToolbar(AppCompatActivity activity) {
        Toolbar actionBar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(actionBar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean toolbarBottom = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getBoolean("toolbarBottom", false);
        if (toolbarBottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            actionBar.setLayoutParams(params);

            TextView whitespace = activity.findViewById(R.id.toolbar_whitespace);
            LinearLayout layout = activity.findViewById(R.id.mainLayout);
            layout.removeView(whitespace);
            layout.addView(whitespace, layout.getChildCount());
        }
    }

    public static void setTheme(Activity activity) {
        boolean darkmodeEnabled = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getBoolean("darkmodeEnabled", false);
        activity.setTheme(darkmodeEnabled ? R.style.DarkTheme : R.style.LightTheme);
    }

    public static void changeSearchPosition(Activity activity) {
        LinearLayout layout = activity.findViewById(R.id.mainLayout);

        LinearLayout searchLayout = activity.findViewById(R.id.linearLayoutSearch);
        layout.removeView(searchLayout);
        layout.addView(searchLayout, layout.getChildCount()-1);

        TextView txtViewBottom = activity.findViewById(R.id.txtViewBottom);
        layout.removeView(txtViewBottom);
        layout.addView(txtViewBottom, 0);
    }

    public static void changeDrawerGravity(Activity activity) {
        LinearLayout sliderlayout = activity.findViewById(R.id.sliderLayout);
        LinearLayout buttonLayout = activity.findViewById(R.id.btnResetLayout);
        sliderlayout.removeView(buttonLayout);
        sliderlayout.addView(buttonLayout);
    }

    public static LinearLayout createLinearLayout(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        return layout;
    }

    public static int getResetButtonVisbility(String input) {
        int textlength = input.length();
        return Session.getFacetQueryGroups().size() > 0 || textlength > 0 ? View.VISIBLE : View.GONE;
    }

    public static float getBitmapScalingFactor(Activity activity, Bitmap bitmap, ImageView imageView) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        int topMargin = layoutParams.topMargin;
        int bottomMargin = layoutParams.bottomMargin;

        int imageViewHeight = height - (topMargin + bottomMargin);

        return ( (float) imageViewHeight / (float) bitmap.getHeight() );
    }

    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
