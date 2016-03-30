package com.example.frankjunior.taidoido.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.ui.fragment.PostDetailFragment;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends BaseActivity {

    public static final String EXTRA_POST = "extra_object";
    private static final int MENU_HOME = android.R.id.home;
    private static final int MENU_FAVORITE = R.id.menu_favorite;
    private Post mPost;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        if (intent != null) {
            mPost = (Post) intent.getSerializableExtra(EXTRA_POST);
            customizeToolbar();
            changeFragment(PostDetailFragment.newInstance(mPost));
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        actionShareButton(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HOME:
                ActivityCompat.finishAfterTransition(PostDetailActivity.this);
                break;
            case MENU_FAVORITE:
                Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

     /*
     **********************************************
     *   Métodos private
     **********************************************
     */
    /**
     * Customize the toolbar
     */
    private void customizeToolbar() {
        if (mPost.getTitle() != null) {
            setTitle(mPost.getTitle());
        } else {
            int barTitle = R.string.app_name;
            setTitle(getResources().getString(barTitle));
        }

        ImageView featuredImage = (ImageView) findViewById(R.id.imgPost);
        Picasso.with(this).load(mPost.getImage()).into(featuredImage);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getTitle());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Método que executa a ação de share.
     *
     * @param menu
     */
    private void actionShareButton(Menu menu) {
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(createShareIntent());
    }

    /**
     * Método que cria a intent de compartilhamento
     *
     * @return
     */
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.share_intent_type));
        shareIntent.putExtra(Intent.EXTRA_TEXT, mPost.getUrl());
        return shareIntent;
    }

}
