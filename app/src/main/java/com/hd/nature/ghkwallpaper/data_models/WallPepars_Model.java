package com.hd.nature.ghkwallpaper.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WallPepars_Model {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    public WallPepars_Model(String id, String image, String categoryId) {
        this.id = id;
        this.image = image;
        this.categoryId = categoryId;
    }

    public WallPepars_Model() {
    }

    public WallPepars_Model(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
