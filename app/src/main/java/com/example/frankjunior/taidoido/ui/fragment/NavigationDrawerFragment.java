package com.example.frankjunior.taidoido.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.controller.RequestController;
import com.example.frankjunior.taidoido.model.Category;
import com.example.frankjunior.taidoido.model.DrawerListItem;
import com.example.frankjunior.taidoido.ui.adapter.NavigationDrawerAdapter;
import com.example.frankjunior.taidoido.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 25/02/16.
 */
public class NavigationDrawerFragment extends BaseFragment implements BaseFragment.ConnectionListener {

    public static final int RECENT_POSTS_ITEM = 0;
    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    // constantes para construção dos itens da lista
    private static final boolean CLICKABLE_ON = true;
    private static final boolean CLICKABLE_OFF = false;
    private static final int ICON_HOME = R.drawable.ic_action_home;
    private static final int ICON_FAVORITE = R.drawable.ic_action_favorite;
    private static final int ICON_CATEGORY = R.drawable.ic_action_label;
    private static final int ICON_SETTINGS = R.drawable.ic_action_settings;
    private static final int ICON_ABOUT = R.drawable.ic_action_info;
    public static String RECENT_POSTS = App.getContext().getString(R.string.navigation_drawer_recent_posts);
    public static String FAVORITES = App.getContext().getString(R.string.navigation_drawer_favorites);
    public static String SETTINGS = App.getContext().getString(R.string.navigation_drawer_settings);
    public static String ABOUT = App.getContext().getString(R.string.navigation_drawer_about);
    private static String LABEL_CATEGORY = App.getContext().getString(R.string.navigation_drawer_category_label);

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition = RECENT_POSTS_ITEM;
    private boolean mUserLearnedDrawer;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mFromSavedInstanceState;
    private ListView mDrawerListView;
    private NavigationDrawerAdapter mNavigationDrawerAdapter;
    private List<DrawerListItem> mNavigationDrawerMenuItens = new ArrayList<DrawerListItem>();
    private RequestController mRequestController;
    private CategoryTask mTask;

    // Construtor
    public NavigationDrawerFragment() {
        mRequestController = RequestController.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mCallbacks = (NavigationDrawerCallbacks) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConnectionListener(this);
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
        mNavigationDrawerMenuItens = fillItensList(null);
        mNavigationDrawerAdapter = new NavigationDrawerAdapter(getActivity(), mNavigationDrawerMenuItens);
        mDrawerListView.setAdapter(mNavigationDrawerAdapter);

        setItemChecked(mCurrentSelectedPosition);
        dispararTask();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
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

    /*
     **********************************************
     *   Métodos private
     **********************************************
     */
    private void dispararTask() {
        if (mTask == null) {
            if (Util.isInternetConnected(getActivity())) {
                startDownload();
            }
        }
    }

    private void startDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new CategoryTask();
            mTask.execute();
        }
    }

    /**
     * Método para preencher os itens do array que será passado pro @NavigationDrawerAdapter.
     *
     * @param categoryItems - itens da categoria que vem preenchido do AsyncTask
     * @return array de itens completamente preenchido
     */
    private List<DrawerListItem> fillItensList(List<DrawerListItem> categoryItems) {
        List<DrawerListItem> menuItemList = new ArrayList<DrawerListItem>();
        // Adiciona os primeiros itens estáticos no array.
        menuItemList.add(new DrawerListItem(CLICKABLE_ON, ICON_HOME, RECENT_POSTS));
        menuItemList.add(new DrawerListItem(CLICKABLE_ON, ICON_FAVORITE, FAVORITES));

        // se o categoryItems não for nulo, adicione o conteúdo dele no array.
        if (categoryItems != null) {
            menuItemList.add(new DrawerListItem()); // separador
            menuItemList.add(new DrawerListItem(LABEL_CATEGORY));
            menuItemList.addAll(categoryItems);
            menuItemList.add(new DrawerListItem()); // separador
        }
        // depois de adicionar o conteúdo do categoryItems, adiciona os ultimos itens estáticos.
        // TODO: Por enquanto, o item de "SETTINGS" estará comentado. Talvez ele só apareça na versão 2
//        menuItemList.add(new DrawerListItem(CLICKABLE_OFF, ICON_SETTINGS, SETTINGS));
        menuItemList.add(new DrawerListItem(CLICKABLE_OFF, ICON_ABOUT, ABOUT));

        return menuItemList;
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

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(mDrawerListView, position);
        }
    }

    private void setItemChecked(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        }
    }

    @Override
    public void onConnectionChanged(boolean hasConnection) {
        if (hasConnection) {
            dispararTask();
        }
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(ListView mDrawerListView, int position);
        void onGetCategoriesList(List<Category> categories);
    }

    /**
     * AsyncTask pra pegar a lista de categorias e preencher o NavigationDrawer
     */
    class CategoryTask extends AsyncTask<Void, Void, List<Category>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Category> doInBackground(Void... strings) {
            return mRequestController.loadCategoryList();
        }

        @Override
        protected void onPostExecute(List<Category> posts) {
            super.onPostExecute(posts);
            List<DrawerListItem> menuItemList = new ArrayList<DrawerListItem>();
            if (posts != null) {
                if (mCallbacks != null) {
                    mCallbacks.onGetCategoriesList(posts);
                }
                for (int i = 0; i < posts.size(); i++) {
                    menuItemList.add(new DrawerListItem(CLICKABLE_ON, ICON_CATEGORY, posts.get(i).getTitle()));
                }
                mNavigationDrawerMenuItens = fillItensList(menuItemList);
            }
            // atualizando o adapter e o "position" da lista
            mNavigationDrawerAdapter = new NavigationDrawerAdapter(getActivity(), mNavigationDrawerMenuItens);
            mDrawerListView.setAdapter(mNavigationDrawerAdapter);
            mNavigationDrawerAdapter.notifyDataSetChanged();
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    userLearnedDrawer();
                    selectItem(position);
                }
            });
            setItemChecked(mCurrentSelectedPosition);
        }
    }
}
