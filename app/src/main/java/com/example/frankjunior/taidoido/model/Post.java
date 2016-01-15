package com.example.frankjunior.taidoido.model;

import java.io.Serializable;

/**
 * Created by frankjunior on 14/01/16.
 */
public class Post implements Serializable {

    private String title;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
