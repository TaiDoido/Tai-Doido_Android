package com.example.frankjunior.taidoido.controller;

import com.example.frankjunior.taidoido.connection.CategoriesRequest;
import com.example.frankjunior.taidoido.connection.RecentPostsRequest;
import com.example.frankjunior.taidoido.model.Category;
import com.example.frankjunior.taidoido.model.Post;

import java.util.List;

/**
 * Created by frankjunior on 02/03/16.
 * Classe para administrar as outras classes de Request
 */
public class RequestController {

    private static final String BLOG_URL = "http://frankjunior.com.br/blog";

    private CategoriesRequest mCategoriesRequest;
    private RecentPostsRequest mRecentPostsRequest;

    public RequestController() {
        mCategoriesRequest = new CategoriesRequest(BLOG_URL);
        mRecentPostsRequest = new RecentPostsRequest(BLOG_URL);
    }

    /**
     * Método para carregar os RecentPosts
     *
     * @return - lista de Posts
     */
    public List<Post> loadRecentPosts() {
        return mRecentPostsRequest.loadRecentPosts();
    }

    /**
     * Método para pegar a lista de categorias
     *
     * @return - Lista de Categorias
     */
    public List<Category> loadCategoryList() {
        return mCategoriesRequest.loadCategoryList();
    }
}
