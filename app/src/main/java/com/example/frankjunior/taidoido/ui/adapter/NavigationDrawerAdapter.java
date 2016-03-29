package com.example.frankjunior.taidoido.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.DrawerListItem;

import java.util.List;

/**
 * Created by frankjunior on 01/03/16.
 * Adapter do @NavigationDrawerFragment
 */
public class NavigationDrawerAdapter extends ArrayAdapter<DrawerListItem> {

    private static final int VIEW_TYPE_COUNT = 4;
    private static final int VIEW_TYPE_SELECTABLE = 0;
    private static final int VIEW_TYPE_SEPARATOR = 1;
    private static final int VIEW_TYPE_UNSELECTABLE = 2;
    private static final int VIEW_TYPE_LABEL = 3;

    final private LayoutInflater mInflater;
    final private int mSelectableBackground;
    private List<DrawerListItem> mMenuItens;

    public NavigationDrawerAdapter(Context context, List<DrawerListItem> menuItens) {
        super(context, 0);
        mMenuItens = menuItens;
        mInflater = LayoutInflater.from(context);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.activatedBackgroundIndicator, outValue, true);
        mSelectableBackground = outValue.resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SEPARATOR:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_separator_navigation_drawer, parent, false);
                }
                break;
            case VIEW_TYPE_SELECTABLE:
            case VIEW_TYPE_UNSELECTABLE:
                ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_navigation_drawer, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DrawerListItem item = getItem(position);
                holder.iconView.setImageResource(item.iconResource);
                holder.textView.setText(item.textTitle);
                if (viewType == VIEW_TYPE_SELECTABLE) {
                    convertView.setBackgroundResource(mSelectableBackground);
                } else {
                    convertView.setBackgroundResource(0);
                }
                break;
            case VIEW_TYPE_LABEL:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_label_navigation_drawer, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DrawerListItem itemLabel = getItem(position);
                holder.textView.setText(itemLabel.textLabel);
                break;
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mMenuItens.size();
    }

    @Override
    public DrawerListItem getItem(int position) {
        return mMenuItens.get(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != VIEW_TYPE_SEPARATOR;
    }

    @Override
    public int getItemViewType(int position) {
        DrawerListItem item = getItem(position);
        if (item.textLabel != null) {
            return VIEW_TYPE_LABEL;
        }
        if (!item.clickable) {
            return VIEW_TYPE_SEPARATOR;
        }
        if (item.selectable) {
            return VIEW_TYPE_SELECTABLE;
        }
        return VIEW_TYPE_UNSELECTABLE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /*
    **********************************************
    *   Classes ViewHolder
    **********************************************
    */
    private class ViewHolder {

        final ImageView iconView;
        final TextView textView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.icon);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
