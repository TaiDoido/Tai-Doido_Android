package com.example.frankjunior.taidoido.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.frankjunior.taidoido.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 01/04/16.
 */
public class PostDAO {
    private static PostDAO instance;
    private AppDatabaseHelper mHelper;

    private PostDAO() {
        mHelper = new AppDatabaseHelper();
    }

    public static PostDAO getInstance() {
        if (instance == null) {
            instance = new PostDAO();
        }
        return instance;
    }

    public long insert(Post post) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PostContract.POST_ID, post.getId());
        cv.put(PostContract.TITLE, post.getTitle());
        cv.put(PostContract.IMAGE, post.getImage());
        cv.put(PostContract.AUTHOR, post.getAuthor());
        cv.put(PostContract.LAST_UPDATE, post.getDate());
        cv.put(PostContract.CONTENT, post.getContent());
        cv.put(PostContract.URL, post.getUrl());
        cv.put(PostContract.FAVORITE, post.isFavorite());

        long id = database.insert(PostContract.TABLE_NAME, null, cv);
        database.close();
        return id;
    }

    public long insertAllPosts(List<Post> post) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        long id = -1;
        for (int i = 0; i < post.size(); i++) {
            cv.put(PostContract.POST_ID, post.get(i).getId());
            cv.put(PostContract.TITLE, post.get(i).getTitle());
            cv.put(PostContract.IMAGE, post.get(i).getImage());
            cv.put(PostContract.AUTHOR, post.get(i).getAuthor());
            cv.put(PostContract.LAST_UPDATE, post.get(i).getDate());
            cv.put(PostContract.CONTENT, post.get(i).getContent());
            cv.put(PostContract.URL, post.get(i).getUrl());
            cv.put(PostContract.FAVORITE, post.get(i).isFavorite());
            id = database.insert(PostContract.TABLE_NAME, null, cv);
        }
        database.close();
        return id;
    }

    public int update(Post post) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
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
        int linhasAfetadas = database.update(PostContract.TABLE_NAME, cv, whereClause, whereArgs);
        database.close();
        return linhasAfetadas;
    }

    public int delete(String postId) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(postId)};
        String whereClause = PostContract.POST_ID + " = ?";
        int linhasAfetadas = database.delete(PostContract.TABLE_NAME, whereClause, whereArgs);
        database.close();
        return linhasAfetadas;
    }

    public void deleteAll() {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.delete(PostContract.TABLE_NAME, null, null);
        database.close();
    }

    public ArrayList<Post> getAllPosts(int typeScreen) {
        ArrayList<Post> list = new ArrayList<>();
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Post post;

        Cursor cursor = database.query(PostContract.TABLE_NAME, null, null, null, null, null, null);

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
        cursor.close();
        database.close();
        return list;
    }

    public Post getPost(String postId) {
        Post resultPost = null;
        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + PostContract.TABLE_NAME + " WHERE " + PostContract.POST_ID + " = ?";
        String[] args = new String[]{postId};

        //Create the query on ButlerInfo table
        Cursor cursor = database.rawQuery(sql, args);

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
            cursor.close();
            database.close();
        }
        return resultPost;
    }

}
