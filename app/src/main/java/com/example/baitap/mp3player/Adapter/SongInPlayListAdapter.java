package com.example.baitap.mp3player.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.baitap.mp3player.Custom.NoDefaultSpinner;
import com.example.baitap.mp3player.Database.DbHelper;
import com.example.baitap.mp3player.Fragment.SongInPlayListFragment;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class SongInPlayListAdapter extends ArrayAdapter<Song> {
    ArrayList<Song> lstSong;
    private MainActivity activity;
    ArrayList<Song>searchList;


    public SongInPlayListAdapter(MainActivity context, int resource, ArrayList<Song>objects) {
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
            convertView =  inflater.inflate(R.layout.custom_row_songinplaylist,null);
            viewHolder=new ViewHodler();

            viewHolder.tvTenBaiHat= (TextView) convertView.findViewById(R.id.tvTenBaiHat);
            viewHolder.tvCaSi=(TextView)convertView.findViewById(R.id.tvCaSi);
            viewHolder.spinner=(NoDefaultSpinner) convertView.findViewById(R.id.spinner);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHodler)convertView.getTag();
        }

        final Song song=lstSong.get(position_item);


        viewHolder.tvTenBaiHat.setText(song.getName());
        viewHolder.tvCaSi.setText(song.getArtist());


        List<String> action = new ArrayList<String>();
        action.add("Select an item...");
        action.add("Xóa khỏi playlist");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, action){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
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
        viewHolder.spinner.setAdapter(dataAdapter);

        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position_spinner, long l) {
                switch (position_spinner){
                    case 1:
                        //position_item là vị trí của item chứa spinner trong listview khác với
                        //position_spinner là vị trí của text trong spinner
                        Song song=lstSong.get(position_item);
                        lstSong.remove(song);
                        notifyDataSetChanged();

                        SongInPlayListFragment songInPlayListFragment= (SongInPlayListFragment) activity.getSupportFragmentManager().findFragmentByTag("SongInPlayListFragment");
                        if(songInPlayListFragment!=null){

                            DbHelper dbHelper=new DbHelper(activity);

                            dbHelper.deleteSongInPlayList(songInPlayListFragment.getTable(),song);
                        }
                        break;

                    default:
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });



        return convertView;
    }


    public class ViewHodler{
        public TextView tvCaSi;
        public TextView tvTenBaiHat;
        public NoDefaultSpinner spinner;


    }
}
