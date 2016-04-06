package com.example.frankjunior.taidoido.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.frankjunior.taidoido.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 01/04/16.
 */
public class PostDAO {
    private static PostDAO instance;
    private AppDatabaseHelper mHelper;
    private Context mContext;

    private PostDAO(Context context) {
        mHelper = new AppDatabaseHelper();
        mContext = context;
    }

    public static PostDAO getInstance(Context context) {
        if (instance == null) {
            instance = new PostDAO(context);
        }
        return instance;
    }

    public long insert(Post post) {
        Uri uri = mContext.getContentResolver().insert(PostProvider.CONTENT_URI, getValues(post));
        long id = Long.parseLong(uri.getLastPathSegment());
        if (id != -1) {
            post.setId(String.valueOf(id));
        }
        return id;
    }

    public void insertAllPosts(List<Post> postList) {
        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            Uri uri = mContext.getContentResolver().insert(PostProvider.CONTENT_URI, getValues(post));
        }
    }

    public int update(Post post) {
        Uri uri = Uri.withAppendedPath(PostProvider.CONTENT_URI, post.getId());
        int linhasAfetadas = mContext.getContentResolver().update(uri, getValues(post), null, null);
        return linhasAfetadas;
    }

    public int delete(String postId) {
        Uri uri = Uri.withAppendedPath(PostProvider.CONTENT_URI, postId);
        int linhasAfetadas = mContext.getContentResolver().delete(uri, null, null);
        return linhasAfetadas;
    }

    // delete tudo que não seja Favorito (ou seja, cache)
    public int deleteAllCache() {
        Uri uri = PostProvider.CONTENT_URI;
        String where = PostContract.FAVORITE + " = ?";
        String[] selectionArgs = new String[]{"0"};
        int linhasAfetadas = mContext.getContentResolver().delete(uri, where, selectionArgs);
        return linhasAfetadas;
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> list = new ArrayList<>();
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Post post;
        Cursor cursor = database.query(PostContract.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            post = getPostFromCursor(cursor);
            list.add(post);
        }
        cursor.close();
        database.close();
        return list;
    }

    public Post getPost(String postId) {
        Uri uri = Uri.withAppendedPath(PostProvider.CONTENT_URI, postId);
        String selection = PostContract.POST_ID + " = ?";
        String[] selectionArgs = new String[]{postId};
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, selectionArgs, null);
        Post post = getPostFromCursor(cursor);
        return post;
    }

    public ArrayList<Post> postListFromCursor(Cursor cursor) {
        ArrayList<Post> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Post post = getPostFromCursor(cursor);
            list.add(post);
        }
        cursor.close();
        return list;
    }

    public Post getPostFromCursor(Cursor cursor) {
        Post resultPost = null;
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex(PostContract.POST_ID));
            String title = cursor.getString(cursor.getColumnIndex(PostContract.TITLE));
            String image = cursor.getString(cursor.getColumnIndex(PostContract.IMAGE));
            String author = cursor.getString(cursor.getColumnIndex(PostContract.AUTHOR));
            String date = cursor.getString(cursor.getColumnIndex(PostContract.LAST_UPDATE));
            String content = cursor.getString(cursor.getColumnIndex(PostContract.CONTENT));
            String url = cursor.getString(cursor.getColumnIndex(PostContract.URL));
            String favoriteString = cursor.getString(cursor.getColumnIndex(PostContract.FAVORITE));
            boolean favorite = false;
            if (favoriteString.equals("1")) {
                favorite = true;
            }
            resultPost = new Post(id, title, image, author, date, content, url, favorite);
        }
        cursor.close();
        return resultPost;
    }

    private ContentValues getValues(Post post) {
        ContentValues cv = new ContentValues();
        cv.put(PostContract.POST_ID, post.getId());
        cv.put(PostContract.TITLE, post.getTitle());
        cv.put(PostContract.IMAGE, post.getImage());
        cv.put(PostContract.AUTHOR, post.getAuthor());
        cv.put(PostContract.LAST_UPDATE, post.getDate());
        cv.put(PostContract.CONTENT, post.getContent());
        cv.put(PostContract.URL, post.getUrl());
        cv.put(PostContract.FAVORITE, post.isFavorite());
        return cv;
    }

}
