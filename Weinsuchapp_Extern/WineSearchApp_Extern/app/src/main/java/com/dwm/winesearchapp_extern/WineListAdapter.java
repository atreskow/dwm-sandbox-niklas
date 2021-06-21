package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.response.WineData;

public class WineListAdapter extends ArrayAdapter<WineListItem> {

    private final Context context;
    private final int listItemResourceId;

    public WineListAdapter(Context context, int listItemResourceId)
    {
        super(context, listItemResourceId);
        this.context = context;
        this.listItemResourceId = listItemResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        WineListItem item = getItem(position);

        View v = inflater.inflate(listItemResourceId, parent, false);
        TextView wineName = v.findViewById(R.id.txtViewWineName);
        TextView producer = v.findViewById(R.id.txtViewProducer);
        TextView origin = v.findViewById(R.id.txtViewOrigin);
        TextView varietals = v.findViewById(R.id.txtViewVarietal);
        TextView trophy = v.findViewById(R.id.txtViewTrophy);

        wineName.setText(item.WineName);
        producer.setText(item.Producer);
        origin.setText(item.Origin);
        varietals.setText(item.Varietal);
        trophy.setText(item.Award);

        new Thread(() ->  {
            String[] imageNames = WineSearchServices.GetBottleImageNames(item.Id);
            if (imageNames != null && imageNames.length != 0) {
                for (String imageName : imageNames) {
                    if (imageName.endsWith("F")) {
                        Bitmap bottleImage = WineSearchServices.GetBottleImage(item.Id, imageName);
                        ((Activity) context).runOnUiThread(() ->
                                ((ImageView) v.findViewById(R.id.imageFront)).setImageBitmap(bottleImage)
                        );
                    }
                }
            }
            if (Utils.HasAward(item.Ranking)) {
                Bitmap medalImage = WineSearchServices.GetMedalImage(item.TrophyCode, item.Ranking);
                ((Activity) context).runOnUiThread(() -> {
                        ((ImageView) v.findViewById(R.id.imageMedal)).setImageBitmap(medalImage);
                });
            }
        }).start();


        return v;
    }
}