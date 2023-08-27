package com.example.baitap.mp3player.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.example.baitap.mp3player.Adapter.AllSongAdapter;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.Constants;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.util.ArrayList;




public class AllSongFragment extends Fragment {
    Context mContext;
    ArrayList<Song> lstSong = new ArrayList<>();
    ListView listViewSong;

    AllSongAdapter adapter;
    View listSongFragment;
    LoadListSong loadListSong;
    ProgressBar mProgressBar;

    public static ArrayList<Song> listSongNowPlaying = new ArrayList<>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.e("allsongfragment", "oncreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listSongFragment = inflater.inflate(R.layout.fragment_listsong, null);
        Log.e("allsongfragment", "oncreateview");
        initControls();
        showListSong();

        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listSongNowPlaying.clear();


                listSongNowPlaying.add(lstSong.get(position));

//                ((MainActivity) mContext).playSong(listSongNowPlaying, 0);
                Intent intent = new Intent(getActivity(),NotificationService.class);

                intent.putExtra(Constants.index,0);
                intent.setAction(Constants.PLAY_ACTION);
                mContext.startService(intent);

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext, new PlaySongFragment(mContext), "PlaySongFragment");
            }
        });

        return listSongFragment;
    }


    private void showListSong() {
        loadListSong = new LoadListSong();
        loadListSong.execute();
    }

    private class LoadListSong extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
//            sGetDataInterface = (GetDataInterface) mContext;
//            lstSong = sGetDataInterface.getDataList();
            lstSong.clear();
            MediaManager mediaManager=new MediaManager((MainActivity)mContext);
            mediaManager.getListSong(lstSong);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter = new AllSongAdapter((MainActivity) mContext,lstSong);
            listViewSong.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initControls() {
        listViewSong = (ListView) listSongFragment.findViewById(R.id.listViewSong);
        mProgressBar = (ProgressBar) listSongFragment.findViewById(R.id.progress_bar_song_list);

    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.allsong_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_allsong);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    listViewSong.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_playallsong:
                AllSongFragment.listSongNowPlaying.clear();

                listSongNowPlaying.addAll(lstSong);

                Intent intent = new Intent(getActivity(),NotificationService.class);
                intent.putExtra(Constants.index,0);
                intent.setAction(Constants.PLAY_ACTION);
                mContext.startService(intent);

                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext, new PlaySongFragment(mContext), "PlaySongFragment");

                break;
            case R.id.search_allsong:
                break;

        }
        return true;
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
        Log.e("allsongfragment", "onResume");

        ((MainActivity) mContext).getSupportActionBar().show();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("allsongfragment", "destroy");

    }


}
