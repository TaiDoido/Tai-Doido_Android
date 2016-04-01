package com.example.frankjunior.taidoido.model;

import java.io.Serializable;

/**
 * Created by frankjunior on 14/01/16.
 */
public class Post implements Serializable {

    private String id;
    private String title;
    private String image;
    private String author;
    private String date;
    private String content;
    private String url;
    private boolean favorite;

    public Post(String id, String title, String image, String author, String date, String content, String url, boolean favorite) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.author = author;
        this.date = date;
        this.content = content;
        this.url = url;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
