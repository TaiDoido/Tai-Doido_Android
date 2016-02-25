package com.example.frankjunior.taidoido.connection;

import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 14/01/16.
 */
public class PostHttp {

    private static final String BLOG_URL = "http://frankjunior.com.br/blog";
    // Constants: tags from JSON object
    private static final String KEY_POSTS = "posts";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FEATURED_IMAGE = "thumbnail";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_AUTHOR_NAME = "nickname";
    private static final String KEY_DATE = "date";
    private static final String KEY_TOTAL_PAGES = "pages";
    private static int pageNumber = 1;
    private static int mTotalPages = 0;

    public static List<Post> loadRecentPosts() {
        try {
            String recentPostsJson = BLOG_URL + "/api/get_recent_posts/?page=" + pageNumber;
            HttpURLConnection conexao = Util.connect(recentPostsJson);

            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                String json = bytesToString(is);
                return readJson(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getTotalPages() {
        return mTotalPages;
    }

    /*
        **********************************************
        *   Métodos private
        **********************************************
        */
    private static List<Post> readJson(String json) throws JSONException {
        List<Post> listaDePosts = new ArrayList<Post>();
        JSONObject jsonObject = new JSONObject(json);

        // se a for a primeira requisição, pegue o numero total de paginas.
        // Esse if é necessário, pra não pegar esse campo a cada request
        if (pageNumber == 1) {
            mTotalPages = jsonObject.getInt(KEY_TOTAL_PAGES);
        }

        JSONArray postsJson = jsonObject.getJSONArray(KEY_POSTS);
        for (int i = 0; i < postsJson.length(); i++) {
            JSONObject jsonEntry = postsJson.getJSONObject(i);
            listaDePosts.add(jsonToModelParser(jsonEntry));
        }
        return listaDePosts;
    }

    private static Post jsonToModelParser(JSONObject jsonEntry) {
        Post post = new Post();
        String id = null;
        String title = null;
        String author = null;
        String date = null;
        String image = null;
        try {
            id = jsonEntry.getString(KEY_ID);
            title = jsonEntry.getString(KEY_TITLE);
            author = jsonEntry.getJSONObject(KEY_AUTHOR).getString(KEY_AUTHOR_NAME);
            date = jsonEntry.getString(KEY_DATE);
            image = jsonEntry.getString(KEY_FEATURED_IMAGE);
        } catch (JSONException e) {
            /*
                Sempre vai cair nesse catch enquanto tiver testando,
                pq nem sempre, o "image" existe.
                Mas no cenário real, ele não vai cair aqui,
                pq todos os posts vão ter "featured image"
             */
            e.printStackTrace();
        }
        post.setId(id);
        post.setTitle(title);
        post.setImage(image);
        post.setAuthor(author);
        post.setDate(date);
        return post;
    }

    private static String bytesToString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }

    public static void setPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        PostHttp.pageNumber = pageNumber;
    }
}
