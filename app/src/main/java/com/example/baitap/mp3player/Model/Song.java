package com.example.baitap.mp3player.Model;

import android.net.Uri;



public class Song {

    private String name;
    private String fileName;
    private String path;
    private String artist;
    private String album;
    private int duration;
    private boolean favorite;
    private Uri pathImage;

    public Song() {

    }



    public Song(String name, String fileName, String path, String artist, String album, int duration, boolean favorite,Uri pathImage) {

        this.name = name;
        this.fileName = fileName;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.favorite = favorite;
        this.pathImage=pathImage;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Uri getPathImage() {
        return pathImage;
    }

    public void setPathImage(Uri pathImage) {
        this.pathImage = pathImage;
    }

    @Override
    public String toString() {
        return (this.getName()+""+this.getArtist());
    }




}
