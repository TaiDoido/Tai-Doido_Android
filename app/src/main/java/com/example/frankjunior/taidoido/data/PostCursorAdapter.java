package com.example.frankjunior.taidoido.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frankjunior.taidoido.R;
import com.squareup.picasso.Picasso;

/**
 * Created by frankjunior on 04/04/16.
 */
public class PostCursorAdapter extends CursorAdapter {

    private static final int LAYOUT = R.layout.item_post_list;
    private static RecyclerViewClickListener mListener;
    Context mContext;

    public PostCursorAdapter(Context context, Cursor c, RecyclerViewClickListener itemListener) {
        super(context, c, 0);
        this.mContext = context;
        mListener = itemListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(LAYOUT, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imgPost = (ImageView) view.findViewById(R.id.imgPost);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtAuthor = (TextView) view.findViewById(R.id.txtAuthor);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDate);

        txtTitle.setText(cursor.getString(cursor.getColumnIndex(PostContract.TITLE)));
        txtAuthor.setText(cursor.getString(cursor.getColumnIndex(PostContract.AUTHOR)));
        txtDate.setText(cursor.getString(cursor.getColumnIndex(PostContract.LAST_UPDATE)));
        String imgUrl = cursor.getString(cursor.getColumnIndex(PostContract.IMAGE));
        Picasso.with(context).load(imgUrl).into(imgPost);
    }

    public interface RecyclerViewClickListener {
        void OnRecyclerViewListClicked(View v, int position);
    }
}
