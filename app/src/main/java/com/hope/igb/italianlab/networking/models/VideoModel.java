package com.hope.igb.italianlab.networking.models;

public class VideoModel {

    private  String video_url, thumbnail_url;
    private  long duration;


    public VideoModel(String video_url, String thumbnail_url, long duration) {
        this.video_url = video_url;
        this.thumbnail_url = thumbnail_url;
        this.duration = duration;
    }

    public VideoModel() {}


    public String getVideo_url() {
        return video_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public long getDuration() {
        return duration;
    }
}
