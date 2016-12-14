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

import com.mytemple.androidapp.MyTemple.Model.Sevas;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleSevaList implements TempleProfileFragmentController.ItemClickEventHandler, Sevas.Callback {
    private static final int SEVA_LIST_GROUP_IDX = 0;

    Dialog mSevaListDialog;
    Activity mActivity;
    int mTempleId;

    public TempleSevaList (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mSevaListDialog = new Dialog(mActivity);
        mSevaListDialog.setContentView(R.layout.dialog_layout_seva_list);
        mSevaListDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.temple_profile_item_seva_list)).concat("</font>")));
        mSevaListDialog.setCancelable(true);
        mSevaListDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mSevaListDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mSevaListDialog.findViewById(R.id.SevaListLayout).setVisibility(View.INVISIBLE);

        mSevaListDialog.show();

        (new Sevas(this)).FetchSevas(mTempleId);
    }

    public void OnSevaListReceived (Sevas.Model model) {
        mSevaListDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mSevaListDialog.findViewById(R.id.SevaListLayout).setVisibility(View.VISIBLE);

        final ListView mSevaList = (ListView) mSevaListDialog.findViewById(R.id.SevaList);
        mSevaList.setAdapter(new SevaListAdapter(model));
    }

    public void OnSevaListNotReceived (RetrofitError error) {
    }

    class SevaListAdapter extends BaseAdapter {
        Sevas.Model mSevaList;
        public SevaListAdapter (Sevas.Model sevaList) {
            mSevaList = sevaList;
        }

        public int getCount() {
            return mSevaList.GetSevaCount(SEVA_LIST_GROUP_IDX);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View sevaItem;
            if (convertView == null) {
                sevaItem = inflater.inflate(R.layout.list_item_seva_list, null);
            } else {
                sevaItem = (View) convertView;
            }

            TextView serialNum = (TextView) sevaItem.findViewById(R.id.SerialNum);
            serialNum.setText(String.valueOf(position + 1));

            TextView name = (TextView) sevaItem.findViewById(R.id.SevaName);
            //name.setText(mActivity.getResources().getString(DemoContent_MarutiMandir.RegularSevaList[position].sevaNameStringId));
            name.setText(mSevaList.GetSevaName(SEVA_LIST_GROUP_IDX, position));

            final TextView price = (TextView) sevaItem.findViewById(R.id.SevaPrice);
            //price.setText(String.format("\u20b9 %d", DemoContent_MarutiMandir.RegularSevaList[position].price));
            price.setText(String.valueOf(mSevaList.GetSevaPrice(SEVA_LIST_GROUP_IDX, position)));

            return sevaItem;
        }

        public long getItemId(int position) {
            return position;
        }

        public Sevas.Model.Seva getItem(int position) {
            return mSevaList.GetSeva(SEVA_LIST_GROUP_IDX, position);
        }
    }
}

