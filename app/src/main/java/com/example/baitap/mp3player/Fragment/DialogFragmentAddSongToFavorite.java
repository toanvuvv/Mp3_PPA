package com.example.baitap.mp3player.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;


import com.example.baitap.mp3player.Adapter.CustomAdapterDiaLog;
import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.Model.Song;
import com.example.baitap.mp3player.R;
import com.example.baitap.mp3player.Utils.MediaManager;

import java.util.ArrayList;




public class DialogFragmentAddSongToFavorite extends DialogFragment {
    Context mContext;
    CustomAdapterDiaLog customAdapterDiaLog;


    ArrayList<Song>listDialog=new ArrayList<>();
    ArrayList<Song>listSongChecked=new ArrayList<>();

    ArrayList<Song>listSongTest=new ArrayList<>();





    ListView lisView_dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        Log.e("dialogfragment","onCreate");

    }




    @Override
    public void onResume() {
        super.onResume();
        Log.e("dialogfragment","onResume");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Log.e("dialogfragment","onCreateDialog");

        View rootView = inflater.inflate(R.layout.fragment_dialogaddsongtofavorite, null, false);
        lisView_dialog = (ListView) rootView.findViewById(R.id.lisView_dialog);


        new MediaManager((MainActivity)mContext).getListSong(listDialog);

        for(int i=0;i<listDialog.size();i++){
            listDialog.get(i).setFavorite(false);
        }

         customAdapterDiaLog=new CustomAdapterDiaLog(listDialog,mContext);
        lisView_dialog.setAdapter(customAdapterDiaLog);


        return new AlertDialog.Builder(ctx)
        .setTitle("Chọn Bài Hát")
        .setView(rootView)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

               for(int i=0;i<listDialog.size();i++){

                   if(listDialog.get(i).isFavorite()){

                       customAdapterDiaLog.notifyDataSetChanged();

                       listSongChecked.add(listDialog.get(i));

                       Log.e("dialogfragmentlistCheck",""+listSongChecked.size());
                   }
               }

                notifyToTarget(Activity.RESULT_OK);


                }
            })

        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogFragmentAddSongToFavorite.this.getDialog().cancel();
                notifyToTarget(Activity.RESULT_CANCELED);
            }
        })
       .create();
    }

    private void notifyToTarget(int code) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            targetFragment.onActivityResult(getTargetRequestCode(), code, null);
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("dialogfragment","onDestroy");

    }

    public ArrayList<Song> getListSongChecked() {
        return listSongChecked;
    }
}
