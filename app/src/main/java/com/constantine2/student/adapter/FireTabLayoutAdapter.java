package com.constantine2.student.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.constantine2.student.R;
import com.constantine2.student.fragment.FireListFragment;
import com.constantine2.student.fragment.FireMapFragment;

public class FireTabLayoutAdapter extends FragmentPagerAdapter {

    Context context;

    public FireTabLayoutAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return new FireMapFragment();
            case 1:
                return new FireListFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            default:
                return context.getString(R.string.fire_map);
            case 1:
                return context.getString(R.string.fire_list);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
