package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;



public class CustomAdapterDiaLog extends BaseAdapter{

    ArrayList<Song> lstSong;

    public CustomAdapterDiaLog(ArrayList<Song> lstSong, Context mContext) {
        this.lstSong = lstSong;
        this.mContext = mContext;
    }

    Context mContext;





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
         ViewHodler viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_dialog, null);
            viewHolder = new ViewHodler();

            viewHolder.tvTenBaiHat = (TextView) convertView.findViewById(R.id.tvTenBaiHat);
            viewHolder.tvCaSi = (TextView) convertView.findViewById(R.id.tvCaSi);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

         final Song song = lstSong.get(position_item);


        viewHolder.tvTenBaiHat.setText(song.getName());
        viewHolder.tvCaSi.setText(song.getArtist());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                song.setFavorite(isCheck);
                notifyDataSetChanged();
            }
        });

       viewHolder.checkBox.setChecked(song.isFavorite());
       // viewHolder.checkBox.setTag(song);


        return convertView;
    }

    public static class ViewHodler {
        public TextView tvCaSi;
        public TextView tvTenBaiHat;
        public CheckBox checkBox;


    }
}

