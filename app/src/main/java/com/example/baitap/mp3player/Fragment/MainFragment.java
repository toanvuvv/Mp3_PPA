package com.example.baitap.mp3player.Fragment;

import static com.example.baitap.mp3player.MainActivity.isPlay;
import static com.example.baitap.mp3player.MainActivity.isSuffle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baitap.mp3player.Adapter.ViewPagerAdapter;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.Constants;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;



public class MainFragment extends Fragment implements View.OnClickListener{
    protected TabLayout tabLayout;

    protected ViewPager viewPager;
    Toolbar toolbar;
    private ViewPagerAdapter adapter;
    View rootView;
    Context mContext;

    LinearLayout currentPlayingBar;
    ImageView btn_pre_current,btn_play_pause_current,btn_next_current;
    TextView tv_song_title_current,tv_artist_current;
    CircleImageView img_album_current_bar;
    Animation f;
    NotificationService notificationService;

    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, null);

        initControls();

        return rootView;
    }

    private void initControls() {
        toolbar=(Toolbar)rootView.findViewById(R.id.toolbar);
        tabLayout=(TabLayout)rootView.findViewById(R.id.tab_layout);
        viewPager=(ViewPager)rootView.findViewById(R.id.vp_pages);

        currentPlayingBar=(LinearLayout)rootView.findViewById(R.id.current_playing_bar);
        tv_song_title_current=(TextView)rootView.findViewById(R.id.tv_song_title_current);
        tv_artist_current=(TextView)rootView.findViewById(R.id.tv_artist_current);
        btn_pre_current = (ImageView) rootView.findViewById(R.id.btn_pre_current);
        btn_play_pause_current = (ImageView) rootView.findViewById(R.id.btn_play_pause_current);
        btn_next_current = (ImageView) rootView.findViewById(R.id.btn_next_current);
        img_album_current_bar=(CircleImageView)rootView.findViewById(R.id.img_album_current_bar);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity)mContext).setSupportActionBar(toolbar);
        ((MainActivity)mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public void onResume() {

        super.onResume();
        setHasOptionsMenu(true);
        if(NotificationService.getMediaPlayer()!=null){
            currentPlayingBar.setVisibility(View.VISIBLE);
            if(isPlay){
                btn_play_pause_current.setImageResource(R.drawable.pb_pause);
            }else {
                btn_play_pause_current.setImageResource(R.drawable.pb_play);
            }
            showInfo();
        }else{
            currentPlayingBar.setVisibility(View.GONE);
        }

        currentPlayingBar.setOnClickListener(this);
        btn_pre_current.setOnClickListener(this);
        btn_play_pause_current.setOnClickListener(this);
        btn_next_current.setOnClickListener(this);


        Log.e("mainfragment", "onResume");

        ((MainActivity) mContext).getSupportActionBar().show();
        ((MainActivity) mContext).getTopLayout().setVisibility(View.VISIBLE);

    }
    public void showInfo(){

        String tvSongTitle=NotificationService.getSong().getName();
        String tvArtist=NotificationService.getSong().getArtist();

        tv_song_title_current.setText(tvSongTitle);
        tv_artist_current.setText(tvArtist);

        Uri pathImage=NotificationService.getSong().getPathImage();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),pathImage);
            //   if(bitmap!=null){
            img_album_current_bar.setImageBitmap(bitmap);
            //       }

        } catch (IOException e) {
            e.printStackTrace();
            img_album_current_bar.setImageResource(R.drawable.default_cover_big);
        }
//            if(imView!=null){
//                imView.setImageResource(R.drawable.default_cover_big);
//            }


        f = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        f.reset();
        img_album_current_bar.clearAnimation();
        img_album_current_bar.startAnimation(f);
    }
    public void updatePlayPause(){
        if(isPlay){
            btn_play_pause_current.setImageResource(R.drawable.pb_pause);
        }else {
            btn_play_pause_current.setImageResource(R.drawable.pb_play);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_playing_bar:
                PlaySongFragment fragment = (PlaySongFragment)getFragmentManager().findFragmentByTag("PlaySongFragment");
                if(fragment!=null){
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,fragment,"PlaySongFragment");
                }else{
                    PlaySongFragment playSongFragment=new PlaySongFragment(mContext);
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,playSongFragment,"PlaySongFragment");
                }
                break;
            case R.id.btn_pre_current:
                if(isSuffle){
                    Intent intent = new Intent(getActivity(),NotificationService.class);

                    intent.setAction(Constants.SUFFLE_ACTION);
                    mContext.startService(intent);

                }
                else {
                    Intent intent = new Intent(getActivity(),NotificationService.class);

                    intent.setAction(Constants.PREV_ACTION);
                    mContext.startService(intent);
                }
                btn_play_pause_current.setImageResource(R.drawable.pb_pause);
                showInfo();

                break;
            case R.id.btn_play_pause_current:
                Intent pauePlayIntent = new Intent(getActivity(),NotificationService.class);
                pauePlayIntent.setAction(Constants.PAUSE_PLAY_ACTION);
                mContext.startService(pauePlayIntent);
                if(isPlay){

                    img_album_current_bar.clearAnimation();
                    btn_play_pause_current.setImageResource(R.drawable.pb_play);

                }else {
                    btn_play_pause_current.setImageResource(R.drawable.pb_pause);
                    img_album_current_bar.startAnimation(f);

                }


                break;
            case R.id.btn_next_current:
                if(isSuffle){

                    Intent intent = new Intent(getActivity(),NotificationService.class);

                    intent.setAction(Constants.SUFFLE_ACTION);
                    mContext.startService(intent);

                }
                else {
                    Intent intent = new Intent(getActivity(),NotificationService.class);
                    intent.setAction(Constants.NEXT_ACTION);
                    mContext.startService(intent);
                }
                btn_play_pause_current.setImageResource(R.drawable.pb_pause);
                showInfo();
                break;

        }
    }
}
