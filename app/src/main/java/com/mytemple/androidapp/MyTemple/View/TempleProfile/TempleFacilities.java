package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.MyTemple.Model.Facilities;
import com.mytemple.androidapp.MyTemple.Model.Pandits;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleFacilities implements TempleProfileFragmentController.ItemClickEventHandler, Facilities.Callback {
    Dialog mFacilitiesDialog;
    Activity mActivity;
    int mTempleId;

    public TempleFacilities(Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mFacilitiesDialog = new Dialog(mActivity);
        mFacilitiesDialog.setContentView(R.layout.dialog_layout_facilities);
        mFacilitiesDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.temple_facilities)).concat("</font>")));
        mFacilitiesDialog.setCancelable(true);
        mFacilitiesDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mFacilitiesDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mFacilitiesDialog.findViewById(R.id.FacilitiesList).setVisibility(View.INVISIBLE);

        mFacilitiesDialog.show();

        (new Facilities(this)).FetchFacilitiesList(mTempleId);
    }

    public void OnFacilitiesListReceived (Facilities.Model model) {
        mFacilitiesDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mFacilitiesDialog.findViewById(R.id.FacilitiesList).setVisibility(View.VISIBLE);

        final ListView facilitiesList = (ListView) mFacilitiesDialog.findViewById(R.id.FacilitiesList);
        facilitiesList.setAdapter(new FacilitiesListAdapter(model));
    }

    public void OnFacilitiesListNotReceived (RetrofitError error) {
    }

    class FacilitiesListAdapter extends BaseAdapter implements ImageDownloader.Callback {
        Facilities.Model mFacilitiesList;
        ImageDownloader mImageDownloader = new ImageDownloader(this);

        public FacilitiesListAdapter(Facilities.Model facilitiesList) {
            mFacilitiesList = facilitiesList;
        }

        public int getCount() {
            return mFacilitiesList.GetFacilitiesCount();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View facilitiesListItem;
            if (convertView == null) {
                facilitiesListItem = inflater.inflate(R.layout.layout_temple_facility_item, null);
            } else {
                facilitiesListItem = (View) convertView;
            }

            Facilities.Model.Facility currFacility = mFacilitiesList.GetFacility(position);

            TextView name = (TextView) facilitiesListItem.findViewById(R.id.FacilityName);
            name.setText(currFacility.GetName());

            TextView about = (TextView) facilitiesListItem.findViewById(R.id.FacilityDescription);
            about.setText(currFacility.GetDescription());

            if (currFacility.GetImage() != null) {
                ImageView imageView = (ImageView) facilitiesListItem.findViewById(R.id.FacilityImage);
                mImageDownloader.download(currFacility.GetImage(), imageView);
            }

            return facilitiesListItem;
        }

        public long getItemId(int position) {
            return position;
        }

        public Facilities.Model.Facility getItem(int position) {
            return mFacilitiesList.GetFacility(position);
        }

        public void OnImageReceived () {
        }
    }
}

