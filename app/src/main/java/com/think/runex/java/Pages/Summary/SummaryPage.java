package com.think.runex.java.Pages.Summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.think.runex.R;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Pages.PendingEvidencePage;
import com.think.runex.java.Pages.RegisteredEvent.RegisteredEventsPage;
import com.think.runex.java.Pages.PendingPaymentPage;

import java.util.ArrayList;
import java.util.List;

public class SummaryPage extends Fragment implements onTabChangedListener {
    /**
     * Main variables
     */
    private final String ct = "SummaryPage->";

    // instance variables
    private List<xFragment> pages = new ArrayList<xFragment>() {{
        add(new RegisteredEventsPage()
                .setOnTabChangedListener(SummaryPage.this::onNotify)
                .setPosition(0)
                .setTitle("ทั้งหมด"));
        add(new PendingPaymentPage()
                .setOnTabChangedListener(SummaryPage.this::onNotify)
                .setPosition(1)
                .setTitle("รอการชำระเงิน"));
        add(new PendingEvidencePage()
                .setOnTabChangedListener(SummaryPage.this::onNotify)
                .setPosition(2)
                .setTitle("รอแนบหลักฐาน"));
    }};
    private SummaryAdapter summaryAdapter;

    // explicit variables
    private int TAB_SELECTED_COLOR = 0;
    private int TAB_UNSELECTED_COLOR = 0;

    // views
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /** Implement methods */
    @Override
    public void onNotify(int position) {
        onTabNotified( tabLayout.getTabAt( position ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_summary, container, false);

        TAB_SELECTED_COLOR = getResources().getColor(R.color.colorAccent);
        TAB_UNSELECTED_COLOR = getResources().getColor(R.color.textColorHint);
        // view matching
        matchingViews(v);

        // view pager
        setupViewPager();

        // tab host children
        setupTabLayout();

        return v;
    }

    /** Feature methods */
    private void onTabNotified(TabLayout.Tab tab){
        // prepare usage variables
        View v = tab.getCustomView();
        v.findViewById(R.id.ic_notify).setVisibility(View.VISIBLE);

    }
    private void onTabUnselected(TabLayout.Tab tab ){
        // prepare usage variables
        View v = tab.getCustomView();
        TextView lb = v.findViewById(R.id.lb_name);
        lb.setTextColor(TAB_UNSELECTED_COLOR);

    }
    private void onTabSelected( TabLayout.Tab tab ){
        // prepare usage variables
        View v = tab.getCustomView();
        TextView lb = v.findViewById(R.id.lb_name);
        lb.setTextColor( TAB_SELECTED_COLOR );
    }

    /**
     * View pager
     */
    private void setupViewPager() {
        summaryAdapter = new SummaryAdapter(getActivity(),
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                pages);

        viewPager.setOffscreenPageLimit(pages.size());
        viewPager.setAdapter(summaryAdapter);

    }

    /**
     * Tab host children
     */
    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();

        // prepare usage variables
        for (int a = 0; a < pages.size(); a++) {
            // prepare usage variables
            xFragment page = pages.get( a );
            TabLayout.Tab tab = tabLayout.newTab();
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tab_summary, null);

            // update views
            v.findViewById(R.id.ic_notify).setVisibility(View.GONE);
            ((TextView)v.findViewById(R.id.lb_name)).setText( page.title );

            // update props
            tab.setCustomView( v );

            // keep tab
            tabLayout.addTab(tab);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SummaryPage.this.onTabSelected( tab );

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                SummaryPage.this.onTabUnselected( tab );
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Matching views
     */
    private void matchingViews(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        viewPager = v.findViewById(R.id.view_pager);


    }

    /** Life cycle */
    @Override
    public void onResume() {
        super.onResume();

        onTabSelected(tabLayout.getTabAt( tabLayout.getSelectedTabPosition() ));
    }
}
