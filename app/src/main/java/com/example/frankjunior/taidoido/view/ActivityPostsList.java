package com.example.frankjunior.taidoido.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.connection.HttpUtil;
import com.example.frankjunior.taidoido.connection.PostHttp;
import com.example.frankjunior.taidoido.model.Post;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ActivityPostsList extends AppCompatActivity implements PostListAdapter.OnClickPostListener {

    private final int EMPTY_LOADING = 0;
    private final int EMPTY_ERROR = 1;
    private final int EMPTY_INVSISIBLE = 2;
    private final int EMPTY_WITHOUT_CONNECTION = 3;
    private PostTask mTask;
    private PostListAdapter mAdapter;
    private RecyclerView mRecycleView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mTextMensagem = (TextView) findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new SlideInLeftAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityPostsList.this);
        mRecycleView.setLayoutManager(layoutManager);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DispararTask();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // aqui são as validações pra disparar a task
        DispararTask();
    }

    /*
      **********************************************
      *   Click da lista
      **********************************************
      */
    @Override
    public void onClickPost(View v, int position, Post post) {
        //TODO: tratar aqui o click da lista
    }

    /*
      **********************************************
      *   Métodos private
      **********************************************
      */
    private void DispararTask() {
        if (mTask == null) {
            if (HttpUtil.hasConnectionAvailable(ActivityPostsList.this)) {
                startDownload();
            } else {
                showEmpty(EMPTY_WITHOUT_CONNECTION);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            showProgress(true);
        }
    }

    private void showProgress(boolean isShow) {
        mProgressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showEmpty(int emptyType) {
        switch (emptyType) {
            case EMPTY_LOADING:
                mTextMensagem.setText(getString(R.string.progress_msg));
                mTextMensagem.setVisibility(View.VISIBLE);
                break;
            case EMPTY_ERROR:
                mTextMensagem.setText(getString(R.string.posts_error));
                mTextMensagem.setVisibility(View.VISIBLE);
                break;
            case EMPTY_WITHOUT_CONNECTION:
                mTextMensagem.setText(getString(R.string.without_connection));
                mTextMensagem.setVisibility(View.VISIBLE);
                break;
            case EMPTY_INVSISIBLE:
                mTextMensagem.setVisibility(View.GONE);
                break;
        }
    }

    private void startDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PostTask();
            mTask.execute();
        }
    }

    /*
      **********************************************
      *   AsyncTask pra pegar os posts inicialmente
      **********************************************
      */
    class PostTask extends AsyncTask<Void, Void, List<Post>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            showEmpty(EMPTY_LOADING);
        }

        @Override
        protected List<Post> doInBackground(Void... strings) {
            return PostHttp.loadBlogJson();
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            showProgress(false);
            if (posts != null) {
                showEmpty(EMPTY_INVSISIBLE);
                mAdapter = new PostListAdapter(ActivityPostsList.this, posts);
                mAdapter.addOnClickPostListener(ActivityPostsList.this);
                mRecycleView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                swipeContainer.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                mTask = null;
            } else {
                showEmpty(EMPTY_ERROR);
            }
        }
    }
}
