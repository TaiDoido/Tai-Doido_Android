package com.example.frankjunior.taidoido.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by frankjunior on 18/01/16.
 */
public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    private List<Post> mPostList;
    private OnClickPostListener mListener;

    public PostListAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPostList = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case VIEW_ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_list, parent, false);
                holder = new PostViewHolder(view);
                view.setTag(holder);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            PostViewHolder vh = (PostViewHolder) view.getTag();
                            int position = vh.getPosition();
                            mListener.onClickPost(view, position, mPostList.get(position).getId());
                        }
                    }
                });
                break;
            case VIEW_PROG:
                view = LayoutInflater.from(mContext).inflate(R.layout.progress_item, parent, false);
                holder = new ProgressViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            Post post = mPostList.get(position);
            Picasso.with(mContext).load(post.getImage()).into(postViewHolder.imgCapa);
            postViewHolder.txtTitulo.setText(post.getTitle());
            postViewHolder.txtAutores.setText(post.getAuthor());
            postViewHolder.txtDate.setText(post.getDate());

        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mPostList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    /*
     **********************************************
     *   Interface para o click da lista, que está
     *   sendo tratado pelo Fragment
     **********************************************
     */
    public void addOnClickPostListener(OnClickPostListener l) {
        mListener = l;
    }

    public interface OnClickPostListener {
        void onClickPost(View v, int position, String postId);
    }

    /*
     **********************************************
     *   Classes ViewHolder
     **********************************************
     */
    private class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtAutores;
        TextView txtDate;

        public PostViewHolder(View parent) {
            super(parent);
            imgCapa = (ImageView) parent.findViewById(R.id.imgPost);
            txtTitulo = (TextView) parent.findViewById(R.id.txtTitle);
            txtAutores = (TextView) parent.findViewById(R.id.txtAuthor);
            txtDate = (TextView) parent.findViewById(R.id.txtDate);
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }
    }
}
