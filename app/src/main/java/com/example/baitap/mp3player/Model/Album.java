package com.example.baitap.mp3player.Model;

import android.graphics.Bitmap;



public class Album {

    private int id;
    private String albumName;
    private String artistName;
    private int numberSong;
    private Bitmap albumImg;

    public Album(int id,String albumName,String artistName,Bitmap albumImg, int numberSong) {
        this.id = id;
        this.albumName = albumName;
        this.artistName = artistName;
        this.albumImg = albumImg;
        this.numberSong = numberSong;
    }

    public Bitmap getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(Bitmap albumImg) {
        this.albumImg = albumImg;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(int numberSong) {
        this.numberSong = numberSong;
    }
}
