package com.example.frankjunior.taidoido.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frankjunior.taidoido.data.PostContract;
import com.example.frankjunior.taidoido.data.PostCursorAdapter;
import com.example.frankjunior.taidoido.data.PostDAO;
import com.example.frankjunior.taidoido.data.PostProvider;
import com.example.frankjunior.taidoido.model.Post;

import java.util.ArrayList;

/**
 * Created by frankjunior on 04/04/16.
 */
public class FavoriteListFragment extends ListFragment implements
        PostCursorAdapter.RecyclerViewClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static ArrayList<Post> mPostList = new ArrayList<Post>();
    private PostDAO mDao;
    private ListView mListView;
    private TextView mTextMensagem;
    private PostCursorAdapter mAdapter;

    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = PostDAO.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new PostCursorAdapter(getActivity(), null, this);
        mListView = getListView();
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                PostProvider.CONTENT_URI,
                new String[]{
                        PostContract.POST_ID,
                        PostContract.TITLE,
                        PostContract.IMAGE,
                        PostContract.AUTHOR,
                        PostContract.LAST_UPDATE,
                        PostContract.CONTENT,
                        PostContract.URL,
                        PostContract.FAVORITE},
                PostContract.FAVORITE + " = " + 1,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void OnRecyclerViewListClicked(View v, int position) {
//        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//        intent.putExtra(PostDetailActivity.EXTRA_POST, mPostList.get(position).getId());
//
//        Pair<View, String> p1 = Pair.create(v.findViewById(R.id.imgPost), getString(R.string.post_image_transition_name));
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
//        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
