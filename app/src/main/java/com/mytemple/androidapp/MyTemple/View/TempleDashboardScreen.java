package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.mytemple.androidapp.Common.IntentActionStrings;
import com.mytemple.androidapp.Common.View.BaseActivity;
import com.mytemple.androidapp.Common.View.NavDrawerItemList;
import com.mytemple.androidapp.Common.View.NavigationDrawerCallbacks;
import com.mytemple.androidapp.Common.View.TabsAdapter;
import com.mytemple.androidapp.MyTemple.View.TempleProfile.TempleProfileFragment;
import com.mytemple.androidapp.R;


public class TempleDashboardScreen extends BaseActivity implements NavigationDrawerCallbacks,
        TempleProfileFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener,
        OnlineServicesFragment.OnFragmentInteractionListener, GalleryFragment.OnFragmentInteractionListener {
    Activity mActivity;
    private Context mContext;
    int mTempleId;

    TabHost mTabHost;
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_dashboard);

        super.onCreateDrawer();
        mNavigationDrawerFragment.SetDefaultSelectedPosition(NavDrawerItemList.BROWSE_ITEM_ID);

        mContext = mActivity = this;

        mTempleId = getIntent().getIntExtra(IntentActionStrings.TEMPLE_ID, 0);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        Bundle args = new Bundle();
        args.putInt(IntentActionStrings.TEMPLE_ID, mTempleId);
        mTabsAdapter.addTab(mTabHost.newTabSpec("profile").setIndicator(getResources().getString(R.string.dashboard_tab_title_temple_profile)),
                TempleProfileFragment.class, args);
        mTabsAdapter.addTab(mTabHost.newTabSpec("events").setIndicator(getResources().getString(R.string.dashboard_tab_title_events)),
                EventsFragment.class, args);
        mTabsAdapter.addTab(mTabHost.newTabSpec("online_services").setIndicator(getResources().getString(R.string.dashboard_tab_title_online_services)),
                OnlineServicesFragment.class, args);
        //mTabsAdapter.addTab(mTabHost.newTabSpec("gallery").setIndicator(getResources().getString(R.string.dashboard_tab_title_temple_gallery)),
        //        GalleryFragment.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++)
            mTabHost.getTabWidget().getChildAt(i).setPadding(16, 0, 16, 0);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        TabWidget widget = mTabHost.getTabWidget();
        //widget.setDividerDrawable(getResources().getDrawable(R.drawable.tab_widget_divider));
        //widget.setShowDividers(TabWidget.SHOW_DIVIDER_BEGINNING);
        for (int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tabTitle = (TextView)v.findViewById(android.R.id.title);
            if(tabTitle == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_selector);

            tabTitle.setGravity(Gravity.CENTER);
            tabTitle.setSingleLine(false);
            tabTitle.setTextSize(14);
        }

        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_temples, menu);

        return true; //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            //Intent intent = new Intent(this, SearchQueryTakerActivity.class);
            //startActivity(intent);

            return true;
        } else if (id == R.id.action_donate) {
            final Dialog hundiDialog = new Dialog(mActivity);
            hundiDialog.setContentView(R.layout.dialog_layout_hundi);
            hundiDialog.setTitle(Html.fromHtml("<font color='#0000FF'>".concat(mActivity.getResources().getString(R.string.hundi)).concat("</font>")));
            hundiDialog.setCancelable(true);
            hundiDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

            TextView cancelButton = (TextView)hundiDialog.findViewById(R.id.HundiCancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hundiDialog.cancel();
                }
            });

            TextView submitButton = (TextView)hundiDialog.findViewById(R.id.HundiSubmitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText amount = (EditText)hundiDialog.findViewById(R.id.HundiAmount);
                    String donationAmount = amount.getText().toString();
                    if (donationAmount.compareTo("") == 0)
                        Toast.makeText(mActivity, "Please enter amount before submitting", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mActivity, "Under Construction", Toast.LENGTH_SHORT).show();

                }
            });

            hundiDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    @Override
    public int getDefaultItemSelectId() {
        return NavDrawerItemList.BROWSE_ITEM_ID;
    }

    public void onProfileFragmentInteraction (Uri uri) {
    }
    public void onOccassionsFragmentInteraction (Uri uri) {
    }
    public void onGalleryFragmentInteraction (Uri uri) {
    }
    public void onActivitiesFragmentInteraction (Uri uri) {
    }
}
