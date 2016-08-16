package com.sip.ceffortless.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sip.ceffortless.R;
import com.sip.ceffortless.model.MenuItemCustomize;

import java.util.List;

/**
 * Created by ssd on 8/15/16.
 */
public class MenuAdapter extends ArrayAdapter <MenuItemCustomize> {

    private List<MenuItemCustomize> menuItems;
    private Context mContext;
    private int resourceId;
    public MenuAdapter(Context context, int resource, List<MenuItemCustomize> objects) {
        super(context, resource, objects);
        mContext = context;
        menuItems = objects;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        ImageView iconMenu = (ImageView)v.findViewById(R.id.icon_menu_left);
        TextView menuTitle = (TextView)v.findViewById(R.id.menu_title);
        menuTitle.setText(menuItems.get(position).getmMenuTitle());
        iconMenu.setImageResource(menuItems.get(position).getmIconResource());
        if(menuItems.get(position).isHasNewLesson()){
            TextView iconNotification = (TextView) v.findViewById(R.id.notification);
            iconNotification.setVisibility(View.VISIBLE);
        }
        return v;
    }

    @Override
    public int getCount() {
        if(menuItems!=null){
            return  menuItems.size();
        }
        return   0;
    }
}
