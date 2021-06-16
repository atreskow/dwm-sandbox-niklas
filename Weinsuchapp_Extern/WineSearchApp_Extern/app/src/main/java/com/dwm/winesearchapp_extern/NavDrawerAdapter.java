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
    private final int layoutResourceId;
    private NavDrawerItem data[] = null;

    public NavDrawerAdapter(Context context, int layoutResourceId, NavDrawerItem[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = inflater.inflate(layoutResourceId, parent, false);

        TextView textView = (TextView) v.findViewById(R.id.navDrawerTextViewName);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.navDrawerCheckBox);

        NavDrawerItem choice = data[position];
        textView.setText(choice.name);

        Log.e("TEST", textView.getText().toString() + " " + checkBox.isChecked());

        return v;
    }
}