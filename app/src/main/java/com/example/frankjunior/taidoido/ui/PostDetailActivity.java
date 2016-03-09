package com.example.frankjunior.taidoido.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.ui.fragment.PostDetailFragment;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "extra_object";
    private static final int MENU_HOME = android.R.id.home;
    private static final int MENU_FAVORITE = R.id.favorite;
    private static final int MENU_SHARE = R.id.share;
    private Post mPost;

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
            case MENU_SHARE:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
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
        // Set the status bar to dark-semi-transparentish
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

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

    /***
     * Change the given fragment on current Activity
     *
     * @param fragment
     */
    private void changeFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag(fragment.getClass().toString()) != null) {
            fragmentTransaction.add(R.id.place_holder, fragment, fragment.getClass().toString());
        } else {
            fragmentTransaction.replace(R.id.place_holder, fragment, fragment.getClass().toString());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
