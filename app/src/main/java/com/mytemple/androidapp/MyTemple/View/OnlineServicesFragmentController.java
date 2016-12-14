package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mytemple.androidapp.R;

import java.util.ArrayList;

public class OnlineServicesFragmentController {
    Activity mActivity;

    public OnlineServicesFragmentController(Activity parentActivty) {
        mActivity = parentActivty;
    }

    class ServiceItem {
        int nameResourceId;
        int iconResourceId;
        ItemClickEventHandler eventHandler;

        public ServiceItem(int _nameResourceId, int _iconResourceId, ItemClickEventHandler _eventHandler) {
            nameResourceId = _nameResourceId;
            iconResourceId = _iconResourceId;
            eventHandler = _eventHandler;
        }
    };

    ServiceItem[] mServiceItems = {
            new ServiceItem(R.string.online_services_seva_booking, R.drawable.seva_booking,
                    new ItemClickEventHandler_SevaBooking()),
            new ServiceItem(R.string.online_services_prasada_seva_booking, R.drawable.prasada,
                    new ItemClickEventHandler_SevaBooking()),
            new ServiceItem(R.string.online_services_facilites_booking, R.drawable.facility,
                    new ItemClickEventHandler_SevaBooking())
    };

    interface ItemClickEventHandler {
        public void HandleEvent();
    }

    public int GetServiceItemCount() { return mServiceItems.length; }

    public int GetServiceItemName(int idx) { return mServiceItems[idx].nameResourceId; }

    public int GetServiceItemIcon(int idx) { return mServiceItems[idx].iconResourceId; }

    public void HandleClickEvent (int idx) { mServiceItems[idx].eventHandler.HandleEvent(); }

    class ItemClickEventHandler_SevaBooking implements ItemClickEventHandler {
        ArrayList<Integer> mSelectedSevas;

        public ItemClickEventHandler_SevaBooking () {
            mSelectedSevas = new ArrayList<>();
        }

        public void HandleEvent () {
            final Dialog sevaListDialog = new Dialog(mActivity);
            sevaListDialog.setContentView(R.layout.dialog_layout_seva_booking);
            sevaListDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.online_services_seva_booking)).concat("</font>")));
            sevaListDialog.setCancelable(true);
            sevaListDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

            sevaListDialog.show();

            final ListView mSevaList = (ListView)sevaListDialog.findViewById(R.id.SevaList);
            mSevaList.setAdapter(new SevaListAdapter());

            sevaListDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

            TextView cancelButton = (TextView)sevaListDialog.findViewById(R.id.CancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sevaListDialog.dismiss();
                }
            });

            TextView bookSevaButton = (TextView)sevaListDialog.findViewById(R.id.BookSevaButton);
            bookSevaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedSevas.size() == 0) {
                        Toast.makeText(mActivity, "Please select one or more Sevas to proceed. Or, " +
                                "tap the Cancel to return to the Temple Profile.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "Under Construction", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        class SevaListAdapter extends BaseAdapter {
            public int getCount() {
                return DemoContent_MarutiMandir.RegularSevaList.length;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View sevaItem;
                if (convertView == null) {
                    sevaItem = inflater.inflate(R.layout.list_item_seva_list_multi_choice_selector, null);
                } else {
                    sevaItem = (View) convertView;
                }

                TextView serialNum = (TextView)sevaItem.findViewById(R.id.SerialNum);
                serialNum.setText(String.valueOf(position + 1));

                TextView name = (TextView)sevaItem.findViewById(R.id.SevaName);
                name.setText(mActivity.getResources().getString(DemoContent_MarutiMandir.RegularSevaList[position].sevaNameStringId));

                final CheckedTextView price = (CheckedTextView)sevaItem.findViewById(R.id.SevaPrice);
                price.setText(String.format("\u20b9 %d", DemoContent_MarutiMandir.RegularSevaList[position].price));
                price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (price.isChecked() == false) {
                            mSelectedSevas.add(position);
                            price.setChecked(true);
                        } else {
                            mSelectedSevas.remove(new Integer(position));
                            price.setChecked(false);
                        }
                    }
                });

                return sevaItem;
            }

            public long getItemId(int position) {
                return position;
            }

            public DemoContent_MarutiMandir.Seva getItem(int position) {
                return DemoContent_MarutiMandir.RegularSevaList[position];
            }
        }
    }
}