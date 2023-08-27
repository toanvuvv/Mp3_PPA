package com.example.baitap.mp3player.Fragment;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.baitap.mp3player.Adapter.AlbumAdapter;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Album;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.util.ArrayList;



public class AlbumFragment extends Fragment {
    Context mContext;
    View albumFragment;
    ListView listViewAlbum;
    ProgressBar progressBarAlbum;
    ArrayList<Album>lstAlbum=new ArrayList<>();
    AlbumAdapter albumAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        setHasOptionsMenu(true);
        Log.e("albumfragment","onCreate");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        albumFragment= inflater.inflate(R.layout.fragment_album,null);
        initControls();
        showAlbum();


        listViewAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new SongInAlbumFragment(lstAlbum.get(i)),"SongInAlbumFragment");

            }
        });

        Log.e("albumfragment","onCreateView");

        return albumFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.casi_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_casi);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    albumAdapter.filter("");
                    listViewAlbum.clearTextFilter();
                } else {
                    albumAdapter.filter(newText);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showAlbum() {
        LoadListAlbum loadListAlbum=new LoadListAlbum();
        loadListAlbum.execute();
    }

    private void initControls() {
        listViewAlbum=(ListView)albumFragment.findViewById(R.id.list_view_album);
        progressBarAlbum=(ProgressBar)albumFragment.findViewById(R.id.progress_bar_album);
    }

    private class LoadListAlbum extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
//            sGetAlbumInterface= (GetAlbumInterface) mContext;
//            lstAlbum = sGetAlbumInterface.getAlbum();
            lstAlbum.clear();
            MediaManager mediaManager=new MediaManager((MainActivity)mContext);
            mediaManager.getAlbumsLists(lstAlbum);

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

            albumAdapter = new AlbumAdapter((MainActivity) mContext, lstAlbum);
            listViewAlbum.setAdapter(albumAdapter);
            progressBarAlbum.setVisibility(View.GONE);
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
        Log.e("albumfragment", "onResume");


    }
}
