package com.example.baitap.mp3player.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.baitap.mp3player.Adapter.ListNowPlayingAdapter;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.Constants;


public class ListNowPlayingFragment extends Fragment {
        Context mContext;
        View fragmentListNowPlaying;
        ListView listViewListNowPlaying;
    ListNowPlayingAdapter listNowPlayingAdapter;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mContext=getActivity();
            Log.e("ListNowPlayingFragment","onCreate");

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            fragmentListNowPlaying = inflater.inflate(R.layout.fragment_listsongnowplaying, null);
            Log.e("ListNowPlayingFragment","onCreateView");
            initControls();
            addEvents();


            return fragmentListNowPlaying;
        }

    private void initControls() {
        listViewListNowPlaying=(ListView)fragmentListNowPlaying.findViewById(R.id.list_view_listnowplaying);
    }

    private void addEvents() {


        listNowPlayingAdapter=new ListNowPlayingAdapter(mContext,AllSongFragment.listSongNowPlaying);
        listViewListNowPlaying.setAdapter(listNowPlayingAdapter);

        listViewListNowPlaying.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getActivity(),NotificationService.class);
                intent.putExtra(Constants.index,position);
                intent.setAction(Constants.PLAY_ACTION);
                mContext.startService(intent);


                PlaySongFragment fragment = (PlaySongFragment)(((MainActivity)mContext).getSupportFragmentManager()).findFragmentByTag("PlaySongFragment");
                if(fragment!=null){
                    fragment.showInfo();
                    fragment.showTime();
                }
                NowPlayingFragment nowPlayingFragment=(NowPlayingFragment)fragment.getNowplayingFragment();

                nowPlayingFragment.setAvartar();

                listNowPlayingAdapter.notifyDataSetChanged();




            }
        });

    }

    public ListNowPlayingAdapter getListNowPlayingAdapter() {
        return listNowPlayingAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ListNowPlayingFragment","onDestroy");
    }
}
