package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;
import java.util.Locale;



public class SongInAlbumAdapter extends ArrayAdapter<Song> {

    ArrayList<Song> lstSong;
    private MainActivity activity;
    ArrayList<Song>searchList;


    public SongInAlbumAdapter(MainActivity context, int resource, ArrayList<Song>objects) {
        super(context, resource, objects);
        this.activity=context;
        this.lstSong=objects;
        this.searchList = new ArrayList<Song>();
        this.searchList.addAll(lstSong);
        notifyDataSetChanged();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lstSong.clear();
        if (charText.length() == 0) {
            lstSong.addAll(searchList);
        } else {
            for (Song s : searchList) {
                if (s.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lstSong.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return lstSong.size();
    }

    @Override
    public Song getItem(int position) {
        return lstSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position_item, View convertView, final ViewGroup parent) {
        final ViewHodler viewHolder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.custom_songinalbum,null);
            viewHolder=new ViewHodler();

            viewHolder.tvTenBaiHat= (TextView) convertView.findViewById(R.id.tv_tenbaihat_album);
            viewHolder.tvCaSi=(TextView)convertView.findViewById(R.id.tv_casi_album);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHodler)convertView.getTag();
        }

        final Song song=lstSong.get(position_item);


        viewHolder.tvTenBaiHat.setText(song.getName());
        viewHolder.tvCaSi.setText(song.getArtist());


        return convertView;
    }


    public class ViewHodler{
        public TextView tvCaSi;
        public TextView tvTenBaiHat;


    }
}
