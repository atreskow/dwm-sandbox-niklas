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

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem>
{
    private final Context context;
    private final int layoutResourceIdHeader;
    private final int layoutResourceIdItem;
    private NavDrawerItem data[];

    public NavDrawerAdapter(Context context, int layoutResourceIdHeader, int layoutResourceIdItem, NavDrawerItem[] data)
    {
        super(context, layoutResourceIdHeader, layoutResourceIdItem, data);
        this.context = context;
        this.layoutResourceIdHeader = layoutResourceIdHeader;
        this.layoutResourceIdItem = layoutResourceIdItem;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        NavDrawerItem item = data[position];

        if (item.isHeader) {
            View v = inflater.inflate(layoutResourceIdHeader, parent, false);
            TextView textView = v.findViewById(R.id.navDrawerTextViewName);

            textView.setText(item.name);

            return v;
        }
        else {
            View v = inflater.inflate(layoutResourceIdItem, parent, false);
            TextView textView = v.findViewById(R.id.navDrawerTextViewName);

            String text = String.format(context.getResources().getString(R.string.navigation_drawer_item), item.name, item.value);
            textView.setText(text);

            return v;
        }
    }
}