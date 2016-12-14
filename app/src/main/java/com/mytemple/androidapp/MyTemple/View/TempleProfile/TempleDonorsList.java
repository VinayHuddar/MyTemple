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
import com.mytemple.androidapp.MyTemple.Model.Donors;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleDonorsList implements TempleProfileFragmentController.ItemClickEventHandler, Donors.Callback, ImageDownloader.Callback {
    private static final int DONORS_GROUP_IDX = 0;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    ViewFlipper mDonorsListFlipper;
    GestureDetector mGestureDetector;

    int mCurrentScreen = 0;
    int mDonorsListCnt = 0;

    ImageDownloader mImageDownloader = new ImageDownloader(this);

    Dialog mDonorsListDialog;
    Activity mActivity;
    int mTempleId;

    public TempleDonorsList (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mDonorsListDialog = new Dialog(mActivity);
        mDonorsListDialog.setContentView(R.layout.dialog_layout_donors);
        mDonorsListDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.temple_profile_item_donors)).concat("</font>")));
        mDonorsListDialog.setCancelable(true);
        mDonorsListDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mDonorsListDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mDonorsListDialog.findViewById(R.id.DonorListFlipperLayout).setVisibility(View.INVISIBLE);

        mDonorsListDialog.show();

        (new Donors(this)).FetchDonorsList(mTempleId);
    }

    public void OnDonorsReceived(Donors.Model model) {
        mDonorsListFlipper = (ViewFlipper) mDonorsListDialog.findViewById(R.id.DonorListFlipper);
        mDonorsListCnt = model.GetDonorsCount(DONORS_GROUP_IDX); //DemoContent_MarutiMandir.MilestoneDates.length;
        for (int i = 0; i < mDonorsListCnt; i++) {
            ImageView donorsListImage = new ImageView(mActivity);
            mImageDownloader.download(model.GetDonorImage(DONORS_GROUP_IDX, i), donorsListImage);
            //donorsListImage.setImageDrawable(mActivity.getResources().getDrawable(DemoContent_MarutiMandir.MilestoneDrawables[i]));
                /*TextView donorsListImage = new TextView(mActivity);
                donorsListImage.setText(Html.fromHtml(mActivity.getResources().getString(DemoContent_MarutiMandir.Milestones[i])));
                donorsListImage.setGravity(Gravity.CENTER_HORIZONTAL);
                donorsListImage.setTextColor(mActivity.getResources().getColor(R.color.white));*/
            mDonorsListFlipper.addView(donorsListImage);
        }
        TextView pageIndicator = (TextView) mDonorsListDialog.findViewById(R.id.PageIndicator);
        pageIndicator.setText(String.format("1 / %d", mDonorsListCnt));

        mGestureDetector = new GestureDetector(new SwipeGestureDetector(mDonorsListDialog, model));
        mDonorsListFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        TextView donorsListHdg = (TextView) mDonorsListDialog.findViewById(R.id.DonorListHeading);
        //donorsListHdg.setText(DemoContent_MarutiMandir.MilestoneDates[0]);
        donorsListHdg.setText(model.GetDonorName(DONORS_GROUP_IDX, 0));
    }

    public void OnDonorsNotReceived(RetrofitError error) {
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        Dialog mRootView;
        Donors.Model mModel;

        public SwipeGestureDetector(Dialog rootView, Donors.Model model) {
            mRootView = rootView;
            mModel = model;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean updateSliderIndicator = false;
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mDonorsListFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_in));
                    mDonorsListFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_out));
                    mDonorsListFlipper.showNext();

                    mCurrentScreen = (mCurrentScreen == mDonorsListCnt - 1 ? 0 : mCurrentScreen + 1);
                    updateSliderIndicator = true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mDonorsListFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_in));
                    mDonorsListFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_out));
                    mDonorsListFlipper.showPrevious();

                    mCurrentScreen = (mCurrentScreen == 0 ? mDonorsListCnt - 1 : mCurrentScreen - 1);
                    updateSliderIndicator = true;
                }

                if (updateSliderIndicator) {
                    TextView milestoneIndicator = (TextView) mRootView.findViewById(R.id.PageIndicator);
                    milestoneIndicator.setText(String.format("%d / %d", mCurrentScreen+1, mDonorsListCnt));

                    TextView donorListHdg = (TextView) mRootView.findViewById(R.id.DonorListHeading);
                    //donorListHdg.setText(DemoContent_MarutiMandir.MilestoneDates[mCurrentScreen]);
                    donorListHdg.setText(mModel.GetDonorName(DONORS_GROUP_IDX, mCurrentScreen));

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public void OnImageReceived () {
        mDonorsListDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mDonorsListDialog.findViewById(R.id.DonorListFlipperLayout).setVisibility(View.VISIBLE);
    }
}
