package com.example.frankjunior.taidoido.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class PostProvider extends ContentProvider {

    // this authority must be the same declared in AndroidManifest.xml
    public static final String AUTHORITY = "com.example.frankjunior.taidoido.provider";
    private static final String BASE_PATH = "posts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final int ALL_POSTS = 0;
    private static final int POST_ID = 1;

    private static final String NUMBER = "/#";
    private static final String TEXT = "/*";
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AppDatabaseHelper mDatabaseHelper;

    public PostProvider() {
    }

    /**
     * buildUriMatcher is a method responsable to check uri of content provider
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, BASE_PATH, ALL_POSTS);
        matcher.addURI(AUTHORITY, BASE_PATH + NUMBER, POST_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new AppDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        long id = 0;
        Cursor cursor;
        switch (uriType) {
            case ALL_POSTS:
//                String postId = String.valueOf(values.get(PostContract.POST_ID));
//                String whereClause = PostContract.POST_ID +" = ?";
//                String[] whereValue = new String[]{ postId };
//                cursor = sqlDB.query(
//                        PostContract.TABLE_NAME,
//                        new String[]{PostContract._ID},
//                        whereClause,
//                        whereValue, null, null, null);
//                if (cursor != null && cursor.moveToNext()){
//                    cursor.moveToFirst();
//                    id = cursor.getInt(cursor.getColumnIndex(PostContract._ID));
//                    sqlDB.update(PostContract.TABLE_NAME, values, whereClause, whereValue);
//                    cursor.close();
//                } else {
//                    id = sqlDB.insert(PostContract.TABLE_NAME, null, values);
//                }
                id = sqlDB.insertWithOnConflict(PostContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        Uri parse = Uri.parse(BASE_PATH + "/" + id);
        return parse;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case ALL_POSTS:
                rowsDeleted = sqlDB.delete(PostContract.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case POST_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PostContract.TABLE_NAME,
                            PostContract.POST_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(PostContract.TABLE_NAME,
                            PostContract.POST_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriType) {
            case ALL_POSTS:
                rowsUpdated = sqlDB.update(PostContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case POST_ID:
//                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PostContract.TABLE_NAME, values,
                            PostContract.POST_ID + "=" + ContentUris.parseId(uri), null);
                } else {
                    rowsUpdated = sqlDB.update(PostContract.TABLE_NAME, values,
                            PostContract.POST_ID + "=" + ContentUris.parseId(uri) + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // Set the table
        queryBuilder.setTables(PostContract.TABLE_NAME);
        int uriType = sUriMatcher.match(uri);
        Cursor cursor = null;
        SQLiteDatabase db;
        switch (uriType) {
            case POST_ID:
            case ALL_POSTS:
                db = mDatabaseHelper.getWritableDatabase();
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
