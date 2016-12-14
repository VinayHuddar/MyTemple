package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.MyTemple.Model.Pandits;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TemplePujaris implements TempleProfileFragmentController.ItemClickEventHandler, Pandits.Callback {
    Dialog mPujariListDialog;
    Activity mActivity;
    int mTempleId;

    public TemplePujaris (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mPujariListDialog = new Dialog(mActivity);
        mPujariListDialog.setContentView(R.layout.dialog_layout_pujari_list);
        mPujariListDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.pujari_list)).concat("</font>")));
        mPujariListDialog.setCancelable(true);
        mPujariListDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mPujariListDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mPujariListDialog.findViewById(R.id.PujariList).setVisibility(View.INVISIBLE);

        mPujariListDialog.show();

        (new Pandits(this)).FetchPanditList(mTempleId);
    }

    public void OnPanditListReceived (Pandits.Model model) {
        mPujariListDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mPujariListDialog.findViewById(R.id.PujariList).setVisibility(View.VISIBLE);

        final ListView pujariList = (ListView) mPujariListDialog.findViewById(R.id.PujariList);
        pujariList.setAdapter(new PanditListAdapter(model));
    }

    public void OnPanditListNotReceived (RetrofitError error) {
    }

    class PanditListAdapter extends BaseAdapter implements ImageDownloader.Callback {
        Pandits.Model mPanditList;
        //ImageDownloader mImageDownloader = new ImageDownloader(this);

        public PanditListAdapter (Pandits.Model panditList) {
            mPanditList = panditList;
        }

        public int getCount() {
            return mPanditList.GetPanditCount();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View pujariListItem;
            if (convertView == null) {
                pujariListItem = inflater.inflate(R.layout.layout_temple_pujari_list_item, null);
            } else {
                pujariListItem = (View) convertView;
            }

            Pandits.Model.Pandit currPandit = mPanditList.GetPandit(position);

            TextView name = (TextView) pujariListItem.findViewById(R.id.Name);
            name.setText(currPandit.GetName());

            TextView about = (TextView) pujariListItem.findViewById(R.id.About);
            about.setText(currPandit.GetDescription());

            //ImageView image = (ImageView) pujariListItem.findViewById(R.id.Photo);
            //mImageDownloader.download(currPandit.GetImage(), image);
            TextView serialNum = (TextView) pujariListItem.findViewById(R.id.SerialNum);
            serialNum.setText(String.valueOf(position+1).concat("."));

            return pujariListItem;
        }

        public long getItemId(int position) {
            return position;
        }

        public Pandits.Model.Pandit getItem(int position) {
            return mPanditList.GetPandit(position);
        }

        public void OnImageReceived () {
        }
    }
}

