package com.hd.nature.ghkwallpaper.data_models;

public class fvrt_model {


    String Fid , url;

    public fvrt_model(String fid, String url) {
        Fid = fid;
        this.url = url;
    }

    public String getFid() {
        return Fid;
    }

    public void setFid(String fid) {
        Fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
