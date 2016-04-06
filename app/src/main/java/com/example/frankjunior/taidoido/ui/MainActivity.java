package com.example.frankjunior.taidoido.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.data.PostDAO;
import com.example.frankjunior.taidoido.model.Category;
import com.example.frankjunior.taidoido.model.DrawerListItem;
import com.example.frankjunior.taidoido.ui.fragment.FavoriteListFragment;
import com.example.frankjunior.taidoido.ui.fragment.NavigationDrawerFragment;
import com.example.frankjunior.taidoido.ui.fragment.PostsListFragment;
import com.example.frankjunior.taidoido.util.MyLog;

import java.util.List;

/**
 * Created by frankjunior on 25/02/16.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int DURATION = 10000;
    private DrawerLayout mDrawerLayout;
    private List<Category> mCategories;
    private Toolbar mToolbar;
    private PostDAO mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDao = PostDAO.getInstance(this);
        customizeToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_list, menu);
        actionSearchButton(menu);
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(ListView drawerListView, int position) {
        Fragment fragment = null;
        // se a ListView do NavigationDrawer não foi carregada ainda, carregue a tela padrão: RecentPostListFragment
        // ou seja, essa é a tela padrão, que abre assim que o app for aberto.
        if (drawerListView == null) {
            fragment = PostsListFragment.newInstance();
            // se não... carregue a tela do item clicado
        } else {
            DrawerListItem selectedDrawerListItem = (DrawerListItem) drawerListView.getItemAtPosition(position);
            if (selectedDrawerListItem.textTitle != null) {
                fragment = getFragmentScreen(selectedDrawerListItem.textTitle);
            }
        }
        changeScreen(fragment);
    }

    @Override
    public void onGetCategoriesList(List<Category> categories) {
        mCategories = categories;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDao.deleteAllCache();
    }

    /*
     **********************************************
     *   Métodos private
     **********************************************
     */
    /**
     * Método usado em forma de callback, pelo @BaseFragment
     * que por sua vez cada fragment do NavigationDrawer deve extender.
     * Sendo assim, a Toolbar é configurada pra cada fragment através desse método
     */
    private void customizeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Método usado para selecionar o fragment correspondente ao clique do item
     * do NavigationDrawer
     * @param selectedItem nome do item selecionado
     * @return Fragment preenchido, ou null caso dê errado
     */
    private Fragment getFragmentScreen(String selectedItem) {
        Fragment fragment = null;
        mToolbar.setTitle(selectedItem);
        if (selectedItem.equals(NavigationDrawerFragment.RECENT_POSTS)) {
            fragment = PostsListFragment.newInstance();
        } else if (selectedItem.equals(NavigationDrawerFragment.FAVORITES)) {
            fragment = FavoriteListFragment.newInstance();
        } else if (selectedItem.equals(NavigationDrawerFragment.SETTINGS)) {
            MyLog.print("cliquei em Settings");
        } else if (selectedItem.equals(NavigationDrawerFragment.ABOUT)) {
            MyLog.print("cliquei em About");
        } else {
            fragment = categoriesClickHandle(selectedItem);
        }
        return fragment;
    }

    private Fragment categoriesClickHandle(String selectedItem) {
        Fragment fragment = null;
        if (mCategories != null) {
            for (int i = 0; i < mCategories.size(); i++) {
                String categoryTitle = mCategories.get(i).getTitle();
                if (categoryTitle.equals(selectedItem)) {
                    fragment = PostsListFragment.newInstance(mCategories.get(i));
                    break;
                }
            }
        }
        return fragment;
    }

    /**
     * Método usado pra trocar o fragment na tela.
     * @param fragment preenchido, pronto pra ser exibido na tela.
     */
    private void changeScreen(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.abc_fade_in,
                            R.anim.abc_fade_out,
                            R.anim.abc_fade_in,
                            R.anim.abc_fade_out)
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    private void setupAnimations() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(DURATION);
        getWindow().setExitTransition(changeBounds);
        getWindow().setEnterTransition(changeBounds);
    }

    /**
     * Método que executa a ação de search.
     *
     * @param menu
     */
    private void actionSearchButton(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName searchableInfo = new ComponentName(this, PostsSearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(searchableInfo));
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
