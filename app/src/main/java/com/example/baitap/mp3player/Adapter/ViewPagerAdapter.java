package com.example.baitap.mp3player.Adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.baitap.mp3player.Fragment.AlbumFragment;
import com.example.baitap.mp3player.Fragment.AllSongFragment;
import com.example.baitap.mp3player.Fragment.CaSiFragment;
import com.example.baitap.mp3player.Fragment.PlaylistFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fm
     */
    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }


    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                // First Fragment of First Tab
                result = new AllSongFragment();
                break;
            case 1:
                // First Fragment of Second Tab
                result=new CaSiFragment();
                break;
            case 2:
                // First Fragment of Third Tab
                result = new AlbumFragment();
                break;
            case 3:
                result = new PlaylistFragment();
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return "Bài Hát";
            case 1:
                return "Ca Sĩ";
            case 2:
                return "Album";
            case 3:
                return "PlayList";
            default:
                return null;
        }
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
