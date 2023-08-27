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
import com.example.baitap.mp3player.Model.Artist;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.Constants;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SongInArtistFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View songInArtistFragment;
    ListView listViewOfArtist;
    ArrayList<Song> lstSongInArtist;

    LinearLayout current_playing_bar_songinartist;
    TextView tv_song_title_current_songinartist,tv_artist_current_songinartist;
    CircleImageView img_album_current_bar_songinartist;
    ImageView btn_pre_current_songinartist,btn_play_pause_current_songinartist,btn_next_current_songinartist;
    Animation f;
    Toolbar toolbarArtist;
    SongInAlbumAdapter songInArtistAdapter;
    public static final String KEY_ALBUM_ID = "albumID";
    Artist artist;
    TextView toolbarNameArist;

    public SongInArtistFragment(Artist artist) {
        this.artist = artist;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        setHasOptionsMenu(true);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        songInArtistFragment = inflater.inflate(R.layout.fragment_songinartist,null);
        initControls();
        addEvents();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarArtist);
        ((MainActivity)mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)mContext).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) mContext).getTopLayout().setVisibility(View.GONE);
        return songInArtistFragment;
    }


    private void initControls() {
        listViewOfArtist=(ListView)songInArtistFragment.findViewById(R.id.listviewofartist);
        tv_song_title_current_songinartist=(TextView)songInArtistFragment.findViewById(R.id.tv_song_title_current_songinartist);

        tv_artist_current_songinartist=(TextView)songInArtistFragment.findViewById(R.id.tv_artist_current_songinartist);
        img_album_current_bar_songinartist=(CircleImageView) songInArtistFragment.findViewById(R.id.img_album_current_bar_songinartist);

        btn_pre_current_songinartist=(ImageView)songInArtistFragment.findViewById(R.id.btn_pre_current_songinartist);
        btn_play_pause_current_songinartist=(ImageView)songInArtistFragment.findViewById(R.id.btn_play_pause_current_songinartist);
        btn_next_current_songinartist=(ImageView)songInArtistFragment.findViewById(R.id.btn_next_current_songinartist);
        current_playing_bar_songinartist=(LinearLayout)songInArtistFragment.findViewById(R.id.current_playing_bar_songinartist);

        toolbarArtist =(Toolbar)songInArtistFragment.findViewById(R.id.toolbar_songinartist);


        toolbarNameArist=(TextView)songInArtistFragment.findViewById(R.id.toolbar_name_artist);

    }
    private void addEvents() {

        MediaManager mediaManager=new MediaManager((MainActivity)mContext);

        lstSongInArtist=mediaManager.getListSongOfArtist(artist.getId());

        toolbarNameArist.setText(artist.getArtist());

        //toolbarNumberSongAlbum.setText("-"+album.getNumberSong()+" bài hát");

        songInArtistAdapter=new SongInAlbumAdapter((MainActivity) mContext,R.layout.custom_songinalbum,lstSongInArtist);

        listViewOfArtist.setAdapter(songInArtistAdapter);
        listViewOfArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                listSongNowPlaying.clear();

                listSongNowPlaying.add(lstSongInArtist.get(position));

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
            current_playing_bar_songinartist.setVisibility(View.VISIBLE);
            if(isPlay){
                btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_pause);
            }else {
                btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_play);
            }
            showInfo();
        }else{
            current_playing_bar_songinartist.setVisibility(View.GONE);
        }

        current_playing_bar_songinartist.setOnClickListener(this);
        btn_pre_current_songinartist.setOnClickListener(this);
        btn_play_pause_current_songinartist.setOnClickListener(this);
        btn_next_current_songinartist.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_playing_bar_songinartist:

                PlaySongFragment fragment = (PlaySongFragment)getFragmentManager().findFragmentByTag("PlaySongFragment");
                if(fragment!=null){
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,fragment,"PlaySongFragment");
                }else{
                    PlaySongFragment playSongFragment=new PlaySongFragment(mContext);
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,playSongFragment,"PlaySongFragment");
                }


                break;
            case R.id.btn_pre_current_songinartist:
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

                btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_pause);
                showInfo();

                break;
            case R.id.btn_play_pause_current_songinartist:
                if(isPlay){
                    NotificationService.getMediaPlayer().pause();
                    img_album_current_bar_songinartist.clearAnimation();
                    btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_play);

                    isPlay=false;
                }else {
                    NotificationService.getMediaPlayer().start();
                    btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_pause);
                    img_album_current_bar_songinartist.startAnimation(f);
                    isPlay=true;
                }

                break;
            case R.id.btn_next_current_songinartist:
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
                btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_pause);
                showInfo();
                break;

        }
    }

    public void updatePlayPause(){
        if(isPlay){
            btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_pause);
        }else {
            btn_play_pause_current_songinartist.setImageResource(R.drawable.pb_play);
        }
    }

    public void showInfo(){
        String tvSongTitle=NotificationService.getSong().getName();
        String tvArtist=NotificationService.getSong().getArtist();

        tv_song_title_current_songinartist.setText(tvSongTitle);
        tv_artist_current_songinartist.setText(tvArtist);

        Uri pathImage=NotificationService.getSong().getPathImage();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),pathImage);
            //   if(bitmap!=null){
            img_album_current_bar_songinartist.setImageBitmap(bitmap);
            //       }

        } catch (IOException e) {
            e.printStackTrace();
            img_album_current_bar_songinartist.setImageResource(R.drawable.default_cover_big);
        }
//            if(imView!=null){
//                imView.setImageResource(R.drawable.default_cover_big);
//            }


        f = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        f.reset();
        img_album_current_bar_songinartist.clearAnimation();
        img_album_current_bar_songinartist.startAnimation(f);
    }
}
