package com.example.frankjunior.taidoido.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.controller.RequestController;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.ui.PostDetailActivity;
import com.example.frankjunior.taidoido.ui.adapter.PostListAdapter;
import com.example.frankjunior.taidoido.util.Util;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class PostsListFragment extends BaseFragment implements
        PostListAdapter.OnClickPostListener,
        BaseFragment.ConnectionListener {

    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_TITLE = "category_title";
    private static ArrayList<Post> mPostList = new ArrayList<Post>();
    private final int LOADING = 0;
    private final int ERROR = 1;
    private final int INVSISIBLE = 2;
    private final int WITHOUT_CONNECTION = 3;
    private final int FIRST_PAGE = 1;
    private PostTask mTask;
    private PostListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mTextMensagem;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeContainer;
    private boolean mLoading = false;
    private boolean isPagination = false;
    private int mRecentPostsCurrentPage = FIRST_PAGE;
    private RequestController mRequestController;
    private LinearLayout mRootPostsList;
    private boolean isPaused = false;
    private String mCategoryId;

    public static PostsListFragment newInstance(String categoryId) {
        PostsListFragment fragment = new PostsListFragment();
        if (categoryId != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_CATEGORY_ID, categoryId);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestController = new RequestController();
        setConnectionListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mCategoryId = arguments.getString(KEY_CATEGORY_ID);
        }

        View view = inflater.inflate(R.layout.fragment_recent_posts_list, container, false);
        mRootPostsList = (LinearLayout) view.findViewById(R.id.rootPostsList);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mTextMensagem = (TextView) view.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgress(INVSISIBLE);
                resetRequest();
                dispararTask();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_yellow,
                R.color.google_red);

        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        PaginationHandle();

        mAdapter = new PostListAdapter(getActivity(), mPostList);
        mAdapter.addOnClickPostListener(PostsListFragment.this);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));

        // aqui são as validações pra disparar a task
        showProgress(LOADING);
        resetRequest();
        dispararTask();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    /**
     * Evento que trata a falta [ou a volta] de conectividade
     *
     * @param hasConnection
     */
    @Override
    public void onConnectionChanged(boolean hasConnection) {
        if (!isPaused) {
            if (!hasConnection) {
                showConnectionSnackBar();
            } else {
                showProgress(LOADING);
                resetRequest();
                dispararTask();
            }
        }
    }

    /*
      **********************************************
      *   Click da lista
      **********************************************
      */
    @Override
    public void onClickPost(View v, int position, Post post) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST, post);

        Pair<View, String> p1 = Pair.create(v.findViewById(R.id.imgPost), getString(R.string.post_image_transition_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    /*
      **********************************************
      *   Métodos private
      **********************************************
      */

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
                    // Se chegou na ultima pagina, retorne false
                    if (mRecentPostsCurrentPage < mRequestController.getTotalPages()) {
                        return (totalVisibleItem + firstVisiblesItem) >= totalItemCount;
                    } else {
                        return false;
                    }
                }

                private void onScrolledToLastItem() {
                    addPaginationLoading();
                    mRequestController.setPageNumber(mRecentPostsCurrentPage);
                    isPagination = true;
                    dispararTask();
                    mLoading = true;
                }
            });
        }
    }

    /**
     * Método pra exibir um SnackBar, quando tiver sem conexão
     */
    private void showConnectionSnackBar() {
        Snackbar.make(mRootPostsList, R.string.no_connection_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgress(LOADING);
                        resetRequest();
                        dispararTask();
                    }
                }).show();
    }

    /**
     * método usado para resetar valores iniciais,
     * usado no fisrt laoding e no pullToRefresh
     */
    private void resetRequest() {
        isPagination = false;
        mPostList.clear();
        mRecentPostsCurrentPage = FIRST_PAGE;
        mRequestController.setPageNumber(FIRST_PAGE);
    }

    private void addPaginationLoading() {
        mRecentPostsCurrentPage++;
        mPostList.add(mPostList.size(), null);
        mAdapter.notifyItemInserted(Util.getLastPositionFromList(mPostList));
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    private void removePaginationLoading() {
        mLoading = false;
        mPostList.remove(Util.getLastPositionFromList(mPostList));
        mAdapter.notifyItemRemoved(mPostList.size());
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    private void dispararTask() {
        if (mTask == null) {
            if (Util.isInternetConnected(getActivity())) {
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

    /**
     * AsyncTask pra pegar os Recent Posts
     */
    class PostTask extends AsyncTask<Void, Void, List<Post>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Post> doInBackground(Void... param) {
            List<Post> posts;
            if (mCategoryId == null) {
                posts = mRequestController.loadRecentPosts();
            } else {
                posts = mRequestController.loadCategoryPosts(mCategoryId);
            }
            return posts;
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            super.onPostExecute(posts);
            if (posts != null) {
                showProgress(INVSISIBLE);
                if (isPagination) {
                    removePaginationLoading();
                }
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
