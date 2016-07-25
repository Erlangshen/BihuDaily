package com.lk.bihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 *除首页之外的所有栏目内容
 */
public class BihuContent implements Serializable{
    private List<Story> stories;
    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private String image_source;
    private List<Editors> editors;

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<Editors> getEditors() {
        return editors;
    }

    public void setEditors(List<Editors> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return "BihuContent{" +
                "stories=" + stories +
                ", description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", editors=" + editors +
                '}';
    }
}
