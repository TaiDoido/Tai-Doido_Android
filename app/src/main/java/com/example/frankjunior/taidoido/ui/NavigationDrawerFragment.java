package com.example.frankjunior.taidoido.ui;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;

/**
 * Created by frankjunior on 25/02/16.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final int RECENT_POSTS_ITEM = 0;
    public static final int FAVORITES_ITEM = 1;
    public static final int SEPARATOR_ITEM = 2;
    public static final int SETTINGS_ITEM = 3;
    public static final int ABOUT_ITEM = 4;

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mFromSavedInstanceState;
    private ListView mDrawerListView;
    private DrawerListAdapter mDrawerListAdapter;

    public NavigationDrawerFragment() {

    }

    private void userLearnedDrawer() {
        if (!mUserLearnedDrawer) {
            // The user manually opened the drawer; store this flag to prevent auto-showing
            // the navigation drawer automatically in the future.
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }
    }

    public void setUp() {
        setUp(mCurrentSelectedPosition);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     */
    public void setUp(int position) {
        mCurrentSelectedPosition = position;
        // The android:id of this fragment in its activity's layout.
        int fragmentId = R.id.navigation_drawer;
        // The DrawerLayout containing this fragment's UI.
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                userLearnedDrawer();
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) rootView.findViewById(R.id.option_list);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userLearnedDrawer();
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(mDrawerListAdapter = new DrawerListAdapter(getActivity()));
        setItemChecked(mCurrentSelectedPosition);
        return rootView;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    private void setItemChecked(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private static class DrawerListItem {

        final boolean clickable;
        final boolean selectable;
        final int iconResource;
        final int textResource;

        private DrawerListItem() {
            this.clickable = false;
            this.selectable = false;
            this.iconResource = 0;
            this.textResource = 0;
        }

        private DrawerListItem(boolean selectable, int iconResource, int textResource) {
            this.clickable = true;
            this.selectable = selectable;
            this.iconResource = iconResource;
            this.textResource = textResource;
        }
    }

    private static class DrawerListAdapter extends ArrayAdapter<DrawerListItem> {

        private static final int VIEW_TYPE_COUNT = 3;
        private static final int VIEW_TYPE_SELECTABLE = 0;
        private static final int VIEW_TYPE_SEPARATOR = 1;
        private static final int VIEW_TYPE_UNSELECTABLE = 2;

        private static final DrawerListItem[] ITEMS = new DrawerListItem[]{
                new DrawerListItem(true, R.drawable.ic_action_home, R.string.navigation_drawer_recent_posts),
                new DrawerListItem(true, R.drawable.ic_action_favorite, R.string.navigation_drawer_favorites),

                new DrawerListItem(),

                new DrawerListItem(false, R.drawable.ic_action_label, R.string.navigation_drawer_fake),
                new DrawerListItem(false, R.drawable.ic_action_label, R.string.navigation_drawer_fake),
                new DrawerListItem(false, R.drawable.ic_action_label, R.string.navigation_drawer_fake),

                new DrawerListItem(),

                new DrawerListItem(false, R.drawable.ic_action_settings, R.string.navigation_drawer_settings),
                new DrawerListItem(false, R.drawable.ic_action_info, R.string.navigation_drawer_about)
        };

        final private LayoutInflater mInflater;
        final private int mSelectableBackground;

        public DrawerListAdapter(Context context) {
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

}
