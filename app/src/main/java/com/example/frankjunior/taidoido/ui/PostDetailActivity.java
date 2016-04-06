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

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.data.PostDAO;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.ui.fragment.PostDetailFragment;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends BaseActivity {

    public static final String EXTRA_POST = "extra_post_id";
    private static final int MENU_HOME = android.R.id.home;
    private static final int MENU_FAVORITE = R.id.menu_favorite;
    private ShareActionProvider mShareActionProvider;
    private PostDAO mDao;
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        mDao = PostDAO.getInstance(this);

        Intent intent = getIntent();
        if (intent != null) {
            String postId = intent.getStringExtra(EXTRA_POST);
            mPost = mDao.getPost(postId);
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

        menu.findItem(R.id.menu_favorite).setIcon(mPost.isFavorite() ?
                R.drawable.ic_action_favorite_menu :
                R.drawable.ic_action_favorite_menu_border);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HOME:
                ActivityCompat.finishAfterTransition(PostDetailActivity.this);
                break;
            case MENU_FAVORITE:
                menuFavoriteAction(item);
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

    /**
     * método responsável pela ação de Favoritar um post.
     *
     * @param item
     */
    private void menuFavoriteAction(MenuItem item) {
        mPost.setFavorite(!mPost.isFavorite());
        int icon;
        if (mPost.isFavorite()) {
            icon = R.drawable.ic_action_favorite_menu;
            mDao.update(mPost);
        } else {
            icon = R.drawable.ic_action_favorite_menu_border;
            mDao.update(mPost);
        }
        item.setIcon(icon);
    }
}
