package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewHelper {
    public static void SetValue(int id, String string, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = string == null? "" : string;
        name.setText(text);
    }

    public static void SetValue(int id, boolean bool, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = bool ? activity.getResources().getString(R.string.yes) : activity.getResources().getString(R.string.no);
        name.setText(text);
    }

    public static void SetValue(int id, float decimal, Activity activity) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
        TextView name = activity.findViewById(id);
        String text = formatter.format(decimal);
        name.setText(text);
    }

    public static void SetValue(int id, int number, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = String.valueOf(number);
        name.setText(text);
    }

    public static void SetValue(int id, float decimal, String unit, Activity activity) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
        TextView name = activity.findViewById(id);
        String text = formatter.format(decimal) + " " + unit;
        name.setText(text);
    }

    public static void SetValue(int id, int number, String unit, Activity activity) {
        TextView name = activity.findViewById(id);
        String text = String.valueOf(number) + " " + unit;
        name.setText(text);
    }

    public static void ToggleLoadingAnimation(Activity activity, int mode) {
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

    public static void ShowToast(Activity activity, String text) {
        activity.runOnUiThread(() -> Toast.makeText(activity, text, Toast.LENGTH_SHORT).show());
    }

    public static void Finish(Activity activity) {
        activity.runOnUiThread(() -> activity.finish());
    }

    public static void SetupToolbar(AppCompatActivity activity) {
        Toolbar actionBar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(actionBar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static LinearLayout CreateLinearLayout(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        return layout;
    }

    public static int GetResetButtonVisbility() {
        return Session.GetFacetQueryGroups().size() > 0 ? View.VISIBLE : View.GONE;
    }

    public static float GetBitmapScalingFactor(Activity activity, Bitmap bitmap, ImageView imageView) {
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
}
