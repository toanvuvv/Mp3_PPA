package com.example.baitap.mp3player.Fragment;

import static com.example.baitap.mp3player.MainActivity.isLoop;
import static com.example.baitap.mp3player.MainActivity.isPlay;
import static com.example.baitap.mp3player.MainActivity.isSuffle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.baitap.mp3player.Adapter.PagerScrollImage_Adapter;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.BlurBuilder;
import com.example.baitap.mp3player.Utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class PlaySongFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{

    Context mContext;

    ImageButton btnPhat, btnSuffle, btnLoop, btnNext, btnPre, btnX;
    RelativeLayout mh, rlt1, rlt3, rlt4,rlt5, rlt6, rlt7;

    TextView tvStart, tvEnd, tv1, tv2;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    ;
    int temp;

    SeekBar seekBar;
    View fragmentSecond;

    TabLayout tablayout;
    ViewPager viewPager;


    PagerScrollImage_Adapter pagerScrollImage_adapter;
    int index;
    NowPlayingFragment nowPlayingFragment;
    ListNowPlayingFragment listNowPlayingFragment;
    Bitmap bitmap;
    ImageView imageFullScreen;
    Toolbar toolbarPlaySong;




    public PlaySongFragment(Context mContext) {
        this.mContext = mContext;


    }

    public PlaySongFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSecond = inflater.inflate(R.layout.frgament_playsong,null);

        anhXa();
        ((MainActivity)mContext).getTopLayout().setVisibility(View.GONE);
        addControls();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarPlaySong);

        ((MainActivity)mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)mContext).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_row_white_24dp);

        seekBar.setOnSeekBarChangeListener(this);
        btnPhat.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnLoop.setOnClickListener(this);
        btnSuffle.setOnClickListener(this);

        if(isLoop){
            btnLoop.setBackgroundResource(R.drawable.ic_player_v4_repeat_all);
        }
        else {
            btnLoop.setBackgroundResource(R.drawable.ic_player_v4_repeat_off);
        }
        if(isSuffle){
            btnSuffle.setBackgroundResource(R.drawable.ic_player_v4_shuffle_on);
        }else {
            btnSuffle.setBackgroundResource(R.drawable.ic_player_v4_shuffle_off);
        }
        if(isPlay){
            btnPhat.setBackgroundResource(R.drawable.ic_player_v4_pause);
        }else {
            btnPhat.setBackgroundResource(R.drawable.ic_player_v4_play);
        }




        Log.e("playsongfragment","onCreateView");

        return fragmentSecond;
    }



    private void addControls() {
        tablayout.setupWithViewPager(viewPager,true);
        FragmentManager manager=getChildFragmentManager();

         pagerScrollImage_adapter=new PagerScrollImage_Adapter(manager);

        viewPager.setAdapter(pagerScrollImage_adapter);


        showTime();
        showInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ((MainActivity)mContext).onBackPressed();
                break;
        }
        return true;
    }


    public void showInfo() {
        tv1.setText(NotificationService.getSong().getName());
        tv2.setText(NotificationService.getSong().getArtist());
        setDefaultWallpaper();
    }

    public void setDefaultWallpaper() {
        //set hình nền mờ
        Uri pathImage=(NotificationService.getSong().getPathImage());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), pathImage);
        } catch (IOException e) {
            e.printStackTrace();

        }
        if(bitmap==null){
            bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_player_bg);
        }

        Bitmap bitmapOpacity=BlurBuilder.blur(mContext,bitmap);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmapOpacity);
        mh.setBackground(bitmapDrawable);
    }

    private void anhXa() {
        rlt3 = (RelativeLayout) fragmentSecond.findViewById(R.id.rlt3);
        rlt4 = (RelativeLayout) fragmentSecond.findViewById(R.id.rlt4);
        rlt5 = (RelativeLayout) fragmentSecond.findViewById(R.id.rlt5);
        rlt6 = (RelativeLayout) fragmentSecond.findViewById(R.id.rlt6);
        rlt7 = (RelativeLayout) fragmentSecond.findViewById(R.id.rlt7);
        btnLoop = (ImageButton) fragmentSecond.findViewById(R.id.btnLoop);
        btnSuffle = (ImageButton) fragmentSecond.findViewById(R.id.btnSuffle);
        btnPhat = (ImageButton) fragmentSecond.findViewById(R.id.btnPhat);
        mh = (RelativeLayout) fragmentSecond.findViewById(R.id.mh);
        btnNext = (ImageButton) fragmentSecond.findViewById(R.id.btnNext);
        btnPre = (ImageButton)fragmentSecond. findViewById(R.id.btnPre);
        tvStart = (TextView) fragmentSecond.findViewById(R.id.tvStart);
        tvEnd = (TextView) fragmentSecond.findViewById(R.id.tvEnd);
        seekBar = (SeekBar) fragmentSecond.findViewById(R.id.seekBar);
        tv1 = (TextView) fragmentSecond.findViewById(R.id.tv1);
        tv2 = (TextView) fragmentSecond.findViewById(R.id.tv2);
        tablayout=(TabLayout)fragmentSecond.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)fragmentSecond.findViewById(R.id.view_pager);
        imageFullScreen=(ImageView)fragmentSecond.findViewById(R.id.image__full_screen);
        toolbarPlaySong=(Toolbar)fragmentSecond.findViewById(R.id.toolbar_playsong);

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            startTime = NotificationService.getMediaPlayer().getCurrentPosition();
            if ((TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) startTime))) < 10) {
                tvStart.setText(String.format("0%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime))));
            } else {
                tvStart.setText(String.format("0%d:%d",

                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
            }
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
     };

    public void showTime() {
        MediaPlayer mp=NotificationService.getMediaPlayer();
        Song s=NotificationService.getSong();

        Log.e("playsongabc","showtime");

        startTime = mp.getCurrentPosition();
        finalTime =mp.getDuration();

        seekBar.setMax((int) finalTime);
        if ((TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                        toMinutes((long) finalTime))) < 10) {
            tvEnd.setText(String.format("0%d:0%d",

                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) finalTime))));
        } else {
            tvEnd.setText(String.format("0%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
            );
        }

        tvStart.setText(String.format("0%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );
        seekBar.setProgress((int) startTime);

        myHandler.postDelayed(UpdateSongTime, 100);
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            if (NotificationService.getMediaPlayer().isPlaying() || (NotificationService.getMediaPlayer() != null)) {
                if (fromUser)
                    NotificationService.getMediaPlayer().seekTo(progress);
            } else if (NotificationService.getMediaPlayer()== null) {

                seekBar.setProgress(0);
            }
        } catch (Exception e) {
            Log.e("seek bar", "" + e);
            seekBar.setEnabled(false);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onClick(View v) {
        nowPlayingFragment = (NowPlayingFragment) pagerScrollImage_adapter.getRegisteredFragment(0);
        listNowPlayingFragment= (ListNowPlayingFragment) pagerScrollImage_adapter.getRegisteredFragment(1);
        switch (v.getId()){
            case R.id.btnPhat:
//                if(isPlay){
//                    NotificationService.getMediaPlayer().pause();
//                    btnPhat.setBackgroundResource(R.drawable.ic_player_v4_play);
//                    isPlay=false;
//
//                }else {
//                    NotificationService.getMediaPlayer().start();
//                    btnPhat.setBackgroundResource(R.drawable.ic_player_v4_pause);
//                    isPlay=true;
//                    //listNowPlayingFragment.getListNowPlayingAdapter().setAnimation();
//                }
                Intent pauePlayIntent = new Intent(getActivity(),NotificationService.class);

                pauePlayIntent.setAction(Constants.PAUSE_PLAY_ACTION);
                mContext.startService(pauePlayIntent);

                nowPlayingFragment.checkAvartar();
                listNowPlayingFragment.getListNowPlayingAdapter().notifyDataSetChanged();

                break;

            case R.id.btnNext:
                Log.e("playsongabc", "" + NotificationService.getSong().getName());

                if(isSuffle){

                    Intent intent = new Intent(getActivity(),NotificationService.class);

                    intent.setAction(Constants.SUFFLE_ACTION);
                    mContext.startService(intent);

                }
                else {
                    Intent nextIntent = new Intent(getActivity(), NotificationService.class);

                    nextIntent.setAction(Constants.NEXT_ACTION);
                    mContext.startService(nextIntent);


//                    btnPhat.setBackgroundResource(R.drawable.ic_player_v4_pause);
//                    showTime();
//                    showInfo();
//                    nowPlayingFragment.setAvartar();
//
//                    pagerScrollImage_adapter.notifyDataSetChanged();
//                    listNowPlayingFragment.getListNowPlayingAdapter().notifyDataSetChanged();


                }
                break;
            case R.id.btnPre:
                if(isSuffle){

                    Intent intent = new Intent(getActivity(),NotificationService.class);

                    intent.setAction(Constants.SUFFLE_ACTION);
                    mContext.startService(intent);

                }
                else {
                    Intent preIntent = new Intent(getActivity(),NotificationService.class);

                    preIntent.setAction(Constants.PREV_ACTION);
                    mContext.startService(preIntent);

                }
                btnPhat.setBackgroundResource(R.drawable.ic_player_v4_pause);//khi ấn vào next hay pre thì set hình ảnh là phát
                nowPlayingFragment.setAvartar();

                showTime();
                showInfo();

                listNowPlayingFragment.getListNowPlayingAdapter().notifyDataSetChanged();

                break;

            case R.id.btnLoop:
                if(isLoop){

                    isLoop=false;
                    btnLoop.setBackgroundResource(R.drawable.ic_player_v4_repeat_off);

                }
                else {
                    isLoop=true;
                    btnLoop.setBackgroundResource(R.drawable.ic_player_v4_repeat_all);
                }
                Log.e("islog:",""+isLoop);
                break;

            case R.id.btnSuffle:
                if(isSuffle){
                    isSuffle=false;
                    btnSuffle.setBackgroundResource(R.drawable.ic_player_v4_shuffle_off);
                }
                else {
                    isSuffle=true;
                    btnSuffle.setBackgroundResource(R.drawable.ic_player_v4_shuffle_on);
                }
                break;



        }
    }

    public void updatePlayPause(){
        if(isPlay){
            btnPhat.setBackgroundResource(R.drawable.ic_player_v4_pause);

        }else {
            btnPhat.setBackgroundResource(R.drawable.ic_player_v4_play);
        }
    }
    public Fragment getNowplayingFragment(){
        return (NowPlayingFragment) pagerScrollImage_adapter.getRegisteredFragment(0);
    }



    public Fragment getListNowplayingFragment(){
        return (ListNowPlayingFragment) pagerScrollImage_adapter.getRegisteredFragment(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
