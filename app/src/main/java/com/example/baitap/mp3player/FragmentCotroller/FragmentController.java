package com.example.baitap.mp3player.FragmentCotroller;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.baitap.mp3player.MainActivity;
import com.example.baitap.mp3player.R;



public class FragmentController {

//    public static void replaceFragmentAndAddBackStackUsingAnim(Context mContext, Fragment fragment){
//
//        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
//            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.no_change).
//                    replace(R.id.content_main,fragment).addToBackStack(null).commit();
//    }

    public static void replaceFragmentAndAddBackStackUsingAnim(Context mContext, Fragment fragment,String tag){

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.no_change, R.anim.no_change, R.anim.slide_out_up).
                replace(R.id.rltlayout,fragment,tag).addToBackStack(null).commit();
    }

    public static void addFragmentAndAddBackStackUsingAnim(Context mContext, Fragment fragment,String tag){

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.no_change, R.anim.no_change, R.anim.slide_out_up).
                add(R.id.rltlayout,fragment,tag).addToBackStack(null).commit();
    }

    public static void replaceFragment(Context mContext, Fragment fragment,String tag){

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.rltlayout,fragment,tag).commit();
    }

    public static void addFragment(Context mContext, Fragment fragment,String tag){

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                add(R.id.rltlayout,fragment,tag).commit();
    }
}