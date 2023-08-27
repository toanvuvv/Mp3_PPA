package com.example.baitap.mp3player.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.baitap.mp3player.Fragment.AllSongFragment.listSongNowPlaying;
import static com.example.baitap.mp3player.MainActivity.isLoop;
import static com.example.baitap.mp3player.MainActivity.isPlay;
import static com.example.baitap.mp3player.MainActivity.isSuffle;

public class NotificationService extends Service {
    public static MediaPlayer mediaPlayer;
    public static Song song;
    public
    static int index;
    Bitmap bitmap;
    RemoteViews views,bigViews;


    NotificationManager notificationManager;
    public final IBinder mBinder = new LocalBinder();
    public NotificationService() {

    }
    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (intent.getAction().equals(Constants.PLAY_ACTION)) {
            index=intent.getIntExtra(Constants.index,0);
            playSong(listSongNowPlaying,index);

            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();


        } else if (intent.getAction().equals(Constants.PREV_ACTION)) {
            playPre(listSongNowPlaying);
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.NEXT_ACTION)) {

            playNext(listSongNowPlaying);
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();

        }else if (intent.getAction().equals(Constants.SUFFLE_ACTION)) {

            playSuffle(listSongNowPlaying);
            Toast.makeText(this, "Clicked Suffle", Toast.LENGTH_SHORT).show();
        }
        else if (intent.getAction().equals(
                Constants.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
            pauseMusic();
        }
         else if (intent.getAction().equals(
            Constants.PAUSE_PLAY_ACTION)) {

            pausePlayMusic();
            updateNoti();


        Toast.makeText(this, "pause_play", Toast.LENGTH_SHORT).show();

         }

        return START_NOT_STICKY;
    }
    Notification status;
    private final String LOG_TAG = "NotificationService";

    private void showNotification() {

// Using RemoteViews to bind custom layouts into Notification
         views = new RemoteViews(getPackageName(),
                R.layout.notification_view);
         bigViews = new RemoteViews(getPackageName(),
                R.layout.notification_view_expanded);

// showing default album image
//        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
//        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
//        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
//                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseplayIntent = new Intent(this, NotificationService.class);
        pauseplayIntent.setAction(Constants.PAUSE_PLAY_ACTION);

        PendingIntent ppauseplayIntent = PendingIntent.getService(this, 0,
                pauseplayIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        views.setOnClickPendingIntent(R.id.btn_play_pause_noti, ppauseplayIntent);
        bigViews.setOnClickPendingIntent(R.id.btn_play_pause_noti, ppauseplayIntent);

        views.setOnClickPendingIntent(R.id.btn_next_noti, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.btn_next_noti, pnextIntent);

       // views.setOnClickPendingIntent(R.id.btn_pre_noti, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.btn_prev_noti, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.btn_close_noti, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.btn_close_noti, pcloseIntent);

        if(isPlay){
            views.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_pause);
            bigViews.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_pause);
        }
        else {
            views.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_play);
            bigViews.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_play);
        }


        views.setTextViewText(R.id.tv_song_title_noti, song.getName());
        bigViews.setTextViewText(R.id.tv_song_title_noti, song.getName());

        views.setTextViewText(R.id.tv_artist_noti,song.getArtist());
        bigViews.setTextViewText(R.id.tv_artist_noti, song.getArtist());

        Uri pathImage = song.getPathImage();
        try {

            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pathImage);

        } catch (IOException e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_cover_big);
        }
        views.setImageViewBitmap(R.id.img_album_art_noti, bitmap);
        bigViews.setImageViewBitmap(R.id.img_album_art_noti, bitmap);


        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags =Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(1, status);
    }

    public void playSong(final ArrayList<Song> songArrayList, final int index) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.reset();
            song=new Song();
            song = songArrayList.get(index);
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlay = true;
            showNotification();
            showInfo();

            Log.e("playsongabc","play");

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.reset();

                    if (isLoop) {
                        playSong(songArrayList, index);
                        Log.e("main", "getloop");
                    } else if (isSuffle) {
                        playSuffle(songArrayList);
                        Log.e("main", "playsuffle");
                    } else {
                        mediaPlayer.reset();
                        playNext(songArrayList);
                        Log.e("main", "playNext");

                    }

                }
            });
        } catch (IOException e) {

        }

    }

    public static MediaPlayer getMediaPlayer() {
        if(mediaPlayer!=null){
            Log.e("mediaplayer",mediaPlayer.toString());
        }

        return mediaPlayer;
    }

    public static Song getSong() {
        if(song!=null){
            Log.e("mediaplayer",""+song.getName());
        }
        return song;
    }
    public void showInfo(){

        Intent intent =new Intent();
        intent.setAction(Constants.UPDATE);
        sendBroadcast(intent);
    }



    public void pausePlayMusic(){
        if(mediaPlayer!=null) {
            if(isPlay){
                mediaPlayer.pause();
                isPlay=false;
            }
            else {
                mediaPlayer.start();
                isPlay=true;
                if(status==null){
                    showNotification();
                }
            }
        }
        Intent intent=new Intent();
        intent.setAction(Constants.PAUSE_PLAY_ACTION);
        sendBroadcast(intent);

    }

    public void pauseMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            isPlay=false;

        }
        Intent intent=new Intent();
        intent.setAction(Constants.PAUSE_PLAY_ACTION);
        sendBroadcast(intent);
    }


    public void playNext(ArrayList<Song> arrayList) {
        index++;
        if (index > arrayList.size() - 1) {
            index = 0;
        }
        mediaPlayer.reset();
        playSong(arrayList, index);


    }



    public void playPre(ArrayList<Song> arrayList) {
        index--;
        if (index < 0) {
            index = arrayList.size() - 1;
        }
        mediaPlayer.reset();
        playSong(arrayList, index);


    }

    public void playSuffle(ArrayList<Song> arrayList) {
        Random random = new Random(System.currentTimeMillis());
        index = random.nextInt(arrayList.size());
        mediaPlayer.reset();
        playSong(arrayList, index);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateNoti(){
        if(isPlay){
            views.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_pause);
            bigViews.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_pause);
        }
        else {
            views.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_play);
            bigViews.setImageViewResource(R.id.btn_play_pause_noti,
                    R.drawable.pb_play);
        }
        notificationManager.notify(1,status);
    }
}
