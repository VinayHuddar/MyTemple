package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mytemple.androidapp.Common.View.BaseActivity;
import com.mytemple.androidapp.Common.View.NavDrawerItemList;
import com.mytemple.androidapp.Common.View.NavigationDrawerCallbacks;
import com.mytemple.androidapp.R;


public class TestScreen extends BaseActivity implements NavigationDrawerCallbacks {
    Activity mActivity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        super.onCreateDrawer();
        mNavigationDrawerFragment.SetDefaultSelectedPosition(NavDrawerItemList.BROWSE_ITEM_ID);

        mContext = mActivity = this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    @Override
    public int getDefaultItemSelectId() {
        return NavDrawerItemList.BROWSE_ITEM_ID;
    }
}
