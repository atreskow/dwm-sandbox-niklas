package com.dwm.winesearchapp_extern;

public class NavDrawerItem
{
    public String Name;
    public String Value;
    public int Count;
    public boolean Checked;

    public NavDrawerItem(String name, String value, int count)
    {
        this.Name = name;
        this.Value = value;
        this.Count = count;
    }
}