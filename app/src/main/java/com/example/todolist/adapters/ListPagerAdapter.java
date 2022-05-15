package com.example.todolist.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.todolist.ListFragment;

/**
 * Created by rodrigo on 11/10/16.
 */

public class ListPagerAdapter extends FragmentPagerAdapter {
    private static int PAGER_ITEMS_NO = 3;

    public ListPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
        return PAGER_ITEMS_NO;
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
        ListFragment fragment = (ListFragment) object;
        if (fragment != null) {
            fragment.update();
        }
        return super.getItemPosition(object);
    }
}
