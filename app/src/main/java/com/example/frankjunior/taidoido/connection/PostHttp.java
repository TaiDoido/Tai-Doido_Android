package com.example.frankjunior.taidoido.connection;

import com.example.frankjunior.taidoido.model.Post;

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
    private static int pageNumber = 1;
    private static final String BLOG_RECENT_POSTS_JSON = BLOG_URL + "/api/get_recent_posts/?page=" + pageNumber;

    public static List<Post> loadBlogJson() {
        try {
            HttpURLConnection conexao = HttpUtil.connect(BLOG_RECENT_POSTS_JSON);

            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                String json = bytesToString(is);
                return readJsonBlog(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Post> readJsonBlog(String json) throws JSONException {
        List<Post> listaDePosts = new ArrayList<Post>();
        JSONObject jsonObject = new JSONObject(json);
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
                pq todos os posts vão ter featured image
             */
            e.printStackTrace();
        } finally {
            post.setId(id);
            post.setTitle(title);
            post.setImage(image);
            post.setAuthor(author);
            post.setDate(date);
            return post;
        }
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
}
