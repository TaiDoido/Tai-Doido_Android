package com.example.frankjunior.taidoido.connection;

import com.example.frankjunior.taidoido.model.Category;
import com.example.frankjunior.taidoido.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankjunior on 02/03/16.
 * Classe responsável por administrar a request de pegar a lista de categorias da API
 */
public class CategoriesRequest {

    private static final String CATEGORY_LIST_API = "/api/get_category_index/";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static String mBlogURL = null;

    public CategoriesRequest(String blogUrl) {
        mBlogURL = blogUrl;
    }

    /**
     * Método principal para pegar os RecentPosts
     *
     * @return - Lista de posts preenchida
     */
    public List<Category> loadCategoryList() {
        try {
            String categoryListJson = mBlogURL + CATEGORY_LIST_API;
            HttpURLConnection conexao = Util.connect(categoryListJson);

            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                String json = Util.bytesToString(is);
                return readJsonCategoryList(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private List<Category> readJsonCategoryList(String json) throws JSONException {
        List<Category> categoriesList = new ArrayList<Category>();
        JSONObject jsonObject = new JSONObject(json);

        JSONArray categoriesJson = jsonObject.getJSONArray(KEY_CATEGORIES);
        for (int i = 0; i < categoriesJson.length(); i++) {
            JSONObject jsonEntry = categoriesJson.getJSONObject(i);
            categoriesList.add(jsonCategoriesParser(jsonEntry));
        }
        return categoriesList;
    }

    /**
     * Método para converter um objeto JSON para o model @Post
     *
     * @param jsonEntry - Objeto JSON
     * @return - Model Post
     */
    private Category jsonCategoriesParser(JSONObject jsonEntry) {
        Category category = new Category();
        String id = null;
        String title = null;
        try {
            id = jsonEntry.getString(KEY_ID);
            title = jsonEntry.getString(KEY_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        category.setId(id);
        category.setTitle(title);
        return category;
    }

}
