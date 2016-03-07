package com.example.frankjunior.taidoido.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "extra_object";
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

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
    protected void changeFragment(@NonNull Fragment fragment) {
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
