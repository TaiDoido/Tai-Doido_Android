package com.example.frankjunior.taidoido.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class PostListAdapter extends ArrayAdapter<Post> {

    public PostListAdapter(Context context, List<Post> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_post_list, null);

            holder = new ViewHolder();
            holder.imgCapa = (ImageView)convertView.findViewById(R.id.imgPost);
            holder.txtTitulo = (TextView)convertView.findViewById(R.id.txtTitulo);
            holder.txtAutores = (TextView)convertView.findViewById(R.id.txtAutores);
            holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Picasso.with(getContext()).load(post.getImage()).into(holder.imgCapa);
        holder.txtTitulo.setText(post.getTitle());
        holder.txtAutores.setText(post.getAuthor());

        String date = formatDate(post.getDate());
        holder.txtDate.setText(date);

        return convertView;
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
        return getContext().getString(R.string.date, SimpleDateFormat.getDateTimeInstance().format(dateJson));
    }

    static class ViewHolder {
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtAutores;
        TextView txtDate;
    }
}
