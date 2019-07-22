package com.hd.nature.ghkwallpaper.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class moreApps_Model {

    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_icon")
    @Expose
    private String appIcon;
    @SerializedName("app_link")
    @Expose
    private String appLink;
    @SerializedName("banner_ads")
    @Expose
    private String bannerAds;
    @SerializedName("full_screen_ads")
    @Expose
    private String fullScreenAds;
    @SerializedName("native_ads")
    @Expose
    private String nativeAds;
    @SerializedName("video_ads")
    @Expose
    private String videoAds;
    @SerializedName("app_update")
    @Expose
    private String appUpdate;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getBannerAds() {
        return bannerAds;
    }

    public void setBannerAds(String bannerAds) {
        this.bannerAds = bannerAds;
    }

    public String getFullScreenAds() {
        return fullScreenAds;
    }

    public void setFullScreenAds(String fullScreenAds) {
        this.fullScreenAds = fullScreenAds;
    }

    public String getNativeAds() {
        return nativeAds;
    }

    public void setNativeAds(String nativeAds) {
        this.nativeAds = nativeAds;
    }

    public String getVideoAds() {
        return videoAds;
    }

    public void setVideoAds(String videoAds) {
        this.videoAds = videoAds;
    }

    public String getAppUpdate() {
        return appUpdate;
    }

    public void setAppUpdate(String appUpdate) {
        this.appUpdate = appUpdate;
    }

}
