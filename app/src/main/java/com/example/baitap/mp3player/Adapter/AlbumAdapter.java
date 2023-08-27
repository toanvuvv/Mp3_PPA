package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Album;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;
import java.util.Locale;



public class AlbumAdapter extends BaseAdapter {
    ArrayList<Album> lstAlbum;
    private MainActivity activity;
    ArrayList<Album>searchList;

    public AlbumAdapter(MainActivity context,ArrayList<Album>lstAlbum) {

        this.activity=context;
        this.lstAlbum=lstAlbum;
        this.searchList = new ArrayList<Album>();
        this.searchList.addAll(lstAlbum);
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lstAlbum.clear();
        if (charText.length() == 0) {
            lstAlbum.addAll(searchList);
        } else {
            for (Album s : searchList) {
                if (s.getAlbumName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lstAlbum.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return lstAlbum.size();
    }

    @Override
    public Album getItem(int position) {
        return lstAlbum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position_item, View convertView, final ViewGroup parent) {
        final ViewHodler viewHolder;
        Log.e("allsongadpter",position_item+"---getView");

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.custom_row_album,null);
            viewHolder=new ViewHodler();

            viewHolder.imageAlbum= (ImageView) convertView.findViewById(R.id.im_view_album);
            viewHolder.nameAlbum=(TextView)convertView.findViewById(R.id.txt_name_album);
            viewHolder.number=(TextView) convertView.findViewById(R.id.number_song_album);
            viewHolder.tvArtist=(TextView)convertView.findViewById(R.id.txt_artist) ;

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHodler)convertView.getTag();
        }

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(activity, R.anim.push_up_in);
        animation.setDuration(300);
        convertView.startAnimation(animation);
        animation = null;

        final Album album= lstAlbum.get(position_item);


        viewHolder.nameAlbum.setText(album.getAlbumName());
        viewHolder.tvArtist.setText(album.getArtistName());
        viewHolder.number.setText("-"+album.getNumberSong()+" bài hát");
        if(album.getAlbumImg()!=null){
            viewHolder.imageAlbum.setImageBitmap(album.getAlbumImg());
        }else {
            viewHolder.imageAlbum.setImageResource(R.drawable.default_cover_big);
        }



        return convertView;
    }

    public class ViewHodler{
        public ImageView imageAlbum;
        public TextView nameAlbum;
        public TextView tvArtist;
        public TextView number;
    }
}
