package com.example.baitap.mp3player.Utils;



import com.example.baitap.mp3player.Model.Song;

import java.util.ArrayList;



public class CheckSong {
    public static boolean checkExistSong(ArrayList<Song> arrayList, Song song){
        boolean checkExist=false;
        for(int i=0;i<arrayList.size();i++){
           if((song.getName().equals(arrayList.get(i).getName()))){
               checkExist=true;
               break;
           }
        }
        return checkExist;
    }

}
