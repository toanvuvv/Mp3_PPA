package com.example.baitap.mp3player.Fragment;

import static com.example.baitap.mp3player.MainActivity.isPlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;



public class NowPlayingFragment extends Fragment {
    Context mContext;
    View fragmentNowPlaying;
    private CircleImageView viewAvartar;
    Animation f;
    Uri pathImage;
    Bitmap bitmap;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        Log.e("nowplayingfragment","onCreate");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentNowPlaying = inflater.inflate(R.layout.fragment_nowplaying, null);

        initControls();
        addEvents();


        Log.e("nowplayingfragment","onCreateView");

        return fragmentNowPlaying;
    }



    private void initControls() {
        viewAvartar=(CircleImageView)fragmentNowPlaying.findViewById(R.id.viewAvartar);
    }




    private void addEvents() {

        setAvartar();
    }

    public void setAvartar() {


        try {
            Song songg= NotificationService.getSong();
            //String name=songg.getAlbum();
             pathImage=songg.getPathImage();
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),pathImage);

//            if(bitmap!=null){
                viewAvartar.setImageBitmap(bitmap);
                Log.e("kkk","notnull");
//            }
        } catch (IOException e) {
            e.printStackTrace();
            viewAvartar.setImageResource(R.drawable.default_cover_big);
        }
        animationAvartar();
    }

    public void animationAvartar(){
        f = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        f.reset();
        viewAvartar.clearAnimation();
        viewAvartar.startAnimation(f);
    }

    public void checkAvartar(){
        if(isPlay){
            viewAvartar.startAnimation(f);
        }
        else {
            viewAvartar.clearAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//
        Log.e("nowplayingfragment","onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("nowplayingfragment","onDestroy");

    }



    //bắt sự kiện ngươi dùng click vào nút playorpause

}
