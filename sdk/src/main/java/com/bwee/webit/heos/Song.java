package com.bwee.webit.heos;

public class Song {
    private final String title;
    private final String artist;
    private final String album;
    private final String url;
    private final String imageURL;
    private final int duration;

    public Song(String title, String artist, String album, String url, String imageURL, int duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.url = url;
        this.imageURL = imageURL;
        this.duration = duration;
    }

    public Song(String title){
        this.title = title;
        this.artist = "";
        this.album = "";
        this.url = "";
        this.imageURL = "";
        this.duration = 0;
    }

    public String getTitle(){
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMediaURL() {
        return this.imageURL;
    }

    public int getDuration() {
        return this.duration;
    }
}
