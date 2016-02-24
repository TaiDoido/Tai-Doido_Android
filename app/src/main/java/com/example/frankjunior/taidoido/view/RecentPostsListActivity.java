package com.example.frankjunior.taidoido.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.connection.HttpUtil;
import com.example.frankjunior.taidoido.connection.PostHttp;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.Util;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class RecentPostsListActivity extends AppCompatActivity implements PostListAdapter.OnClickPostListener {

    private static ArrayList<Post> mPostList = new ArrayList<Post>();
    private final int LOADING = 0;
    private final int ERROR = 1;
    private final int INVSISIBLE = 2;
    private final int WITHOUT_CONNECTION = 3;
    private PostTask mTask;
    private PostListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeContainer;
    private boolean mLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mTextMensagem = (TextView) findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgress(INVSISIBLE);
                DispararTask();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RecentPostsListActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        PaginationHandle();

        mAdapter = new PostListAdapter(RecentPostsListActivity.this, mPostList);
        mAdapter.addOnClickPostListener(RecentPostsListActivity.this);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));

        // aqui são as validações pra disparar a task
        showProgress(LOADING);
        DispararTask();
    }

    // método que trata o evento de pagination
    private void PaginationHandle() {
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private int totalVisibleItem, totalItemCount, firstVisiblesItem;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //if scrolled down
                    if (dy > 0) {
                        totalVisibleItem = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        firstVisiblesItem = linearLayoutManager.findFirstVisibleItemPosition();

                        if (!mLoading && canScrollerLastItens()) {
                            onScrolledToLastItem();
                        }
                    }
                }

                private boolean canScrollerLastItens() {
                    return (totalVisibleItem + firstVisiblesItem) >= totalItemCount;
                }

                private void onScrolledToLastItem() {
                    addPaginationLoading();
                    // Handle fake, só pra testar o componente de loading
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Toast.makeText(RecentPostsListActivity.this, "carregou", Toast.LENGTH_SHORT).show();
                            removePaginationLoading();
                        }
                    }, 3000);
                    mLoading = true;
                }

                private void addPaginationLoading() {
                    mPostList.add(mPostList.size(), null);
                    mAdapter.notifyItemInserted(Util.getLastPositionFromList(mPostList));
                    mRecyclerView.swapAdapter(mAdapter, false);
                }

                private void removePaginationLoading() {
                    mPostList.remove(Util.getLastPositionFromList(mPostList));
                    mAdapter.notifyItemRemoved(mPostList.size());
                    mRecyclerView.swapAdapter(mAdapter, false);
                }
            });
        }
    }

    public void setLoaded() {
        mLoading = false;
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
            if (HttpUtil.hasConnectionAvailable(RecentPostsListActivity.this)) {
                startDownload();
            } else {
                showProgress(WITHOUT_CONNECTION);
            }
        }
    }

    private void startDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PostTask();
            mTask.execute();
        }
    }

    private void showProgress(int emptyType) {
        switch (emptyType) {
            case LOADING:
                mTextMensagem.setText(getString(R.string.progress_msg));
                mTextMensagem.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                mTextMensagem.setText(getString(R.string.posts_error));
                mTextMensagem.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                break;
            case WITHOUT_CONNECTION:
                mTextMensagem.setText(getString(R.string.without_connection));
                mTextMensagem.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                break;
            case INVSISIBLE:
                mTextMensagem.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                break;
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
        }

        @Override
        protected List<Post> doInBackground(Void... strings) {
            return PostHttp.loadRecentPosts();
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            if (posts != null) {
                showProgress(INVSISIBLE);
                mPostList.addAll(posts);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                mTask = null;
            } else {
                showProgress(ERROR);
            }
        }
    }
}
