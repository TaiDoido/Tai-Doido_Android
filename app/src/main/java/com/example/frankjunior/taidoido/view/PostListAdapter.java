package com.example.frankjunior.taidoido.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by frankjunior on 18/01/16.
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostsViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    private AoClicarNoPostListener mListener;

    public PostListAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    public void setAoClicarNoPostListener(AoClicarNoPostListener l){
        mListener = l;
    }

    @NonNull
    private String formatDate(String date) {
        Date dateJson = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            dateJson = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = SimpleDateFormat.getDateTimeInstance().format(dateJson);
        return mContext.getString(R.string.date, format);
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_post_list, parent, false);
        PostsViewHolder vh = new PostsViewHolder(v);
        v.setTag(vh);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    PostsViewHolder vh = (PostsViewHolder) view.getTag();
                    int position = vh.getPosition();
                    mListener.aoClicarNoPost(view, position, mPosts.get(position));
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        Post post = mPosts.get(position);

        Picasso.with(mContext).load(post.getImage()).into(holder.imgCapa);
        holder.txtTitulo.setText(post.getTitle());
        holder.txtAutores.setText(post.getAuthor());
        String date = formatDate(post.getDate());
        holder.txtDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mPosts != null ? mPosts.size() : 0;
    }

    public interface AoClicarNoPostListener {
        void aoClicarNoPost(View v, int position, Post post);
    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtAutores;
        TextView txtDate;

        public PostsViewHolder(View parent) {
            super(parent);
            imgCapa = (ImageView) parent.findViewById(R.id.imgPost);
            txtTitulo = (TextView) parent.findViewById(R.id.txtTitulo);
            txtAutores = (TextView) parent.findViewById(R.id.txtAutores);
            txtDate = (TextView) parent.findViewById(R.id.txtDate);
        }
    }
}
