package com.example.baitap.mp3player.Adapter;

import static com.example.baitap.mp3player.Fragment.AllSongFragment.listSongNowPlaying;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baitap.mp3player.Fragment.PlaySongFragment;
import com.example.baitap.mp3player.FragmentCotroller.FragmentController;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;
import com.example.baitap.mp3player.Utils.CheckSong;
import com.example.baitap.mp3player.Utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;



public class AllSongAdapter extends BaseAdapter {
    ArrayList<Song> lstSong;
    private MainActivity activity;
    ArrayList<Song>searchList;


    public AllSongAdapter(MainActivity context, ArrayList<Song>lstSong) {

        this.activity=context;
        this.lstSong=lstSong;
        this.searchList = new ArrayList<Song>();
        this.searchList.addAll(lstSong);
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
            convertView =  inflater.inflate(R.layout.custom_row_allsong,null);
            viewHolder=new ViewHodler();

            viewHolder.tvTenBaiHat= (TextView) convertView.findViewById(R.id.tvTenBaiHat);
            viewHolder.tvCaSi=(TextView)convertView.findViewById(R.id.tvCaSi);
            viewHolder.addButton=(ImageButton) convertView.findViewById(R.id.add_to_play);
            viewHolder.avatarSong=(ImageView) convertView.findViewById(R.id.avatar_song);

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

        final Song song=lstSong.get(position_item);


        viewHolder.tvTenBaiHat.setText(song.getName());
        viewHolder.tvCaSi.setText(song.getArtist());



        try {

           Uri pathImage=song.getPathImage();
           Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),pathImage);

//            if(bitmap!=null){
            viewHolder.avatarSong.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
            viewHolder.avatarSong.setImageResource(R.drawable.ic_ic_empty_song);
        }


            viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listSongNowPlaying.size()==0){
                        listSongNowPlaying.add(song);

                        Intent intent = new Intent(activity,NotificationService.class);
                        intent.putExtra(Constants.index,0);
                        intent.setAction(Constants.PLAY_ACTION);
                        activity.startService(intent);

                        FragmentController.replaceFragmentAndAddBackStackUsingAnim(activity,new PlaySongFragment(activity),"PlaySongFragment");

                    }
                    else if(!(CheckSong.checkExistSong(listSongNowPlaying,song))){
                        listSongNowPlaying.add(song);
                        Toast.makeText(activity,song.getName()+" đã được thêm vào danh sách phát",Toast.LENGTH_SHORT).show();
                    }

                }
            });




        return convertView;
    }

    public class ViewHodler{
        public TextView tvCaSi;
        public TextView tvTenBaiHat;
        public ImageButton addButton;
        ImageView avatarSong;


    }
}
