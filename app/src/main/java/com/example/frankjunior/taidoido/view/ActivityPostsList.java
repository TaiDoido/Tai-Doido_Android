package com.example.frankjunior.taidoido.view;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.connection.HttpUtil;
import com.example.frankjunior.taidoido.connection.PostHttp;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.MyLog;

import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ActivityPostsList extends AppCompatActivity implements PostListAdapter.AoClicarNoPostListener {

    private static final int COLUMNS_NUMBER = 2;
    private PostTask mTask;
    private PostListAdapter mAdapter;
    private RecyclerView mRecycleView;
    private TextView mTextMensagem;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        mTextMensagem = (TextView) findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(ActivityPostsList.this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new LinearLayoutManager(this);
        } else {
            mLayoutManager = new GridLayoutManager(this, COLUMNS_NUMBER);
        }
        mRecycleView.setLayoutManager(mLayoutManager);

        if (mTask == null) {
            if (HttpUtil.hasConnectionAvailable(ActivityPostsList.this)) {
                startDownload();
            } else {
                mTextMensagem.setText(getString(R.string.without_connection));
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            showProgress(true);
        }
    }

    private void showProgress(boolean exibir) {
        if (exibir) {
            mTextMensagem.setText(getString(R.string.progress_msg));
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void startDownload() {
        if (mTask == null ||  mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PostTask();
            mTask.execute();
        }
    }

    @Override
    public void aoClicarNoPost(View v, int position, Post post) {

    }

    class PostTask extends AsyncTask<Void, Void, List<Post>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<Post> doInBackground(Void... strings) {
            return PostHttp.carregarBlogJson();
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            showProgress(false);
            if (posts != null) {
                mAdapter = new PostListAdapter(ActivityPostsList.this, posts);
                mAdapter.setAoClicarNoPostListener(ActivityPostsList.this);
                mRecycleView.setAdapter(new ScaleInAnimationAdapter(mAdapter));

                for (int i = 0; i < posts.size(); i++){
                    MyLog.print("posts = "+posts.get(i).getTitle());
                    MyLog.print("author = "+posts.get(i).getAuthor());
                    MyLog.print("date = "+ posts.get(i).getDate());
                    MyLog.print("image = "+posts.get(i).getImage());
                    MyLog.print("========================");
                }
                mAdapter.notifyDataSetChanged();
            } else {
                mTextMensagem.setText(getString(R.string.posts_error));
            }
        }
    }
}
