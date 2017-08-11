package com.android.settings.smartawake;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import com.android.settings.R;

public class MyListAdapter extends ArrayAdapter<Item> {
    
    private List<Item> items;
    private LayoutInflater inflater;
    public enum RowType{
        APP_LIST_ITEM, ANIM_ITEM;
    }

    public MyListAdapter(Context context, LayoutInflater inflater, List<Item> items){
        super(context,0,items);
        this.items = items;
        this.inflater = inflater;
    }
    
    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }
    
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).getView(inflater, convertView,position);
    }

}
