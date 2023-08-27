package com.example.baitap.mp3player.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.baitap.mp3player.Database.DbHelper;
import com.example.baitap.mp3player.R;




public class DialogAddNamePlayList extends DialogFragment {
    Context mContext;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }




    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialogaddnameplaylist, null, false);
        final EditText edtAddNameList = (EditText) rootView.findViewById(R.id.edtaddnamelist);

        edtAddNameList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        return new AlertDialog.Builder(ctx)
                .setTitle("Tên playlist")
                .setView(rootView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s=String.valueOf(edtAddNameList.getText().toString());


                        DbHelper dbPlayList=new DbHelper(mContext);

                        if(dbPlayList.addPlayList(s)){


                            Intent intent=new Intent();
                            intent.putExtra("data",s);

                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DialogAddNamePlayList.this.getDialog().cancel();
                    }
                })
                .create();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
