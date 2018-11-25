package com.rune.travelpal.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GoogleApiAvailability;

import com.rune.travelpal.R;
import com.rune.travelpal.data.Broadcasts;
import com.rune.travelpal.fragment.MainFragment;
import com.rune.travelpal.fragment.TabFragment;
import com.rune.travelpal.fragment.TrackerFragment;
import rune.paging.FragmentPagerAdapter;
import rune.paging.FragmentPager;

public class MainActivity extends AbstractActivity {

    // Fields

    private static final int DEFAULT_VIEWPAGER_INDEX = 1;

    private FragmentPager mViewPager;

    private ActionBar.TabListener mTabListener;

    private TabFragment[] viewPagerFragments = new TabFragment[] {
            MainFragment.newInstance(),
            TrackerFragment.newInstance(),
        };

    // Constructor

    public MainActivity() {
        super(R.layout.activity_main, MainActivity.class);
    }

    // Event handlers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getDataGateway().isClientIdSet()) {
            log.d("Client id not set, redirecting to client id setup activity..");

            startActivity(new Intent(this, SetupClientIdActivity.class));
            finish();
        }

        mViewPager = (FragmentPager) findViewById(R.id.viewPager);
        setupViewPager();
        setTabListener();
        createTabs();

        if (savedInstanceState == null) {
            setStartupDisplayFragment();
        }
    }

    private void setStartupDisplayFragment() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AppPreferenceActivity.class));
            return true;
        }

        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }

        if (id == R.id.action_reload) {
            sendBroadcast(new Intent(Broadcasts.BROADCAST_RELOAD_TICKET_STATUS));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        log.d("onResume, client TravelPal-id is " + getDataGateway().getClientId());
        log.d("\tis google services available:" + GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this));
    }

    // Methods

    @NonNull
    public ActionBar getActionBar() {
        ActionBar bar = super.getActionBar();
        if (bar == null)
            throw new RuntimeException("Tab bar feature is not supported.");

        return bar;
    }

    private void setTabListener() {
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mTabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                int index = tab.getPosition();
                log.d("Clicked tab index " + index);
                mViewPager.setCurrentItem(index);
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) { }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) { }
        };
    }

    private void createTabs() {
        final ActionBar actionBar = getActionBar();

        // Add 3 tabs, specifying the tab's text and TabListener
        for(TabFragment fragment : viewPagerFragments) {
            actionBar.addTab(
                actionBar.newTab()
                    .setText(fragment.getTabName())
                    .setTabListener(mTabListener)
                );
        }
    }

    private void setupViewPager() {
        mViewPager.setOnPageChangeListener(new FragmentPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ActionBar bar = getActionBar();
                bar.selectTab(bar.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(
                new FragmentPagerAdapter(getFragmentManager(), viewPagerFragments)
        );
    }

}
