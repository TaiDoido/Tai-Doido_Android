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

    private static int pageNumber = 1;
    private static final int POST_PER_PAGE = 10;
    private static final String BLOG_URL = "http://frankjunior.com.br/blog";
    private static final String BLOG_RECENT_POSTS_JSON = BLOG_URL + "/wp-json/posts?filter[post_status]=publish&filter[posts_per_page]="+ POST_PER_PAGE +"&page="+ pageNumber +"&filter[orderby]=date&filter[order]=desc";

    private static String KEY_TITLE = "title";

    public static List<Post> carregarBlogJson() {
        try {
            HttpURLConnection conexao = HttpUtil.connect(BLOG_RECENT_POSTS_JSON);

            int resposta = conexao.getResponseCode();
            if (resposta ==  HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                String json = bytesParaString(is);
                return lerJsonBlog(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Post> lerJsonBlog(String json) throws JSONException {
        List<Post> listaDePosts = new ArrayList<Post>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonEntry = jsonArray.getJSONObject(i);
            listaDePosts.add(entryFromJSON(jsonEntry));
        }

        return listaDePosts;
    }

    private static Post entryFromJSON(JSONObject jsonEntry) throws JSONException {
        Post post = new Post();
        String title = jsonEntry.getString(KEY_TITLE);

        post.setTitle(title);
        return post;
    }

    private static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }
}
