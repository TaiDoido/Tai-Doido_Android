package com.example.frankjunior.taidoido.view;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.connection.HttpUtil;
import com.example.frankjunior.taidoido.connection.PostHttp;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.MyLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by frankjunior on 15/01/16.
 */
public class PostsListFragment extends Fragment {

    private PostTask mTask;
    private ArrayAdapter<Post> mAdapter;
    private List<Post> mPosts;
    private ListView mListView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_posts_list, null);
        mTextMensagem = (TextView)layout.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar)layout.findViewById(R.id.progressBar);
        mListView = (ListView)layout.findViewById(R.id.list);
        mListView.setEmptyView(mTextMensagem);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mPosts == null) {
            mPosts = new ArrayList<Post>();
        }
        mAdapter = new PostListAdapter(getActivity(), mPosts);
        mListView.setAdapter(mAdapter);

        if (mTask == null) {
            if (HttpUtil.hasConnectionAvailable(getActivity())) {
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
                mPosts.clear();
                mPosts.addAll(posts);
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
