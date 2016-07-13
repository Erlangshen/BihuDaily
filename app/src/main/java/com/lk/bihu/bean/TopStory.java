package com.lk.bihu.bean;

import java.io.Serializable;

/**
 * 首页轮询广告
 */
public class TopStory implements Serializable{
    private String image;
    private String ga_prefix;
    private String title;
    private int type;
    private int id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TopStory{" +
                "image='" + image + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
