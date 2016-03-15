package com.example.frankjunior.taidoido.connection;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.MyLog;
import com.example.frankjunior.taidoido.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 14/01/16.
 * * Classe responsável por administrar a request de pegar os RecentPosts da API
 */
public class RecentPostsRequest {

    // ===================================================================
    // Constants: JSON objects tags - Recent Posts
    private static final String KEY_POSTS = "posts";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FEATURED_IMAGE = "thumbnail";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_AUTHOR_NAME = "nickname";
    private static final String KEY_DATE = "date";
    private static final String KEY_TOTAL_PAGES = "pages";
    private static final String KEY_URL = "url";
    private static final String KEY_CONTENT = "content";
    private static int pageNumber = 1;
    private static int mTotalPages = 0;
    private static String mBlogURL = null;
    // ===================================================================
    private final int FIRST_PAGE = 1;

    public RecentPostsRequest(String blogUrl) {
        mBlogURL = blogUrl;
    }

    /**
     * Método para pegar o numero total de paginas, para controle da paginação
     *
     * @return
     */
    public static int getTotalPages() {
        return mTotalPages;
    }

    /**
     * Método para setar o numero da pagina, da paginação do RecentPosts
     *
     * @param pageNumber - numero da pagina
     */
    public static void setPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        RecentPostsRequest.pageNumber = pageNumber;
    }

    /*
    **********************************************
    *   Métodos private
    **********************************************
    */

    /**
     * Método principal para pegar os RecentPosts
     *
     * @return - Lista de posts preenchida
     */
    public List<Post> loadRecentPosts() {
        try {
            String recentPostsJson = App.getContext().getString(R.string.get_recent_posts_api, mBlogURL, pageNumber);
            String json = Util.doGetRequest(recentPostsJson);
            return readJsonRecentPosts(json);
        } catch (Exception e) {
            MyLog.printError("Erro em fazer o donwload dos Recent Posts", e);
        }
        return null;
    }

    /**
     * Método para ler o JSON da API de RecentPosts, e converter para uma lista de posts
     *
     * @param json - JSON inteiro a ser lido
     * @return - um arrayList de posts
     * @throws JSONException
     */
    private List<Post> readJsonRecentPosts(String json) throws JSONException {
        List<Post> listaDePosts = new ArrayList<Post>();
        JSONObject jsonObject = new JSONObject(json);

        // se a for a primeira requisição, pegue o numero total de paginas.
        // Esse if é necessário, pra não pegar esse campo a cada request
        if (pageNumber == FIRST_PAGE) {
            mTotalPages = jsonObject.getInt(KEY_TOTAL_PAGES);
        }

        JSONArray postsJson = jsonObject.getJSONArray(KEY_POSTS);
        for (int i = 0; i < postsJson.length(); i++) {
            JSONObject jsonEntry = postsJson.getJSONObject(i);
            listaDePosts.add(jsonRecentPostsParser(jsonEntry));
        }
        return listaDePosts;
    }

    /**
     * Método para converter um objeto JSON para o model @Post
     *
     * @param jsonEntry - Objeto JSON
     * @return - Model Post
     */
    private Post jsonRecentPostsParser(JSONObject jsonEntry) {
        Post post = new Post();
        String id = null;
        String title = null;
        String author = null;
        String date = null;
        String image = null;
        String content = null;
        String url = null;
        try {
            id = jsonEntry.getString(KEY_ID);
            content = jsonEntry.getString(KEY_CONTENT);
            title = jsonEntry.getString(KEY_TITLE);
            author = jsonEntry.getJSONObject(KEY_AUTHOR).getString(KEY_AUTHOR_NAME);
            date = jsonEntry.getString(KEY_DATE);
            url = jsonEntry.getString(KEY_URL);
            image = jsonEntry.getString(KEY_FEATURED_IMAGE);
        } catch (JSONException e) {
            /*
                Sempre vai cair nesse catch enquanto tiver testando,
                pq nem sempre, o "image" existe.
                Mas no cenário real, ele não vai cair aqui,
                pq todos os posts vão ter "featured image"
             */
            //TODO: descomentar essa linha quando estiver no cenário real
//            MyLog.printError("Item do Json não existe", e);
        }
        post.setId(id);
        post.setTitle(title);
        post.setImage(image);
        post.setAuthor(author);
        post.setDate(date);
        post.setContent(content);
        post.setUrl(url);
        return post;
    }
}
