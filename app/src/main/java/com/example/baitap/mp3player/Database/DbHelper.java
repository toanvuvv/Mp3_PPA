package com.example.baitap.mp3player.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.example.baitap.mp3player.Model.Song;

import java.util.ArrayList;




public class DbHelper extends SQLiteOpenHelper {
    Context context;

    public  static  final  String DB_NAME = "mydb.sqlite";


    private static final String TABLE_Playlist = "PLAYLIST";

    private static final String TABLE_SongInPlayList = "SONGINPLAYLIST";


    private static final String KEY_ID = "Id";

    private static final String KEY_PLAYLIST = "Playlist";

    private static final String KEY_NAME = "Name";
    private static final String KEY_FILE_NAME ="FileName";
    private static final String KEY_PATH ="Path";
    private static final String KEY_ARTIST ="Artist";
    private static final String KEY_ALBUM ="Album";
    private static final String KEY_DURATION="Duration";
    private static final String KEY_FAVORITE ="Favorite";
    private static final String KEY_PATHIMAGE="PathImage";


    //tạo bảng tên các playlist

    public String createDb_Playlist="CREATE TABLE `PLAYLIST` ( `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Playlist` TEXT NOT NULL )";
    public String createDb_SongInPlayList="CREATE TABLE `SONGINPLAYLIST` ( `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,'Playlist' TEXT,`Name` TEXT, `FileName` TEXT, `Path` TEXT, `Artist` TEXT, `Album` TEXT, `Duration` INTEGER, `Favorite` BLOB,`PathImage` TEXT)";

    public  DbHelper(Context context){
        super(context,DB_NAME,null,1);
        this.context=context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDb_Playlist);
       db.execSQL(createDb_SongInPlayList);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.e("tạo bảng mới", "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Playlist);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SongInPlayList);


        // Và tạo lại.
        onCreate(db);

    }


//LÀM VIỆC VỚI PLAYLIST
    //lấy tất cả playlist
    public ArrayList<String> getAllPlayList() {
        ArrayList<String> listPlayList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Playlist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                String s=cursor.getString(1);
                // Adding contact to list

                listPlayList.add(s);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listPlayList;
    }

    public boolean checkForExistPlayList(String s){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_Playlist+ " WHERE "+ KEY_PLAYLIST + " = "+"'"+s+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


    //thêm playlist mới
    public boolean addPlayList(String s) {
        boolean flag=false;

        SQLiteDatabase db = this.getWritableDatabase();
        if(!checkForExistPlayList(s)){
            ContentValues values = new ContentValues();
            values.put(KEY_PLAYLIST, s);

            // Inserting Row
            db.insert(TABLE_Playlist, null, values);
            flag=true;
        }

        db.close();
        return flag;// Closing database connection
    }


    //xóa playlist khỏi danh sách playlist
    public void deletePlaylist(String s) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_Playlist, KEY_PLAYLIST + " = ?",
                new String[] { String.valueOf(s) });

        db.close();
    }



//LÀM VIỆC VỚI SONG IN PLAYLIST

    public boolean checkForExistSongInPlayList(String Playlist, Song song){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " +"'"+TABLE_SongInPlayList+"'"+" WHERE "+ KEY_NAME + " = "+"'"+song.getName()+"'"+" AND "+KEY_PLAYLIST+" = "+""+"'"+Playlist+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }




    //lấy tât cả bài hát trong 1 playlist
    public ArrayList<Song> getAllSongFromPlayList(String Playlist) {
        ArrayList<Song> listSong = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM "+"'"+ TABLE_SongInPlayList+"'"+" WHERE "+KEY_PLAYLIST+"="+"'"+Playlist+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setName(cursor.getString(2));
                song.setFileName(cursor.getString(3));
                song.setPath(cursor.getString(4));
                song.setArtist(cursor.getString(5));
                song.setAlbum(cursor.getString(6));
                song.setDuration(Integer.parseInt(cursor.getString(7)));

                boolean value = cursor.getInt(8) > 0;

                song.setFavorite(false);
                song.setPathImage(Uri.parse(cursor.getString(9)));
                // Adding contact to list
                Log.e("dbhelper",""+song.isFavorite());
                listSong.add(song);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listSong;
    }




    //thêm bài hát vào playlist

    public boolean addSongToPlayList(String Playlist,Song song) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean flag=false;

        if(!checkForExistSongInPlayList(Playlist,song)) {

          //  song.setFavorite(true);
            ContentValues values = new ContentValues();

            values.put(KEY_PLAYLIST,Playlist);
            values.put(KEY_NAME, song.getName());
            values.put(KEY_FILE_NAME, song.getFileName());
            values.put(KEY_PATH, song.getPath());
            values.put(KEY_ARTIST, song.getArtist());
            values.put(KEY_ALBUM, song.getAlbum());
            values.put(KEY_DURATION, song.getDuration());
            values.put(KEY_FAVORITE, song.isFavorite());
            values.put(KEY_PATHIMAGE, String.valueOf(song.getPathImage()));

            // Inserting Row
            db.insert("'"+TABLE_SongInPlayList+"'", null, values);
//            Toast.makeText(context, song.getName() + " đã được thêm vào danh sách yêu thích !", Toast.LENGTH_SHORT).show();
            flag=true;
            Log.e("124", "" + song.getPath());
        }else{
//            Toast.makeText(context,song.getName()+" đã tồn tại",Toast.LENGTH_SHORT).show();
        }

        db.close(); // Closing database connection
        return flag;
    }




    //xóa bài hát trong playlist
    public void deleteSongInPlayList(String Playlist,Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        //song.setFavorite(false);

        if(checkForExistSongInPlayList(Playlist,song)){

        db.delete(TABLE_SongInPlayList, KEY_NAME + " = ? AND "+KEY_PLAYLIST+"= ?",
                new String[] { String.valueOf(song.getName()),Playlist });

        }
        Toast.makeText(context,song.getName()+" đã bị xóa khỏi danh sách yêu thích",Toast.LENGTH_LONG).show();

        db.close();
    }

    public void deleteAllSongInPlayList(String Playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        //song.setFavorite(false);


            db.delete(TABLE_SongInPlayList,KEY_PLAYLIST+" = ?",
                    new String[] {Playlist});


        db.close();
    }


}
