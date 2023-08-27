package com.example.baitap.mp3player.Model;



public class Artist {
    private  int id;
    private  String artist;


    public Artist(int id,String artist) {
        this.id = id;
        this.artist = artist;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
