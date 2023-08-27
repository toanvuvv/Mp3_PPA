package com.example.baitap.mp3player.Utils;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.baitap.mp3player.Model.Album;
import com.example.baitap.mp3player.Model.Artist;
import com.example.baitap.mp3player.Model.Song;

import java.util.ArrayList;


public class MediaManager {
    Activity mContext;
    Song song;
    ArrayList<Song>lstSong=new ArrayList<>();
    final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";


    public MediaManager(Activity mContext,ArrayList<Song>lstSong) {
        this.mContext = mContext;
        this.lstSong=lstSong;
    }
    public MediaManager(Activity mContext) {
        this.mContext = mContext;

    }




    public void getListSong(ArrayList<Song> lstSong) {
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID}
                , null, null, MediaStore.Audio.Media.TITLE + " ASC");

        if(cursor!=null&&cursor.moveToFirst()) {

            do {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Long albumId=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri


                        .parse("content://media/external/audio/albumart");
                Uri pathImage = ContentUris.withAppendedId(sArtworkUri, albumId);
//                String pathImage = getCoverArtPath(albumId);

                song=new Song(name,fileName, path, artist, album, duration, false,pathImage);
                Log.e("mediamanger",""+path);


                if(song.getDuration()>60000){
                    lstSong.add(song);
                }
            }

            while (cursor.moveToNext());

        }
        cursor.close();
    }

    public String getCoverArtPath(long albumId) {
        Cursor albumCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }


    public void getAlbumsLists(ArrayList<Album>lstAlbum){


       Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        String _id = MediaStore.Audio.Albums._ID;
         String album_name = MediaStore.Audio.Albums.ALBUM;
         String artist = MediaStore.Audio.Albums.ARTIST;
        String albumart = MediaStore.Audio.Albums.ALBUM_ART;
         String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        final String[] columns = { _id, album_name, artist, albumart, tracks };
        Cursor cursor = mContext.getContentResolver().query(uri, columns, null, null, null);

        if(cursor!=null && cursor.moveToFirst()){

            do {

                int id = cursor.getInt(cursor.getColumnIndex(_id));
                String name = cursor.getString(cursor.getColumnIndex(album_name));
                String artist2 = cursor.getString(cursor.getColumnIndex(artist));
                String artPath = cursor.getString(cursor.getColumnIndex(albumart));

                Bitmap art = BitmapFactory.decodeFile(artPath);

                int number_song =Integer.parseInt(cursor.getString(cursor.getColumnIndex(tracks)));

                lstAlbum.add(new Album(id, name, artist2, art, number_song));

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public ArrayList<Song> getListSongOfAlbum(int albumId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {

            do {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Long idAlbum=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri pathImage = ContentUris.withAppendedId(sArtworkUri, idAlbum);
//                String pathImage = getCoverArtPath(albumId);

                Song songOfAlbum=new Song(name,fileName, path, artist, album, duration, false,pathImage);

                lstSong.add(songOfAlbum);

            } while (cursor.moveToNext());
        }
        return lstSong;
    }


    public ArrayList<Artist> getListArtist() {
        ArrayList<Artist> lstArtist = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID}, null, null, MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
//                ArrayList<Song> lstSong = getListSongOfArtist(id);

                Artist item = new Artist(id, title);
                lstArtist.add(item);
            } while (cursor.moveToNext());
        }

        return lstArtist;
    }

    public ArrayList<Song> getListSongOfArtist(int artistId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Long idAlbum=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");

                Uri pathImage = ContentUris.withAppendedId(sArtworkUri, idAlbum);
//                String pathImage = getCoverArtPath(albumId);

                Song songOfArtist=new Song(name,fileName, path, artist, album, duration, false,pathImage);

                lstSong.add(songOfArtist);
            } while (cursor.moveToNext());
        }
        return lstSong;
    }






}