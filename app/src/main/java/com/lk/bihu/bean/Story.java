package com.lk.bihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 展示列表每个item的详情
 */
public class Story implements Serializable{
    private String title;
    private String ga_prefix;
    private List<String> images;
    private boolean multipic;
    private int type;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
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
        return "Story{" +
                "title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", images=" + images +
                ", multipic=" + multipic +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
