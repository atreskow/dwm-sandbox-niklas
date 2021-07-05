package com.dwm.winesearchapp_extern;

public class NavDrawerItem
{
    public String name;
    public String value;
    public int count;
    public boolean checked;

    public NavDrawerItem(String name, String value, int count)
    {
        this.name = name;
        this.value = value;
        this.count = count;
    }
}