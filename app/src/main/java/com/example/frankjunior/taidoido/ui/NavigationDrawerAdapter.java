package com.example.frankjunior.taidoido.ui;

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

/**
 * Created by frankjunior on 01/03/16.
 * Adapter do @NavigationDrawerFragment
 */
public class NavigationDrawerAdapter extends ArrayAdapter<DrawerListItem> {

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_SELECTABLE = 0;
    private static final int VIEW_TYPE_SEPARATOR = 1;
    private static final int VIEW_TYPE_UNSELECTABLE = 2;

    // constantes para construção dos itens da lista
    private static final boolean CLICKABLE_ON = true;
    private static final boolean CLICKABLE_OFF = false;
    private static final int ICON_HOME = R.drawable.ic_action_home;
    private static final int ICON_FAVORITE = R.drawable.ic_action_favorite;
    private static final int ICON_CATEGORY = R.drawable.ic_action_label;
    private static final int ICON_SETTINGS = R.drawable.ic_action_settings;
    private static final int ICON_ABOUT = R.drawable.ic_action_info;

    private static final int RECENT_POSTS = R.string.navigation_drawer_recent_posts;
    private static final int FAVORITES = R.string.navigation_drawer_favorites;
    private static final int STRING_FAKE = R.string.navigation_drawer_fake;
    private static final int SETTINGS = R.string.navigation_drawer_settings;
    private static final int ABOUT = R.string.navigation_drawer_about;

    private static final DrawerListItem[] ITEMS = new DrawerListItem[]{
            new DrawerListItem(CLICKABLE_ON, ICON_HOME, RECENT_POSTS),
            new DrawerListItem(CLICKABLE_ON, ICON_FAVORITE, FAVORITES),

            new DrawerListItem(),

            new DrawerListItem(CLICKABLE_OFF, ICON_CATEGORY, STRING_FAKE),
            new DrawerListItem(CLICKABLE_OFF, ICON_CATEGORY, STRING_FAKE),
            new DrawerListItem(CLICKABLE_OFF, ICON_CATEGORY, STRING_FAKE),

            new DrawerListItem(),

            new DrawerListItem(CLICKABLE_OFF, ICON_SETTINGS, SETTINGS),
            new DrawerListItem(CLICKABLE_OFF, ICON_ABOUT, ABOUT)
    };

    final private LayoutInflater mInflater;
    final private int mSelectableBackground;

    public NavigationDrawerAdapter(Context context) {
        super(context, 0, ITEMS);
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
                    convertView = mInflater.inflate(R.layout.list_item_separator_navigation_drawer, parent, false);
                }
                break;
            case VIEW_TYPE_SELECTABLE:
            case VIEW_TYPE_UNSELECTABLE:
                ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_item_navigation_drawer, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DrawerListItem item = getItem(position);
                holder.iconView.setImageResource(item.iconResource);
                holder.textView.setText(item.textResource);
                if (viewType == VIEW_TYPE_SELECTABLE) {
                    convertView.setBackgroundResource(mSelectableBackground);
                } else {
                    convertView.setBackgroundResource(0);
                }
                break;
        }
        return convertView;
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

    private class ViewHolder {

        final ImageView iconView;
        final TextView textView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.icon);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
