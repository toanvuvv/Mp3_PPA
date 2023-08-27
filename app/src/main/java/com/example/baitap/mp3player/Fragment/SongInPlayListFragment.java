package com.example.baitap.mp3player.Fragment;

import static com.example.baitap.mp3player.Fragment.AllSongFragment.listSongNowPlaying;
import static com.example.baitap.mp3player.MainActivity.isPlay;
import static com.example.baitap.mp3player.MainActivity.isSuffle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.Toast;

import com.example.baitap.mp3player.Adapter.SongInPlayListAdapter;
import com.example.baitap.mp3player.Database.DbHelper;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.CheckSong;
import com.example.baitap.mp3player.Utils.Constants;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class SongInPlayListFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    View fragmentt;
    ListView listviewofplaylist;

    ArrayList<Song> listSongChecked=new ArrayList<>();
    DialogFragmentAddSongToFavorite dialog_fragment_addsongtoplaylist;
    ArrayList<Song>listSongInPlayList=new ArrayList<>();
    SongInPlayListAdapter songInPlayListAdapter;
    String table;
    DbHelper dbHelper;
    public static final int REQ_CODE_PlayList=3;

    LinearLayout current_playing_bar_songinlist;
    TextView tv_song_title_current_songinlist,tv_artist_current_songinlist;
    CircleImageView img_album_current_bar_songinlist;
    ImageView btn_pre_current_songinlist,btn_play_pause_current_songinlist,btn_next_current_songinlist;
    Animation f;
    Toolbar toolbarSongInList;
    TextView textView;

    public SongInPlayListFragment(String table) {
        this.table=table;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentt = inflater.inflate(R.layout.fragment_songinlist, null);
        initControls();
        addEvents();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarSongInList);
        ((MainActivity)mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)mContext).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) mContext).getTopLayout().setVisibility(View.GONE);

        LoadListSongFromDb loadListSongFromDb=new LoadListSongFromDb();
        loadListSongFromDb.execute();

        listviewofplaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                listSongNowPlaying.clear();
                listSongNowPlaying.add(listSongInPlayList.get(position));

                Intent intent = new Intent(getActivity(),NotificationService.class);
                intent.putExtra(Constants.index,0);
                intent.setAction(Constants.PLAY_ACTION);
                mContext.startService(intent);


                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new PlaySongFragment(mContext),"PlaySongFragment");
                //((MainActivity)mContext).setPostion(position);
            }
        });

        return fragmentt;
    }

    private class LoadListSongFromDb extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            dbHelper=new DbHelper(mContext);

            listSongInPlayList= dbHelper.getAllSongFromPlayList(table);
//
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            songInPlayListAdapter=new SongInPlayListAdapter((MainActivity) mContext,R.layout.custom_row_allsong,listSongInPlayList);
            listviewofplaylist.setAdapter(songInPlayListAdapter);

        }
    }




    private void initControls() {
        listviewofplaylist =(ListView)fragmentt.findViewById(R.id.listviewofplaylist);

        tv_song_title_current_songinlist=(TextView)fragmentt.findViewById(R.id.tv_song_title_current_songinlist);

        tv_artist_current_songinlist=(TextView)fragmentt.findViewById(R.id.tv_artist_current_songinlist);
        img_album_current_bar_songinlist=(CircleImageView) fragmentt.findViewById(R.id.img_album_current_bar_songinlist);

        btn_pre_current_songinlist=(ImageView)fragmentt.findViewById(R.id.btn_pre_current_songinlist);
        btn_play_pause_current_songinlist=(ImageView)fragmentt.findViewById(R.id.btn_play_pause_current_songinlist);
        btn_next_current_songinlist=(ImageView)fragmentt.findViewById(R.id.btn_next_current_songinlist);
        current_playing_bar_songinlist=(LinearLayout)fragmentt.findViewById(R.id.current_playing_bar_songinlist);
        toolbarSongInList =(Toolbar)fragmentt.findViewById(R.id.toolbar_songinlist);
        textView=(TextView)fragmentt.findViewById(R.id.txtsonginlist);
    }

    private void addEvents() {
        songInPlayListAdapter=new SongInPlayListAdapter((MainActivity) mContext,R.layout.custom_row_allsong,listSongInPlayList);
        listviewofplaylist.setAdapter(songInPlayListAdapter);
        textView.setText(table);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addsongtoplaylist, menu);


        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_addsongtoplaylist:

                dialog_fragment_addsongtoplaylist=new DialogFragmentAddSongToFavorite();

                dialog_fragment_addsongtoplaylist.setTargetFragment(SongInPlayListFragment.this, REQ_CODE_PlayList);

                dialog_fragment_addsongtoplaylist.show(getFragmentManager(),"DialogFragmentAddSongToFavorite");

                break;
            
            case android.R.id.home:
                ((MainActivity)mContext).onBackPressed();
                break;
            case R.id.menu_playallsonginlist:
                AllSongFragment.listSongNowPlaying.clear();
                if(listSongInPlayList.size()>0){
                    listSongNowPlaying.addAll(listSongInPlayList);

                    Intent intent = new Intent(getActivity(),NotificationService.class);
                    intent.putExtra(Constants.index,0);
                    intent.setAction(Constants.PLAY_ACTION);
                    mContext.startService(intent);

                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new PlaySongFragment(mContext),"PlaySongFragment");
                }else {
                    Toast.makeText(mContext,"ko có bài hát để phát",Toast.LENGTH_SHORT).show();
                }
            break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQ_CODE_PlayList:
                if (resultCode == Activity.RESULT_OK) {

//                    namePlayList=data.getStringExtra("data");
//                    listPlay.add(namePlayList);
                    Log.e("playlist","onActivityResult");
                    if(dialog_fragment_addsongtoplaylist!=null){

                        listSongChecked=dialog_fragment_addsongtoplaylist.getListSongChecked();


                        for(int i=0;i<listSongChecked.size();i++){

                            Song songCheck=listSongChecked.get(i);

                            if(!CheckSong.checkExistSong(listSongInPlayList,songCheck)){
                                listSongInPlayList.add(songCheck);

                               dbHelper.addSongToPlayList(table,songCheck);
                            }
                        }

                    }
                    songInPlayListAdapter.notifyDataSetChanged();

                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("playlist","onResume");
        if(NotificationService.getMediaPlayer()!=null){
            current_playing_bar_songinlist.setVisibility(View.VISIBLE);
            if(isPlay){
                btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_pause);
            }else {
                btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_play);
            }
            showInfo();
        }else{
            current_playing_bar_songinlist.setVisibility(View.GONE);
        }

        current_playing_bar_songinlist.setOnClickListener(this);
        btn_pre_current_songinlist.setOnClickListener(this);
        btn_play_pause_current_songinlist.setOnClickListener(this);
        btn_next_current_songinlist.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_playing_bar_songinlist:

                PlaySongFragment fragment = (PlaySongFragment)getFragmentManager().findFragmentByTag("PlaySongFragment");
                if(fragment!=null){

                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,fragment,"PlaySongFragment");
                }else{
                    PlaySongFragment playSongFragment=new PlaySongFragment(mContext);
                    FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,playSongFragment,"PlaySongFragment");
                }


                break;
            case R.id.btn_pre_current_songinlist:
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
                btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_pause);
                showInfo();

                break;
            case R.id.btn_play_pause_current_songinlist:
                if(isPlay){
                    NotificationService.getMediaPlayer().pause();
                    img_album_current_bar_songinlist.clearAnimation();
                    btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_play);

                    isPlay=false;
                }else {
                    NotificationService.getMediaPlayer().start();
                    btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_pause);
                    img_album_current_bar_songinlist.startAnimation(f);
                    isPlay=true;
                }

                break;
            case R.id.btn_next_current_songinlist:
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
                btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_pause);
                showInfo();
                break;

        }
    }

    public void updatePlayPause(){
        if(isPlay){
            btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_pause);
        }else {
            btn_play_pause_current_songinlist.setImageResource(R.drawable.pb_play);
        }
    }

    public void showInfo(){
        String tvSongTitle=NotificationService.getSong().getName();
        String tvArtist=NotificationService.getSong().getArtist();

        tv_song_title_current_songinlist.setText(tvSongTitle);
        tv_artist_current_songinlist.setText(tvArtist);

        Uri pathImage=NotificationService.getSong().getPathImage();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),pathImage);
            //   if(bitmap!=null){
            img_album_current_bar_songinlist.setImageBitmap(bitmap);
            //       }

        } catch (IOException e) {
            e.printStackTrace();
            img_album_current_bar_songinlist.setImageResource(R.drawable.default_cover_big);
        }
//            if(imView!=null){
//                imView.setImageResource(R.drawable.default_cover_big);
//            }


        f = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        f.reset();
        img_album_current_bar_songinlist.clearAnimation();
        img_album_current_bar_songinlist.startAnimation(f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("playlist","onDestroy");


    }


    public String getTable() {
        return String.valueOf(table);
    }
}
