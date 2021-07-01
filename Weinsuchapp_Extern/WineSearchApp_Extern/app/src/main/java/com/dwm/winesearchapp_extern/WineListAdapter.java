package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.stream.Collectors;

public class WineListAdapter extends ArrayAdapter<WineListItem> {

    private final Context _context;
    private final int _listItemResourceId;

    public WineListAdapter(Context context, int listItemResourceId)
    {
        super(context, listItemResourceId);
        this._context = context;
        this._listItemResourceId = listItemResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = ((Activity) _context).getLayoutInflater();
        WineListItem wineListItem = getItem(position);

        View view = layoutInflater.inflate(_listItemResourceId, parent, false);
        RelativeLayout wineItemLayout = view.findViewById(R.id.wineItemLayout);

        TextView wineName = view.findViewById(R.id.txtViewWineName);
        wineName.setText(wineListItem.WineName);

        TextView producer = view.findViewById(R.id.txtViewProducer);
        String producerText = String.format(_context.getResources().getString(R.string.winelist_producer), wineListItem.Producer);
        producer.setText(producerText);

        TextView origin = view.findViewById(R.id.txtViewOrigin);
        String region = (wineListItem.Region == null ? "" : wineListItem.Region + ", ") + wineListItem.Country;
        String originText = String.format(_context.getResources().getString(R.string.winelist_origin), region);
        origin.setText(originText);

        TextView varietals = view.findViewById(R.id.txtViewVarietal);
        String varietalDesc = wineListItem.Varietal.size() > 1 ? _context.getString(R.string.winelist_varietals) : _context.getString(R.string.winelist_varietal);
        String varietalText = String.format(varietalDesc, wineListItem.Varietal.stream().collect(Collectors.joining(", ")));
        varietals.setText(varietalText);

        TextView award = view.findViewById(R.id.txtViewTrophy);
        if (wineListItem.Award != null) {
            String awardText = String.format(_context.getResources().getString(R.string.winelist_award), wineListItem.Award);
            award.setText(awardText);
        }

        wineItemLayout.setOnClickListener(v -> {
            Session.SetSelectedListItem(wineListItem);
            Intent intent = new Intent(_context, WinedetailsActivity.class);
            _context.startActivity(intent);
        });

        new Thread(() ->  {
            String[] imageNames = WineSearchServices.GetBottleImageNames(wineListItem.Id);
            if (imageNames != null && imageNames.length != 0) {
                for (String imageName : imageNames) {
                    if (imageName.endsWith("F")) {
                        Bitmap bottleImage = WineSearchServices.GetBottleImageType(wineListItem, "normal", "png", imageName);
                        ((Activity) _context).runOnUiThread(() ->
                                ((ImageView) view.findViewById(R.id.imageFront)).setImageBitmap(bottleImage)
                        );
                    }
                }
            }

            boolean medalEnabled = PreferenceManager.getDefaultSharedPreferences(_context.getApplicationContext()).getBoolean("medalPreview", true);
            if (Utils.HasAward(wineListItem.Ranking) && medalEnabled) {
                Bitmap medalImage = WineSearchServices.GetMedalImage(wineListItem.TrophyCode, wineListItem.Ranking);
                ((Activity) _context).runOnUiThread(() -> {
                        ((ImageView) view.findViewById(R.id.imageMedal)).setImageBitmap(medalImage);
                });
            }
        }).start();

        return view;
    }
}