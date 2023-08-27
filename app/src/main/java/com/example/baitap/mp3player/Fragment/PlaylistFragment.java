package com.example.baitap.mp3player.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.baitap.mp3player.Adapter.PlayListAdapter;
import com.example.baitap.mp3player.Database.DbHelper;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;




public class PlaylistFragment extends Fragment {
    Context mContext;
    View fragmentPlaylist;
    ArrayList<String>listPlay=new ArrayList<>();
    ListView listView_playlist;
    PlayListAdapter playListAdapter;
    LoadListSong loadListSong;
    ProgressBar progress_bar_playlist;
    String namePlayList;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

        Log.e("playlist","onCreate");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       fragmentPlaylist= inflater.inflate(R.layout.fragment_playlist,null);
        initControls();

        showPlayList();



        listView_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String namePlayList=listPlay.get(i);
                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new SongInPlayListFragment(namePlayList),"SongInPlayListFragment");

            }
        });
        Log.e("playlist","onCreateView");

        return fragmentPlaylist;
    }



    private void initControls() {
        listView_playlist=(ListView)fragmentPlaylist.findViewById(R.id.listView_playlist);
        progress_bar_playlist=(ProgressBar)fragmentPlaylist.findViewById(R.id.progressBarStyle);

    }

    private void showPlayList() {
        loadListSong=new LoadListSong();
        loadListSong.execute();
    }

    private class LoadListSong extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            DbHelper dbHelper=new DbHelper(mContext);
            listPlay=dbHelper.getAllPlayList();

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
            playListAdapter=new PlayListAdapter((MainActivity)mContext,listPlay);
            listView_playlist.setAdapter(playListAdapter);
            progress_bar_playlist.setVisibility(View.GONE);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_playlist:

                DialogAddNamePlayList dialogAddNamePlayList=new DialogAddNamePlayList();

                dialogAddNamePlayList.setTargetFragment(this, 1);
                dialogAddNamePlayList.show(getFragmentManager(),"DialogAddNamePlayList");

                break;

        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case 1:

                    namePlayList=data.getStringExtra("data");
                   listPlay.add(namePlayList);

                    break;
            }
            playListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        setHasOptionsMenu(true);
        Log.e("playlistfragment","onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("playlistfragment","onDestroy");
    }

    public ArrayList<String> getListPlay() {
        return listPlay;
    }
}
