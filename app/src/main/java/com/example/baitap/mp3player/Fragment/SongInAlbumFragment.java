package com.example.baitap.mp3player.Fragment;

import static com.example.baitap.mp3player.Fragment.AllSongFragment.listSongNowPlaying;
import static com.example.baitap.mp3player.MainActivity.isPlay;
import static com.example.baitap.mp3player.MainActivity.isSuffle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baitap.mp3player.Adapter.SongInAlbumAdapter;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Album;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.Common;
import com.example.baitap.mp3player.Utils.Constants;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SongInAlbumFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View songInAlbumFragment;
    ListView listViewOfAlbum;
    ArrayList<Song>lstSongInAlbum;

    LinearLayout current_playing_bar_songinalbum;
    TextView tv_song_title_current_songinalbum,tv_artist_current_songinalbum;
    CircleImageView img_album_current_bar_songinalbum;
    ImageView btn_pre_current_songinalbum,btn_play_pause_current_songinalbum,btn_next_current_songinalbum;
    Animation f;
    Toolbar toolbarAlbum;
    SongInAlbumAdapter songInAlbumAdapter;
    public static final String KEY_ALBUM_ID = "albumID";
    Album album;
    TextView toolbarNameAlbum, toolbarNameArtist,toolbarNumberSongAlbum;
    ImageView imgAlbum;

    public SongInAlbumFragment(Album album) {
        this.album = album;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        setHasOptionsMenu(true);
        Common.setStatusBarTranslucent(true, getActivity());

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        songInAlbumFragment = inflater.inflate(R.layout.fragment_songinalbum, null);
        initControls();
        addEvents();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarAlbum);
        ((MainActivity)mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)mContext).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) mContext).getTopLayout().setVisibility(View.GONE);
        return songInAlbumFragment;
    }


    private void initControls() {
        listViewOfAlbum=(ListView)songInAlbumFragment.findViewById(R.id.listviewofalbum);
        tv_song_title_current_songinalbum=(TextView)songInAlbumFragment.findViewById(R.id.tv_song_title_current_songinalbum);

        tv_artist_current_songinalbum=(TextView)songInAlbumFragment.findViewById(R.id.tv_artist_current_songinalbum);
        img_album_current_bar_songinalbum=(CircleImageView) songInAlbumFragment.findViewById(R.id.img_album_current_bar_songinalbum);

        btn_pre_current_songinalbum=(ImageView)songInAlbumFragment.findViewById(R.id.btn_pre_current_songinalbum);
        btn_play_pause_current_songinalbum=(ImageView)songInAlbumFragment.findViewById(R.id.btn_play_pause_current_songinalbum);
        btn_next_current_songinalbum=(ImageView)songInAlbumFragment.findViewById(R.id.btn_next_current_songinalbum);
        current_playing_bar_songinalbum=(LinearLayout)songInAlbumFragment.findViewById(R.id.current_playing_bar_songinalbum);

        toolbarAlbum =(Toolbar)songInAlbumFragment.findViewById(R.id.toolbar_songinalbum);
        toolbarNameAlbum=(TextView)songInAlbumFragment.findViewById(R.id.toolbar_name_album);
        toolbarNameArtist =(TextView)songInAlbumFragment.findViewById(R.id.name_artist_in_toolbar);
        toolbarNumberSongAlbum=(TextView)songInAlbumFragment.findViewById(R.id.toolbar_number_song_album);
        imgAlbum=(ImageView)songInAlbumFragment.findViewById(R.id.img_album_list_play);

    }
    private void addEvents() {

        MediaManager mediaManager=new MediaManager((MainActivity)mContext);

        lstSongInAlbum=mediaManager.getListSongOfAlbum(album.getId());

        toolbarNameAlbum.setText(album.getAlbumName());
        toolbarNameArtist.setText(album.getArtistName());
        toolbarNumberSongAlbum.setText("-"+album.getNumberSong()+" bài hát");

        Bitmap bitmap= album.getAlbumImg();
        if(bitmap!=null){
            imgAlbum.setImageBitmap(bitmap);
        }else {
            imgAlbum.setImageResource(R.drawable.bg_intro_footer);
        }


        songInAlbumAdapter=new SongInAlbumAdapter((MainActivity) mContext,R.layout.custom_songinalbum,lstSongInAlbum);
        listViewOfAlbum.setAdapter(songInAlbumAdapter);
        listViewOfAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                listSongNowPlaying.clear();

                listSongNowPlaying.add(lstSongInAlbum.get(position));

                Intent intent = new Intent(getActivity(),NotificationService.class);
                intent.putExtra(Constants.index,0);
                intent.setAction(Constants.PLAY_ACTION);
                mContext.startService(intent);

                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new PlaySongFragment(mContext),"PlaySongFragment");
            }
        });
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


    @Override
    public void onResume() {
        super.onResume();
        Log.e("playlist","onResume");
        if(NotificationService.getMediaPlayer()!=null){
            current_playing_bar_songinalbum.setVisibility(View.VISIBLE);
            if(isPlay){
                btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_pause);
            }else {
                btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_play);
            }
            showInfo();
        }else{
            current_playing_bar_songinalbum.setVisibility(View.GONE);
        }

        current_playing_bar_songinalbum.setOnClickListener(this);
        btn_pre_current_songinalbum.setOnClickListener(this);
        btn_play_pause_current_songinalbum.setOnClickListener(this);
        btn_next_current_songinalbum.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_playing_bar_songinalbum:

                PlaySongFragment fragment = (PlaySongFragment)getFragmentManager().findFragmentByTag("PlaySongFragment");
                if(fragment!=null){
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,fragment,"PlaySongFragment");
                }else{
                    PlaySongFragment playSongFragment=new PlaySongFragment(mContext);
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,playSongFragment,"PlaySongFragment");
                }


                break;
            case R.id.btn_pre_current_songinalbum:
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
                btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_pause);
                showInfo();

                break;
            case R.id.btn_play_pause_current_songinalbum:
                if(isPlay){
                    NotificationService.getMediaPlayer().pause();
                    img_album_current_bar_songinalbum.clearAnimation();
                    btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_play);

                    isPlay=false;
                }else {
                    NotificationService.getMediaPlayer().start();
                    btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_pause);
                    img_album_current_bar_songinalbum.startAnimation(f);
                    isPlay=true;
                }

                break;
            case R.id.btn_next_current_songinalbum:
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
                btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_pause);
                showInfo();
                break;

        }
    }

    public void updatePlayPause(){
        if(isPlay){
            btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_pause);
        }else {
            btn_play_pause_current_songinalbum.setImageResource(R.drawable.pb_play);
        }
    }

    public void showInfo(){
        String tvSongTitle=NotificationService.getSong().getName();
        String tvArtist=NotificationService.getSong().getArtist();

        tv_song_title_current_songinalbum.setText(tvSongTitle);
        tv_artist_current_songinalbum.setText(tvArtist);

        Uri pathImage=NotificationService.getSong().getPathImage();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),pathImage);
            //   if(bitmap!=null){
            img_album_current_bar_songinalbum.setImageBitmap(bitmap);
            //       }

        } catch (IOException e) {
            e.printStackTrace();
            img_album_current_bar_songinalbum.setImageResource(R.drawable.default_cover_big);
        }
//            if(imView!=null){
//                imView.setImageResource(R.drawable.default_cover_big);
//            }


        f = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        f.reset();
        img_album_current_bar_songinalbum.clearAnimation();
        img_album_current_bar_songinalbum.startAnimation(f);
    }
}
