package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView textView = v.findViewById(R.id.txtViewWineName);

        textView.setText(item.WineName);

        return v;
    }
}