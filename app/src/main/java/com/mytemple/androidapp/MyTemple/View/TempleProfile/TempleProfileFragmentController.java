package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;

import com.mytemple.androidapp.R;

import java.util.ArrayList;

public class TempleProfileFragmentController {
    Activity mActivity;
    ArrayList<ProfileItem> mProfileItems;

    int mTempleId;

    public TempleProfileFragmentController(Activity parentActivty, int templeId) {
        mActivity = parentActivty;
        mTempleId = templeId;

        mProfileItems = new ArrayList<>();
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_about_temple, R.drawable.temple_black,
                        new TempleAbout(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_temple_history, R.drawable.ic_history_black_36dp,
                        new TempleHistory(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_pooja_timing, R.drawable.ic_schedule_black_36dp,
                        new TempleTiming(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_seva_list, R.drawable.ic_list_black_36dp,
                        new TempleSevaList(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_pujaris, R.drawable.ic_person_black_36dp,
                        new TemplePujaris(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_trust, R.drawable.ic_group_black_36dp,
                        new TempleTrust(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_gallery, R.drawable.ic_photo_library_black_36dp,
                        new TempleGallery(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_facilities, R.drawable.ic_account_balance_black_36dp,
                        new TempleFacilities(mActivity, mTempleId)));
        mProfileItems.add(new ProfileItem(R.string.temple_profile_item_donors, R.drawable.donation_42dp,
                        new TempleDonorsList(mActivity, mTempleId)));
    }

    class ProfileItem {
        int nameResourceId;
        int iconResourceId;
        ItemClickEventHandler eventHandler;

        public ProfileItem(int _nameResourceId, int _iconResourceId, ItemClickEventHandler _eventHandler) {
            nameResourceId = _nameResourceId;
            iconResourceId = _iconResourceId;
            eventHandler = _eventHandler;
        }
    };

    public int GetProfileItemCount() {
        return mProfileItems.size();
    }

    public int GetProfileItemName(int idx) {
        return mProfileItems.get(idx).nameResourceId;
    }

    public int GetProfileItemIcon(int idx) {
        return mProfileItems.get(idx).iconResourceId;
    }

    public void HandleClickEvent(int idx) {
        mProfileItems.get(idx).eventHandler.HandleEvent();
    }

    class ItemClickEventHandler_Facilities implements ItemClickEventHandler {
        public void HandleEvent() {

        }
    }

    /**
     * Created by vinayhuddar on 13/10/15.
     */
    public static interface ItemClickEventHandler {
        public void HandleEvent();
    }
}