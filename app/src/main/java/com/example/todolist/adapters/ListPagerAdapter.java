package com.example.todolist.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.todolist.ListFragment;

/**
 * Created by rodrigo on 11/10/16.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment - Pending
                return ListFragment.newInstance(0);
            case 1: // Fragment - Done
                return ListFragment.newInstance(1);
            case 2: // Fragment - All
                return ListFragment.newInstance(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment - Pending
                return "Pending";
            case 1: // Fragment - Done
                return "Done";
            case 2: // Fragment - All
                return "All";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        ListFragment f = (ListFragment) object;
        if (f != null) {
            f.update();
        }
        return super.getItemPosition(object);
    }
}
