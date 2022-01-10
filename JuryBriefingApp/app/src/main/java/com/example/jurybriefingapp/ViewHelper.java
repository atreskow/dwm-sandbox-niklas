package com.example.jurybriefingapp;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class ViewHelper {
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
}
