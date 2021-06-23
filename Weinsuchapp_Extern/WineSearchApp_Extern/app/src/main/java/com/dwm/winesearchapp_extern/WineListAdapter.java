package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        RelativeLayout wineItemLayout = v.findViewById(R.id.wineItemLayout);
        TextView wineName = v.findViewById(R.id.txtViewWineName);
        TextView producer = v.findViewById(R.id.txtViewProducer);
        TextView origin = v.findViewById(R.id.txtViewOrigin);
        TextView varietals = v.findViewById(R.id.txtViewVarietal);
        TextView award = v.findViewById(R.id.txtViewTrophy);

        wineName.setText(item.WineName);

        String producerText = String.format(context.getResources().getString(R.string.winelist_producer), item.Producer);
        producer.setText(producerText);

        String region = (item.Region == null ? "" : item.Region + ", ") + item.Country;
        String originText = String.format(context.getResources().getString(R.string.winelist_origin), region);
        origin.setText(originText);

        String varietalText = String.format(context.getResources().getString(R.string.winelist_varietal), item.Varietal);
        varietals.setText(varietalText);

        if (item.Award != null) {
            String awardText = String.format(context.getResources().getString(R.string.winelist_award), item.Award);
            award.setText(awardText);
        }

        wineItemLayout.setOnClickListener(view -> {
            Session.SetSelectedListItem(item);
            Intent intent = new Intent(context, WinedetailsActivity.class);
            context.startActivity(intent);
        });

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