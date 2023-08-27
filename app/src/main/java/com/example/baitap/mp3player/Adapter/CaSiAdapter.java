package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Artist;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;
import java.util.Locale;



public class CaSiAdapter extends BaseAdapter{
    ArrayList<Artist> lstArtist;
    private MainActivity activity;
    ArrayList<Artist>searchList;

    public CaSiAdapter(MainActivity context, ArrayList<Artist>lstArtist) {
        this.activity=context;
     this.lstArtist=lstArtist;
        this.searchList = new ArrayList<Artist>();
        this.searchList.addAll(lstArtist);
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lstArtist.clear();
        if (charText.length() == 0) {
            lstArtist.addAll(searchList);
        } else {
            for (Artist s : searchList) {
                if (s.getArtist().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lstArtist.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return lstArtist.size();
    }

    @Override
    public Artist getItem(int position) {
        return lstArtist.get(position);
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
            convertView =  inflater.inflate(R.layout.custom_row_artist,null);
            viewHolder=new ViewHodler();

            viewHolder.tvNameArtist= (TextView) convertView.findViewById(R.id.tv_name_artist);

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

        final Artist artist= lstArtist.get(position_item);

       viewHolder.tvNameArtist.setText(artist.getArtist());

        return convertView;
    }

    public class ViewHodler{

        public TextView tvNameArtist;

    }
}
