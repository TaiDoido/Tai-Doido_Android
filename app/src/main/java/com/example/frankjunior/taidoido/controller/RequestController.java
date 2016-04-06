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
    private static final int INVALID_PAGE = -1;
    private static RequestController instance;
    private final int FIRST_PAGE = 1;
    private GetCategoriesRequest mGetCategoriesRequest;
    private GetPostsRequest mGetPostsRequest;
    private int mPageNumber = INVALID_PAGE;

    private RequestController() {
        mGetCategoriesRequest = new GetCategoriesRequest();
        mGetPostsRequest = new GetPostsRequest();
    }

    public static RequestController getInstance() {
        if (instance == null) {
            instance = new RequestController();
        }
        return instance;
    }

    /**
     * Método para carregar os RecentPosts
     *
     * @return - lista de Posts
     */
    public List<Post> loadRecentPosts() {
        String recentPostsJson = App.getContext().getString(R.string.get_recent_posts_api, BLOG_URL, mPageNumber);
        return mGetPostsRequest.loadPosts(recentPostsJson);
    }

    public List<Post> loadSearchedPosts(String query) {
        String searchedPosts = App.getContext().getString(R.string.get_search_post_api, BLOG_URL, query, mPageNumber);
        return mGetPostsRequest.loadPosts(searchedPosts);
    }

    public List<Post> loadCategoryPosts(String categoryId) {
        String categoryListJson = App.getContext().getString(R.string.get_categories_posts_api, BLOG_URL, categoryId);
        return mGetPostsRequest.loadPosts(categoryListJson);
    }

    /**
     * Método para pegar a lista de categorias
     *
     * @return - Lista de Categorias
     */
    public List<Category> loadCategoryList() {
        String categoryListJson = App.getContext().getString(R.string.get_categories_list_api, BLOG_URL);
        return mGetCategoriesRequest.loadCategoryList(categoryListJson);
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    /**
     * Método para setar o numero da pagina, da paginação e do RecentPosts
     *
     * @param pageNumber - numero da pagina
     */
    public void setPageNumber(int pageNumber) {
        if (pageNumber <= INVALID_PAGE) {
            pageNumber = FIRST_PAGE;
        }
        mPageNumber = pageNumber;
    }

    /**
     * Método para pegar o numero total de paginas, para controle da paginação
     *
     * @return
     */
    public int getTotalPages() {
        return mGetPostsRequest.getTotalPages();
    }
}
