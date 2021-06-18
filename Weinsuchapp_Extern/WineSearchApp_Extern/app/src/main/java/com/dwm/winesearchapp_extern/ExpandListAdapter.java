package com.dwm.winesearchapp_extern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Activity _activity;
    private List<String> _listDataHeader;
    private HashMap<String, List<NavDrawerItem>> _listDataChild;

    public ExpandListAdapter(Activity activity, List<String> listDataHeader,
                             HashMap<String, List<NavDrawerItem>> listChildData) {
        this._activity = activity;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        NavDrawerItem child = (NavDrawerItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.navDrawerTextViewName);
        LinearLayout layoutChild = convertView.findViewById(R.id.linearLayout);
        CheckBox checkBoxChild = convertView.findViewById(R.id.navDrawerCheckBox);

        layoutChild.setOnClickListener(view -> {
            checkBoxChild.setChecked(!checkBoxChild.isChecked());

            child.Checked = checkBoxChild.isChecked();

            List<String> facetQueryGroupValue = new ArrayList<>();
            facetQueryGroupValue.add(child.Name);
            FacetQueryGroup facetQueryGroup = new FacetQueryGroup(child.Value, facetQueryGroupValue);

            if (child.Checked) {
                Session.AddFacetQueryGroupValue(facetQueryGroup);
            }
            else {
                Session.RemoveFacetQueryGroupValue(facetQueryGroup);
            }

            ((SearchActivity) _activity).startWineSearchThread();
        });

        String text = String.format(_activity.getResources().getString(R.string.navigation_drawer_item), child.Name, child.Count);

        txtListChild.setText(text);
        checkBoxChild.setChecked(child.Checked);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawer_list_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.navDrawerTextViewName);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }}
