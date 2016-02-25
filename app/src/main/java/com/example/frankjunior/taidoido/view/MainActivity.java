package com.example.frankjunior.taidoido.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.example.frankjunior.taidoido.R;

/**
 * Created by frankjunior on 25/02/16.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String EXTRA_PROGRAM_ID = MainActivity.class.getPackage().getName() + ".extra.PROGRAM_ID";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
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

    public void onFragmentCreateView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mNavigationDrawerFragment.setUp();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}