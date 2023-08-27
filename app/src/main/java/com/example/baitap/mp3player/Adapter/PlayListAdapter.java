package com.example.baitap.mp3player.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.baitap.mp3player.Custom.NoDefaultSpinner;
import com.example.baitap.mp3player.Database.DbHelper;
import com.example.baitap.mp3player.R;

import java.util.ArrayList;
import java.util.List;



public class PlayListAdapter extends BaseAdapter {
    ArrayList<String> lstList;
    Context mContext;



    public PlayListAdapter(Activity mContext, ArrayList<String> lstSong) {
        this.mContext = mContext;
        this.lstList = lstSong;

    }




    @Override
    public int getCount() {
        return lstList.size();
    }

    @Override
    public Object getItem(int position) {
        return lstList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position_item, View convertView, final ViewGroup parent) {
        final ViewHodler viewHolder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.custom_listplaylist,null);
            viewHolder=new ViewHodler();

            viewHolder.tv_list= (TextView) convertView.findViewById(R.id.tv_list);
            viewHolder.spinnerlist=(NoDefaultSpinner) convertView.findViewById(R.id.spinnerlist);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHodler)convertView.getTag();
        }

        viewHolder.tv_list.setText(lstList.get(position_item));



        List<String> action = new ArrayList<String>();
        action.add("Select an item...");
        action.add("Xóa");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, action){
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
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        viewHolder.spinnerlist.setAdapter(dataAdapter);
        viewHolder.spinnerlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position_spinner, long l) {
                switch (position_spinner){
                    case 1:
                            String s=lstList.get(position_item);

                            DbHelper dbHelper=new DbHelper(mContext);
                            dbHelper.deletePlaylist(s);

                            dbHelper.deleteAllSongInPlayList(s);

                            lstList.remove(s);

                        notifyDataSetChanged();

                        break;

                    default:
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
        public TextView tv_list;
        public NoDefaultSpinner spinnerlist;


    }
}
