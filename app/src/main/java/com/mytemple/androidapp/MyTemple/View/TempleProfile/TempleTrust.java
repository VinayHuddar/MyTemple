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
import com.mytemple.androidapp.MyTemple.Model.Trusts;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleTrust implements TempleProfileFragmentController.ItemClickEventHandler, Trusts.Callback, ImageDownloader.Callback {
    private static final int TRUST_GROUP_IDX = 0;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    ViewFlipper mTrustFlipper;
    GestureDetector mGestureDetector;

    int mCurrentScreen = 0;
    int mTrustGroupCnt = 0;

    ImageDownloader mImageDownloader = new ImageDownloader(this);

    Dialog mTrustDialog;
    Activity mActivity;
    int mTempleId;

    public TempleTrust (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mTrustDialog = new Dialog(mActivity);
        mTrustDialog.setContentView(R.layout.dialog_layout_trustee_list);
        mTrustDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.trust)).concat("</font>")));
        mTrustDialog.setCancelable(true);
        mTrustDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mTrustDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mTrustDialog.findViewById(R.id.TrustLayout).setVisibility(View.INVISIBLE);

        mTrustDialog.show();

        (new Trusts(this)).FetchTrusts(mTempleId);
    }

    public void OnTempleTrustsReceived (Trusts.Model model) {
        TextView trustName = (TextView) mTrustDialog.findViewById(R.id.TrustName);
        trustName.setText(model.GetTrustName(TRUST_GROUP_IDX));

        //ListView trusteeList = (ListView) mTrustDialog.findViewById(R.id.TrusteeList);
        //trusteeList.setAdapter(new TrusteeListAdapter(model));

        mTrustFlipper = (ViewFlipper) mTrustDialog.findViewById(R.id.TrustFlipper);
        mTrustGroupCnt = model.GetTrustGroupCount();
        for (int i = 0; i < mTrustGroupCnt; i++) {
            ImageView trustGroupImg = new ImageView(mActivity);
            mImageDownloader.download(model.GetTrustGroupImage(i), trustGroupImg);
            mTrustFlipper.addView(trustGroupImg);
        }
        TextView pageIndicator = (TextView) mTrustDialog.findViewById(R.id.PageIndicator);
        pageIndicator.setText(String.format("1 / %d", mTrustGroupCnt));

        mGestureDetector = new GestureDetector(new SwipeGestureDetector(mTrustDialog, model));
        mTrustFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        TextView trustDate = (TextView) mTrustDialog.findViewById(R.id.TrustDate);
        trustDate.setText(model.GetTrustGroupDescription(0));
    }

    public void OnTempleTrustsNotReceived(RetrofitError error) {

    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        Dialog mRootView;
        Trusts.Model mModel;

        public SwipeGestureDetector(Dialog rootView, Trusts.Model model) {
            mRootView = rootView;
            mModel = model;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean updateSliderIndicator = false;
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mTrustFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_in));
                    mTrustFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.left_out));
                    mTrustFlipper.showNext();

                    mCurrentScreen = (mCurrentScreen == mTrustGroupCnt - 1 ? 0 : mCurrentScreen + 1);
                    updateSliderIndicator = true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mTrustFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_in));
                    mTrustFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_out));
                    mTrustFlipper.showPrevious();

                    mCurrentScreen = (mCurrentScreen == 0 ? mTrustGroupCnt - 1 : mCurrentScreen - 1);
                    updateSliderIndicator = true;
                }

                if (updateSliderIndicator) {
                    TextView pageIndicator = (TextView) mRootView.findViewById(R.id.PageIndicator);
                    pageIndicator.setText(String.format("%d / %d", mCurrentScreen + 1, mTrustGroupCnt));

                    TextView trustDate = (TextView) mRootView.findViewById(R.id.TrustDate);
                    trustDate.setText(mModel.GetTrustGroupDescription(mCurrentScreen));

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public void OnImageReceived () {
        mTrustDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mTrustDialog.findViewById(R.id.TrustLayout).setVisibility(View.VISIBLE);
    }


        /*class TrusteeListAdapter extends BaseAdapter implements ImageDownloader.Callback {
            Trusts.Model mTrusts;
            ImageDownloader mImageDownloader = new ImageDownloader(this);

            public TrusteeListAdapter (Trusts.Model trusts) {
                mTrusts = trusts;
            }

            public int getCount() {
                return mTrusts.GetMemberCount(TRUST_GROUP_IDX);
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View trusteeListItem;
                if (convertView == null) {
                    trusteeListItem = inflater.inflate(R.layout.layout_temple_trustee_list_item, null);
                } else {
                    trusteeListItem = (View) convertView;
                }

                Trusts.Model.Trustee currTrustee = mTrusts.GetTrustee(TRUST_GROUP_IDX, position);
                //DemoContent_MarutiMandir.Trustee currTrustee = DemoContent_MarutiMandir.Trust[position];
                TextView name = (TextView) trusteeListItem.findViewById(R.id.Name);
                name.setText(currTrustee.GetName());

                TextView designation = (TextView) trusteeListItem.findViewById(R.id.Designation);
                designation.setText(currTrustee.GetDesignation());

                TextView serialNum = (TextView) trusteeListItem.findViewById(R.id.SerialNum);
                serialNum.setText(String.valueOf(position+1).concat("."));

                return trusteeListItem;
            }

            public long getItemId(int position) {
                return position;
            }

            public Trusts.Model.Trustee getItem(int position) {
                return mTrusts.GetTrustee(TRUST_GROUP_IDX, position);
            }

            public void OnImageReceived () {
            }
        }*/
}

