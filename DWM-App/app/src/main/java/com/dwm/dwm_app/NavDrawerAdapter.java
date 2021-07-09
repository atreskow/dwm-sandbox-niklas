package com.dwm.dwm_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.color.MaterialColors;

import java.util.HashMap;
import java.util.List;

public class NavDrawerAdapter extends BaseExpandableListAdapter {

    private Activity _activity;
    private List<String> _facetHeaderList;
    private HashMap<String, List<NavDrawerItem>> _facetHeaderChildMap;

    public NavDrawerAdapter(Activity activity, List<String> facetHeaderList,
                            HashMap<String, List<NavDrawerItem>> facetHeaderChildMap) {
        this._activity = activity;
        this._facetHeaderList = facetHeaderList;
        this._facetHeaderChildMap = facetHeaderChildMap;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._facetHeaderChildMap.get(this._facetHeaderList.get(groupPosition)).get(childPosititon);
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
            LayoutInflater layoutInflater = (LayoutInflater) this._activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView childTextView = convertView.findViewById(R.id.navDrawerTextViewName);
        LinearLayout childLayout = convertView.findViewById(R.id.linearLayout);
        CheckBox childCheckBox = convertView.findViewById(R.id.navDrawerCheckBox);

        childLayout.setOnClickListener(view -> {
            //Listener ist auf Layout, weshalb das Checken manuell gesetzt werden muss
            childCheckBox.setChecked(!childCheckBox.isChecked());
            child.checked = childCheckBox.isChecked();

            if (child.checked) {
                Session.addFacetQueryGroupValue(child.value, child.name);
            }
            else {
                Session.removeFacetQueryGroupValue(child.value, child.name);
            }

            ((SearchActivity) _activity).startNewWineSearch();
        });

        String text = String.format(_activity.getResources().getString(R.string.navigation_drawer_item), child.name, child.count);

        childTextView.setText(text);
        childCheckBox.setChecked(child.checked);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._facetHeaderChildMap.get(this._facetHeaderList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._facetHeaderList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._facetHeaderList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public List<NavDrawerItem> getGroupList(int groupPosition) {
        return this._facetHeaderChildMap.get(this._facetHeaderList.get(groupPosition));
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

        if (containsChecked(groupPosition)) {
            lblListHeader.setTextColor(_activity.getResources().getColor(R.color.buttonPrimary, null));
        }
        else {
            lblListHeader.setTextColor(MaterialColors.getColor(_activity, R.attr.textColor, Color.BLACK));
        }

        return convertView;
    }

    private boolean containsChecked(int groupPosition) {
        List<NavDrawerItem> group = getGroupList(groupPosition);
        for (NavDrawerItem item : group) {
            if (item.checked) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }}
