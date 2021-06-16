package com.dwm.winesearchapp_extern;

public class NavDrawerItem
{
    public boolean isHeader;

    public String name;
    public int value;
    public boolean checked;

    public NavDrawerItem(boolean isHeader, String name, int value)
    {
        this.isHeader = isHeader;
        this.name = name;
        this.value = value;
    }
}