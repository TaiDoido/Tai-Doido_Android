package com.example.frankjunior.taidoido.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.data.PostDAO;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.ui.PostDetailActivity;
import com.example.frankjunior.taidoido.ui.adapter.PostListAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by frankjunior on 04/04/16.
 */
public class FavoriteListFragment extends Fragment implements PostListAdapter.OnClickPostListener {

    private static ArrayList<Post> mPostList = new ArrayList<Post>();
    private PostDAO mDao;
    private RecyclerView mRecyclerView;
    private TextView mTextMensagem;
    private PostListAdapter mAdapter;

    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = PostDAO.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_posts_list, container, false);
        mTextMensagem = (TextView) view.findViewById(android.R.id.empty);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

//        mPostList = mDao.getFavoritePost();

        mAdapter = new PostListAdapter(getActivity(), mPostList);
        mAdapter.addOnClickPostListener(FavoriteListFragment.this);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
        return view;
    }

    @Override
    public void onClickPost(View v, int position, String postId) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST, postId);

        Pair<View, String> p1 = Pair.create(v.findViewById(R.id.imgPost), getString(R.string.post_image_transition_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
