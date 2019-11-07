package com.think.runex.java.Pages.Summary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.think.runex.java.Customize.Fragment.xFragment;

import java.util.List;

public class SummaryAdapter extends FragmentPagerAdapter {
    /** Main variables */
    private final String ct = "SummaryAdapter->";

    // instance variables
    private Context context;
    private List<xFragment> pages;

    public SummaryAdapter(Context context, @NonNull FragmentManager fm, int behavior, List<xFragment> pages) {
        super(fm, behavior);

        this.context = context;
        this.pages = pages;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get( position );
    }
}
