package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.Common.IntentActionStrings;
import com.mytemple.androidapp.Common.View.BaseActivity;
import com.mytemple.androidapp.Common.View.NavDrawerItemList;
import com.mytemple.androidapp.Common.View.NavigationDrawerCallbacks;
import com.mytemple.androidapp.MyTemple.Model.Events;
import com.mytemple.androidapp.MyTemple.Model.GalleryImages;
import com.mytemple.androidapp.R;

import java.util.HashMap;

import retrofit.RetrofitError;


public class MainTempleScreen extends BaseActivity implements NavigationDrawerCallbacks, GalleryImages.Callback,
        Events.Callback, ImageDownloader.Callback {
    final int MAIN_SLIDER_IMAGE_GALLERY_IDX = 0;
    final int SWIPE_MIN_DISTANCE = 120;
    final int SWIPE_THRESHOLD_VELOCITY = 200;

    final int EVENT_TYPE_ABHISHEKA = 1;
    final int EVENT_TYPE_HOMA = 2;

    Activity mActivity;
    Context mContext;

    ViewFlipper mViewFlipper;
    GestureDetector mGestureDetector;
    ImageDownloader mImageDownloader;
    int[] mSliderIndicators = {R.id.SlideIndicator1, R.id.SlideIndicator2, R.id.SlideIndicator3, R.id.SlideIndicator4};

    int mCurrentScreen = 0;
    int mFlipperImageCnt = 0;

    int mTempleId;
    String mTempleName;

    class EventColor {
        int headingColor;
        int bodyColor;

        public EventColor(int _headingColor, int _bodyColor) {
            headingColor = _headingColor;
            bodyColor = _bodyColor;
        }
    }
    HashMap<Integer, EventColor> mEventColors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_main);

        super.onCreateDrawer();
        mNavigationDrawerFragment.SetDefaultSelectedPosition(NavDrawerItemList.BROWSE_ITEM_ID);

        mContext = mActivity = this;

        mImageDownloader = new ImageDownloader(this);
        mGestureDetector = new GestureDetector(new SwipeGestureDetector());

        mTempleId = getIntent().getIntExtra(IntentActionStrings.TEMPLE_ID, 0);
        mTempleName = getIntent().getStringExtra(IntentActionStrings.TEMPLE_NAME);
        setTitle(mTempleName);

        findViewById(R.id.loading_message).setVisibility(View.VISIBLE);

        // Fetch images for the flipper
        GalleryImages galleryImages = new GalleryImages(this);
        galleryImages.FetchGalleryImages(mTempleId);

        // Fetch events
        Events events = new Events(this);
        events.FetchEvents(mTempleId);
    }

    public void OnGalleryImagesReceived(GalleryImages.Model model) {
        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

        mViewFlipper  = (ViewFlipper)findViewById(R.id.ImageFlipper);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));

        mFlipperImageCnt = model.GetImageCount(MAIN_SLIDER_IMAGE_GALLERY_IDX);
        ImageView[] flipperImageViews = {
                (ImageView)findViewById(R.id.SliderImage1), (ImageView)findViewById(R.id.SliderImage2),
                (ImageView)findViewById(R.id.SliderImage3), (ImageView)findViewById(R.id.SliderImage4)
        };
        for (int i = 0; i < mFlipperImageCnt; i++) {
            mImageDownloader.download(model.GetImage(MAIN_SLIDER_IMAGE_GALLERY_IDX, i).GetImageUrl(), flipperImageViews[i]);
            flipperImageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
        }

        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mViewFlipper.stopFlipping();

                mCurrentScreen = mViewFlipper.getDisplayedChild();
                RelativeLayout indicator = (RelativeLayout) findViewById(R.id.ImageFlipperIndicator);
                indicator.setVisibility(View.VISIBLE);

                final ImageView currIndImage = (ImageView) findViewById(mSliderIndicators[mCurrentScreen]);
                currIndImage.setBackground(getResources().getDrawable(R.drawable.circle_red));

                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void OnGalleryImagesNotReceived(RetrofitError error) {

    }

    EventsListAdapter mEventsListAdapter;
    public void OnEventsReceived(final Events.Model model) {
        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

        final ExpandableListView mEventsListELV = (ExpandableListView) findViewById(R.id.EventsList);
        final EventsListAdapter eventsListAdapter = new EventsListAdapter(model);
        mEventsListAdapter = eventsListAdapter;
        mEventsListELV.setAdapter(eventsListAdapter);

        for (int i = 0; i < eventsListAdapter.getGroupCount(); i++)
            mEventsListELV.expandGroup(i);

        mEventsListELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Events.Model.Event selProd = (Events.Model.Event) eventsListAdapter.getChild(groupPosition, childPosition);

                final Dialog eventDialog = new Dialog(mActivity);
                eventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                eventDialog.setContentView(R.layout.dialog_layout_special_event);
                eventDialog.setCancelable(true);
                eventDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

                eventDialog.show();

                Events.Model.Event selectedEvent = model.GetEvent(groupPosition, childPosition);
                TextView eventTitle = (TextView) eventDialog.findViewById(R.id.EventTitle);
                eventTitle.setText(selectedEvent.GetName());
                int headingColor = mActivity.getResources().getColor(mEventColors.get(EVENT_TYPE_HOMA).headingColor);
                eventTitle.setBackgroundColor(headingColor);

                TextView eventDate = (TextView) eventDialog.findViewById(R.id.EventDate);
                eventDate.setText(selectedEvent.GetStartDate());
                eventDate.setBackgroundColor(headingColor);

                TextView eventDescription = (TextView) eventDialog.findViewById(R.id.EventDescription);
                eventDescription.setText(Html.fromHtml(selectedEvent.GetDescription()));
                final int bodyColor = mActivity.getResources().getColor(mEventColors.get(EVENT_TYPE_HOMA).bodyColor);
                eventDescription.setBackgroundColor(bodyColor);

                return false;
            }
        });

        mEventColors.put(EVENT_TYPE_ABHISHEKA, new EventColor(R.color.event_type_color_abhisheka_heading, R.color.event_type_color_abhisheka_body));
        mEventColors.put(EVENT_TYPE_HOMA, new EventColor(R.color.primary_color, R.color.primary_color_light_shade_1));

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.GotoTempleDashboard);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        findViewById(R.id.GotoTempleDashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TempleDashboardScreen.class);
                intent.putExtra(IntentActionStrings.TEMPLE_ID, mTempleId);
                startActivity(intent);
            }
        });
    }

    public void OnEventsNotReceived(RetrofitError error) {
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

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean updateSliderIndicator = false;
            int prevScreen = -1;
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    mViewFlipper.showNext();

                    prevScreen = mCurrentScreen;
                    mCurrentScreen = (mCurrentScreen == mFlipperImageCnt-1 ? 0 : mCurrentScreen + 1);
                    updateSliderIndicator = true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));
                    mViewFlipper.showPrevious();

                    prevScreen = mCurrentScreen;
                    mCurrentScreen = (mCurrentScreen == 0 ? mFlipperImageCnt-1 : mCurrentScreen - 1);
                    updateSliderIndicator = true;
                }

                if (updateSliderIndicator) {
                    ImageView currScreenInd = (ImageView)findViewById(mSliderIndicators[mCurrentScreen]);
                    currScreenInd.setBackground(getResources().getDrawable(R.drawable.circle_red));

                    ImageView prevScreenInd = (ImageView)findViewById(mSliderIndicators[prevScreen]);
                    prevScreenInd.setBackground(getResources().getDrawable(R.drawable.circle_white));

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class EventsListAdapter extends BaseExpandableListAdapter {
        Events.Model mEventsList;

        public EventsListAdapter(Events.Model eventsList) {
            mEventsList = eventsList;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return mEventsList.GetEvent(groupPosition, childPosition).GetId();
        }

        public int getChildrenCount(int groupPosition) {
            return mEventsList.GetEventCount(groupPosition);
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public Object getGroup(int groupPosition) {
            return mEventsList.GetEventGroup(groupPosition);
        }

        public Object getChild(int groupPosition, int childPosition) {
            return mEventsList.GetEvent(groupPosition, childPosition);
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(mActivity);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(60, 0, 0, 0);
            textView.setText(mEventsList.GetGroupName(groupPosition));
            textView.setTextColor(mActivity.getResources().getColor(R.color.white));
            textView.setTextSize(16);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setBackgroundColor(mActivity.getResources().getColor(R.color.primary_color));
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            final Events.Model.Event currEvent = mEventsList.GetEvent(groupPosition, childPosition);

            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View currItem;
            if (convertView == null) {
                currItem = inflater.inflate(R.layout.layout_event_list_item, null);
            } else {
                currItem = (View) convertView;
            }

            TextView eventTitle = (TextView)currItem.findViewById(R.id.EventTitle);
            eventTitle.setText(currEvent.GetName());

            TextView eventDate = (TextView)currItem.findViewById(R.id.EventDate);
            eventDate.setText(currEvent.GetStartDate());

            return currItem;
        }
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        public int getGroupCount() {
            return mEventsList.GetGroupCount();
        }
        public boolean hasStableIds() {
            return true;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    @Override
    public int getDefaultItemSelectId() {
        return NavDrawerItemList.BROWSE_ITEM_ID;
    }

    public void OnImageReceived () {
    }
}
