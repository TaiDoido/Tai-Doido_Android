package com.example.frankjunior.taidoido.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.ui.fragment.NavigationDrawerFragment;
import com.example.frankjunior.taidoido.ui.fragment.RecentPostsListFragment;
import com.example.frankjunior.taidoido.util.MyLog;

/**
 * Created by frankjunior on 25/02/16.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String EXTRA_PROGRAM_ID = MainActivity.class.getPackage().getName() + ".extra.PROGRAM_ID";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_list, menu);
        actionSearchButton(menu);
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        MyLog.print("click position = " + position);
        Bundle params = getIntent().getExtras();
        Fragment fragment = null;
        switch (position) {
            case NavigationDrawerFragment.RECENT_POSTS_ITEM:
                if (params != null && params.containsKey(EXTRA_PROGRAM_ID)) {
                    fragment = RecentPostsListFragment.newInstance(params.getLong(EXTRA_PROGRAM_ID));
                } else {
                    fragment = new RecentPostsListFragment();
                }
                break;
            case NavigationDrawerFragment.FAVORITES_ITEM:

                break;
            case NavigationDrawerFragment.SETTINGS_ITEM:

                break;
            case NavigationDrawerFragment.ABOUT_ITEM:

                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Método usado em forma de callback, pelo @BaseFragment
     * que por sua vez cada fragment do NavigationDrawer deve extender.
     * Sendo assim, a Toolbar é configurada pra cada fragment através desse método
     *
     * @param view View inflada no Fragment
     */
    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mNavigationDrawerFragment.setUp();
    }

    /*
     **********************************************
     *   Métodos private
     **********************************************
     */
    private void setupAnimations() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(10000);
        getWindow().setExitTransition(changeBounds);
        getWindow().setEnterTransition(changeBounds);
    }

    /**
     * Método que executa a ação de search.
     *
     * @param menu
     */
    private void actionSearchButton(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.menu_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
