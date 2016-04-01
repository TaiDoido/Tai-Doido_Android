package com.example.frankjunior.taidoido.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.frankjunior.taidoido.model.Post;

import java.util.ArrayList;

/**
 * Created by frankjunior on 01/04/16.
 */
public class PostDAO {
    private SQLiteDatabase mDatabase;

    public PostDAO() {
        AppDatabaseHelper helper = new AppDatabaseHelper();
        mDatabase = helper.getWritableDatabase();
    }

    public long insert(Post post) {
        ContentValues cv = new ContentValues();
        cv.put(PostContract.POST_ID, post.getId());
        cv.put(PostContract.TITLE, post.getTitle());
        cv.put(PostContract.IMAGE, post.getImage());
        cv.put(PostContract.AUTHOR, post.getAuthor());
        cv.put(PostContract.LAST_UPDATE, post.getDate());
        cv.put(PostContract.CONTENT, post.getContent());
        cv.put(PostContract.URL, post.getUrl());
        cv.put(PostContract.FAVORITE, post.isFavorite());

        long id = mDatabase.insert(PostContract.TABLE_NAME, null, cv);
        mDatabase.close();
        return id;
    }

    public int update(Post post) {
        ContentValues cv = new ContentValues();
        cv.put(PostContract.POST_ID, post.getId());
        cv.put(PostContract.TITLE, post.getTitle());
        cv.put(PostContract.IMAGE, post.getImage());
        cv.put(PostContract.AUTHOR, post.getAuthor());
        cv.put(PostContract.LAST_UPDATE, post.getDate());
        cv.put(PostContract.CONTENT, post.getContent());
        cv.put(PostContract.URL, post.getUrl());
        cv.put(PostContract.FAVORITE, post.isFavorite());

        String[] whereArgs = {String.valueOf(post.getId())};
        String whereClause = PostContract.POST_ID + " = ?";
        int linhasAfetadas = mDatabase.update(PostContract.TABLE_NAME, cv, whereClause, whereArgs);
        mDatabase.close();
        return linhasAfetadas;
    }

    public int delete(Post post) {
        String[] whereArgs = {String.valueOf(post.getId())};
        String whereClause = PostContract.POST_ID + " = ?";
        int linhasAfetadas = mDatabase.delete(PostContract.TABLE_NAME, whereClause, whereArgs);
        mDatabase.close();
        return linhasAfetadas;
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> list = new ArrayList<>();
        Post post;

        //Create the query on ButlerInfo table
        Cursor cursor = mDatabase.query(PostContract.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(PostContract.POST_ID));
            String title = cursor.getString(cursor.getColumnIndex(PostContract.TITLE));
            String image = cursor.getString(cursor.getColumnIndex(PostContract.IMAGE));
            String author = cursor.getString(cursor.getColumnIndex(PostContract.AUTHOR));
            String date = cursor.getString(cursor.getColumnIndex(PostContract.LAST_UPDATE));
            String content = cursor.getString(cursor.getColumnIndex(PostContract.CONTENT));
            String url = cursor.getString(cursor.getColumnIndex(PostContract.URL));
            boolean favorite = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PostContract.FAVORITE)));

            post = new Post(id, title, image, author, date, content, url, favorite);
            list.add(post);
        }
        return list;
    }
}
