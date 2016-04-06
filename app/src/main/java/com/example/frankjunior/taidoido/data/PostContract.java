package com.example.frankjunior.taidoido.data;

import android.provider.BaseColumns;

/**
 * Created by frankjunior on 31/03/16.
 */
public interface PostContract extends BaseColumns {
    String TABLE_NAME = "tb_posts";
    String POST_ID = "_id";
    String TITLE = "title";
    String IMAGE = "image";
    String AUTHOR = "author";
    String LAST_UPDATE = "last_update";
    String CONTENT = "content";
    String URL = "url";
    String FAVORITE = "favorite";
}
