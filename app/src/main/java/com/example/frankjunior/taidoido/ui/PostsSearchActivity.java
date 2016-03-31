package com.example.frankjunior.taidoido.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.ui.fragment.PostsListFragment;
import com.example.frankjunior.taidoido.util.MyLog;
import com.example.frankjunior.taidoido.util.Util;

public class PostsSearchActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Util.isInternetConnected(this)) {
            Toast.makeText(getApplicationContext(), R.string.without_connection, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_posts_search);
        customizeToolbar();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            MyLog.print("Searching for:" + query);

            mToolbar.setTitle(query);
            changeFragment(PostsListFragment.newInstance(query));
        }
    }

    private void customizeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
