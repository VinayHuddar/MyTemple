package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.MyTemple.Model.History;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleHistory implements TempleProfileFragmentController.ItemClickEventHandler, History.Callback, ImageDownloader.Callback {
    private static final int HISTORY_GROUP_IDX = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    ViewFlipper mHistoryViewFlipper;
    GestureDetector mGestureDetector;

    int mCurrentScreen = 0;
    int mMilestonesCnt = 0;

    ImageDownloader mImageDownloader = new ImageDownloader(this);

    Dialog mHistoryDialog;
    Activity mActivity;
    int mTempleId;

    public TempleHistory (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mHistoryDialog = new Dialog(mActivity);
        mHistoryDialog.setContentView(R.layout.dialog_layout_temple_history);
        mHistoryDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.historical_events)).concat("</font>")));
        mHistoryDialog.setCancelable(true);
        mHistoryDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mHistoryDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mHistoryDialog.findViewById(R.id.HistoryFlipperLayout).setVisibility(View.INVISIBLE);

        mHistoryDialog.show();

        (new History(this)).FetchTempleHistory(mTempleId);
    }

    public void OnTempleHistoryReceived(History.Model model) {
        mHistoryViewFlipper = (ViewFlipper) mHistoryDialog.findViewById(R.id.HistoryFlipper);
        mMilestonesCnt = model.GetMilestoneCount(HISTORY_GROUP_IDX); //DemoContent_MarutiMandir.MilestoneDates.length;
        for (int i = 0; i < mMilestonesCnt; i++) {
            ImageView milestone = new ImageView(mActivity);
            mImageDownloader.download(model.GetMilestoneImage(HISTORY_GROUP_IDX, i), milestone);
            //milestone.setImageDrawable(mActivity.getResources().getDrawable(DemoContent_MarutiMandir.MilestoneDrawables[i]));
                /*TextView milestone = new TextView(mActivity);
                milestone.setText(Html.fromHtml(mActivity.getResources().getString(DemoContent_MarutiMandir.Milestones[i])));
                milestone.setGravity(Gravity.CENTER_HORIZONTAL);
                milestone.setTextColor(mActivity.getResources().getColor(R.color.white));*/
            mHistoryViewFlipper.addView(milestone);
        }
        TextView milestoneIndicator = (TextView) mHistoryDialog.findViewById(R.id.MilestoneIndicator);
        milestoneIndicator.setText(String.format("1 / %d", mMilestonesCnt));

        mGestureDetector = new GestureDetector(new SwipeGestureDetector(mHistoryDialog, model));
        mHistoryViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        TextView milestoneDate = (TextView) mHistoryDialog.findViewById(R.id.MilestoneDate);
        //milestoneDate.setText(DemoContent_MarutiMandir.MilestoneDates[0]);
        milestoneDate.setText(model.GetMilestoneName(HISTORY_GROUP_IDX, 0));
    }

    public void OnTempleHistoryNotReceived(RetrofitError error) {
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        Dialog mRootView;
        History.Model mModel;

        public SwipeGestureDetector(Dialog rootView, History.Model model) {
            mRootView = rootView;
            mModel = model;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean updateSliderIndicator = false;
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mHistoryViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_in));
                    mHistoryViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_out));
                    mHistoryViewFlipper.showNext();

                    mCurrentScreen = (mCurrentScreen == mMilestonesCnt - 1 ? 0 : mCurrentScreen + 1);
                    updateSliderIndicator = true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mHistoryViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_in));
                    mHistoryViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_out));
                    mHistoryViewFlipper.showPrevious();

                    mCurrentScreen = (mCurrentScreen == 0 ? mMilestonesCnt - 1 : mCurrentScreen - 1);
                    updateSliderIndicator = true;
                }

                if (updateSliderIndicator) {
                    TextView milestoneIndicator = (TextView) mRootView.findViewById(R.id.MilestoneIndicator);
                    milestoneIndicator.setText(String.format("%d / %d", mCurrentScreen+1, mMilestonesCnt));

                    TextView milestoneDate = (TextView) mRootView.findViewById(R.id.MilestoneDate);
                    //milestoneDate.setText(DemoContent_MarutiMandir.MilestoneDates[mCurrentScreen]);
                    milestoneDate.setText(mModel.GetMilestoneName(HISTORY_GROUP_IDX, mCurrentScreen));

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public void OnImageReceived () {
        mHistoryDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mHistoryDialog.findViewById(R.id.HistoryFlipperLayout).setVisibility(View.VISIBLE);
    }
}

