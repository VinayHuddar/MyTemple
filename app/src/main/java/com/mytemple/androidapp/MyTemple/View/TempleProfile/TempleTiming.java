package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mytemple.androidapp.MyTemple.Model.Schedules;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleTiming implements TempleProfileFragmentController.ItemClickEventHandler, Schedules.Callback {
    private static final int POOJA_TIMING_SCHEDULE_GROUP_IDX = 0;

    Dialog mTimingDialog;
    Activity mActivity;
    int mTempleId;

    public TempleTiming (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mTimingDialog = new Dialog(mActivity);
        mTimingDialog.setContentView(R.layout.dialog_layout_temple_pooja_timing);
        mTimingDialog.setTitle(Html.fromHtml("<font color='#3C3C3C'>".concat(mActivity.getResources().getString(R.string.temple_profile_item_pooja_timing))));
        mTimingDialog.setCanceledOnTouchOutside(true);
        mTimingDialog.setCancelable(true);
        mTimingDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mTimingDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mTimingDialog.findViewById(R.id.PoojaTiming).setVisibility(View.INVISIBLE);

        mTimingDialog.show();

        (new Schedules(this)).FetchSchedules(mTempleId);
    }

    public void OnTempleSchedulesReceived(Schedules.Model model) {
        mTimingDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mTimingDialog.findViewById(R.id.PoojaTiming).setVisibility(View.VISIBLE);

        int numPoojaTimings = model.GetScheduleCount(POOJA_TIMING_SCHEDULE_GROUP_IDX);
        String poojaTimings = "";
        for (int i = 0; i < numPoojaTimings; i++) {
            String name = model.GetScheduleName(POOJA_TIMING_SCHEDULE_GROUP_IDX, i);
            if ((name != null) && (name.compareTo("") != 0))
                poojaTimings = String.format("%s%s", poojaTimings, name);

            String fromTime = model.GetScheduleFromTime(POOJA_TIMING_SCHEDULE_GROUP_IDX, i);
            if ((fromTime != null) && (fromTime.compareTo("") != 0))
                poojaTimings = String.format("%s %s", poojaTimings, fromTime);

            String toTime = model.GetScheduleToTime(POOJA_TIMING_SCHEDULE_GROUP_IDX, i);
            if ((toTime != null)  && (toTime.compareTo("") != 0))
                poojaTimings = String.format("%s - %s", poojaTimings, toTime);

            String description = model.GetScheduleDescription(POOJA_TIMING_SCHEDULE_GROUP_IDX, i);
            if ((description != null) && (description.compareTo("") != 0))
                poojaTimings = String.format("%s %s", poojaTimings, description);

            poojaTimings = poojaTimings.concat("<br><br>");
        }

        ((TextView)mTimingDialog.findViewById(R.id.PoojaTiming)).setText(Html.fromHtml("<font color='#0000FF'>".concat(poojaTimings)));
    }

    public void OnTempleSchedulesNotReceived(RetrofitError error) {

    }
}

