package com.example.frankjunior.taidoido.controller;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.connection.GetCategoriesRequest;
import com.example.frankjunior.taidoido.connection.GetPostsRequest;
import com.example.frankjunior.taidoido.model.Category;
import com.example.frankjunior.taidoido.model.Post;

import java.util.List;

/**
 * Created by frankjunior on 02/03/16.
 * Classe para administrar as outras classes de Request
 */
public class RequestController {

    private static final String BLOG_URL = App.getContext().getString(R.string.tai_doido_url);

    private GetCategoriesRequest mGetCategoriesRequest;
    private GetPostsRequest mGetPostsRequest;

    public RequestController() {
        mGetCategoriesRequest = new GetCategoriesRequest(BLOG_URL);
        mGetPostsRequest = new GetPostsRequest(BLOG_URL);
    }

    /**
     * Método para carregar os RecentPosts
     *
     * @return - lista de Posts
     */
    public List<Post> loadPosts(String query) {
        return mGetPostsRequest.loadPosts(query);
    }

    /**
     * Método para pegar a lista de categorias
     *
     * @return - Lista de Categorias
     */
    public List<Category> loadCategoryList() {
        return mGetCategoriesRequest.loadCategoryList();
    }
}
