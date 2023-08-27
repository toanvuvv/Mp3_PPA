package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baitap.mp3player.Custom.NoDefaultSpinner;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Service.NotificationService;

import java.util.ArrayList;
import java.util.List;

import es.claucookie.miniequalizerlibrary.EqualizerView;



public class ListNowPlayingAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<Song>lstSong;





    public ListNowPlayingAdapter(Context mContext, ArrayList<Song> lstSong) {
        this.mContext = mContext;
        this.lstSong = lstSong;
    }

    @Override
    public int getCount() {
        return lstSong.size();
    }

    @Override
    public Object getItem(int position) {
        return lstSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position_item, View convertView, ViewGroup parent) {
        ViewHodler viewHolder;
        Animation f = AnimationUtils.loadAnimation(mContext, R.anim.translate);

        Log.e("listnowplayingadptre",position_item+"---getView");

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.custom_listviewnowplaying,null);
            viewHolder=new ViewHodler();

            viewHolder.tvTenBaiHatNowPlaying= (TextView) convertView.findViewById(R.id.tvTenBaiHatnowplaying);
            viewHolder.tvCaSiNowPlaying=(TextView)convertView.findViewById(R.id.tvCaSinowplaying);
            viewHolder.spinnerNowPlaying=(NoDefaultSpinner) convertView.findViewById(R.id.spinnernowplaying);

            viewHolder.equalizerView=(EqualizerView) convertView.findViewById(R.id.equalizer_view);

            viewHolder.txtNumber=(TextView) convertView.findViewById(R.id.txt_number);



            convertView.setTag(viewHolder);

        }

        else {
            viewHolder = (ViewHodler)convertView.getTag();
        }


        //position_item là vị trí của bài hát

          final Song song=lstSong.get(position_item);

        Log.e("final",""+song.getName());

        viewHolder.tvTenBaiHatNowPlaying.setText(song.getName());
        viewHolder.tvCaSiNowPlaying.setText(song.getArtist());



        if(NotificationService.getSong()==song){
            if(MainActivity.isPlay){

                viewHolder.tvTenBaiHatNowPlaying.setTextColor(mContext.getResources().getColor(R.color.yellow));
                viewHolder.tvCaSiNowPlaying.setTextColor(mContext.getResources().getColor(R.color.yellow));
                //f.reset();
//                viewHolder.tvTenBaiHatNowPlaying.clearAnimation();
//                viewHolder.tvTenBaiHatNowPlaying.startAnimation(f);
//                viewHolder.tvCaSiNowPlaying.clearAnimation();
//                viewHolder.tvCaSiNowPlaying.startAnimation(f);
                viewHolder.equalizerView.setVisibility(View.VISIBLE);
                viewHolder.txtNumber.setVisibility(View.GONE);
                viewHolder.equalizerView.animateBars();

            }else {
                viewHolder.tvTenBaiHatNowPlaying.setTextColor(mContext.getResources().getColor(R.color.yellow));
                viewHolder.tvCaSiNowPlaying.setTextColor(mContext.getResources().getColor(R.color.yellow));

//                viewHolder.tvTenBaiHatNowPlaying.clearAnimation();
//                viewHolder.tvCaSiNowPlaying.clearAnimation();
                viewHolder.equalizerView.setVisibility(View.VISIBLE);
                viewHolder.txtNumber.setVisibility(View.GONE);
                viewHolder.equalizerView.stopBars();

            }
        }else {
            viewHolder.tvTenBaiHatNowPlaying.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tvCaSiNowPlaying.setTextColor(mContext.getResources().getColor(R.color.white));
//            viewHolder.tvTenBaiHatNowPlaying.clearAnimation();
//            viewHolder.tvCaSiNowPlaying.clearAnimation();
            viewHolder.txtNumber.setVisibility(View.VISIBLE);
            viewHolder.txtNumber.setText(""+(lstSong.indexOf(song)+1));
            viewHolder.equalizerView.setVisibility(View.GONE);

        }




        List<String> action1 = new ArrayList<String>();

        action1.add("Select an item...");
        action1.add("Xoá khỏi danh sách NowPlaying");



        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, action1){
            @Override
            public boolean isEnabled(int position) {
                Log.e("listnowplayingadpter1","isEnabled");

                Song songMain=NotificationService.getSong();
                if(songMain==song){
                    if(position==0){ return false;}
                    else return false;

                }
                else if(position == 0)
                     {

                    return false;
                    }
                    else return true;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {


                Log.e("listnowplayingadpter1","getDropDownView");

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(NotificationService.getSong()==song){
                    tv.setTextColor(Color.GRAY);


                }
                else if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    }
                    else {
                    tv.setTextColor(Color.WHITE);
                    }
                return view;
            }

        };


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        viewHolder.spinnerNowPlaying.setAdapter(dataAdapter);

        viewHolder.spinnerNowPlaying.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position_spinner, long l) {
                switch (position_spinner){
                    case 1:
                        //position_item là vị trí của item chứa spinner trong listview khác với
                        //position_spinner là vị trí của text trong spinner
                        if(NotificationService.getSong()!=song){
                            lstSong.remove(song);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(mContext,"Không được xóa bài hát đang phát",Toast.LENGTH_SHORT).show();
                        }


                        break;
                    default:
                        Toast.makeText(mContext,""+position_spinner,Toast.LENGTH_SHORT).show();
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("spinner","onnothing");

            }
        });


        return convertView;
    }


    public class ViewHodler{
        public TextView tvCaSiNowPlaying;
        public TextView tvTenBaiHatNowPlaying;
        public NoDefaultSpinner spinnerNowPlaying;
        public EqualizerView equalizerView;
        public TextView txtNumber;

    }


}
