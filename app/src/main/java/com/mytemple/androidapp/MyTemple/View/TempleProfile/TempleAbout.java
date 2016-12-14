package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mytemple.androidapp.MyTemple.Model.Temple;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
public class TempleAbout implements TempleProfileFragmentController.ItemClickEventHandler, Temple.Callback {
    Dialog mAboutDialog;
    Activity mActivity;
    int mTempleId;

    public TempleAbout (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mAboutDialog = new Dialog(mActivity);
        mAboutDialog.setContentView(R.layout.dialog_layout_temple_about);
        mAboutDialog.setTitle(Html.fromHtml("<font color='#3C3C3C'>".concat(mActivity.getResources().getString(R.string.temple_profile_item_about_temple))));
        mAboutDialog.setCanceledOnTouchOutside(true);
        mAboutDialog.setCancelable(true);
        mAboutDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mAboutDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mAboutDialog.findViewById(R.id.AboutTemple).setVisibility(View.INVISIBLE);

        mAboutDialog.show();

        (new Temple(this)).FetchTempleInfo(mTempleId);
    }

    public void OnTempleInfoReceived(Temple.Model templeInfo) {
        mAboutDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mAboutDialog.findViewById(R.id.AboutTemple).setVisibility(View.VISIBLE);

        ((TextView)mAboutDialog.findViewById(R.id.AboutTemple)).setText(Html.fromHtml(templeInfo.GetTempleInfo().GetDescription()));
    }

    public void OnTempleInfoNotReceived(RetrofitError error) {

    }
}

