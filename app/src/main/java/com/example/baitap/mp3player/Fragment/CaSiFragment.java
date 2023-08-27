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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.baitap.mp3player.Adapter.CaSiAdapter;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Artist;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.util.ArrayList;



public class CaSiFragment extends Fragment {
    Context mContext;
    View artistFragment;
    ListView listViewArtist;
    ProgressBar progressBarArtist;
    ArrayList<Artist> lstArtist =new ArrayList<>();
    CaSiAdapter artistAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        artistFragment= inflater.inflate(R.layout.fragment_casi,null);
        initControls();
        showArtist();
        Log.e("casifragment","onCreateView");

        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                FragmentController.replaceFragmentAndAddBackStackUsingAnim(mContext,new SongInArtistFragment(lstArtist.get(i)),"SongInArtistFragment");

            }
        });

        return artistFragment;
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
                    artistAdapter.filter("");
                    listViewArtist.clearTextFilter();
                } else {
                    artistAdapter.filter(newText);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
        Log.e("casifragment", "onResume");

        if(artistAdapter!=null){
            artistAdapter.notifyDataSetChanged();
        }

    }



    private void showArtist() {
        LoadListArtist loadListArtist=new LoadListArtist();
        loadListArtist.execute();
    }


    private void initControls() {
        listViewArtist=(ListView)artistFragment.findViewById(R.id.list_view_artist);
        progressBarArtist=(ProgressBar)artistFragment.findViewById(R.id.progress_bar_artist);
    }

    private class LoadListArtist extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
//            sGetArtistInterface= (GetArtistInterface) mContext;
//            lstArtist = sGetArtistInterface.getlistArtist();
           lstArtist.clear();
            MediaManager mediaManager=new MediaManager((MainActivity)mContext);
            lstArtist=mediaManager.getListArtist();
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

            artistAdapter = new CaSiAdapter((MainActivity) mContext,lstArtist);
            listViewArtist.setAdapter(artistAdapter);
            progressBarArtist.setVisibility(View.GONE);
        }
    }
}
