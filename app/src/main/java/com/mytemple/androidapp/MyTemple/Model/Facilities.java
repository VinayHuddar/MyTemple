package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Facilities {
    Callback mCallback;

    public Facilities(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        Facility[] facilities;
        public int GetFacilitiesCount() { return facilities.length; }
        public Facility GetFacility (int facilitiesIdx) { return facilities[facilitiesIdx]; }

        public class Facility {
            int id;
            String image;
            String name;
            String quantity;
            String price;
            String description;
            String[] images;
            String date_added;

            public String GetImage () { return image; }
            public String GetName () { return name; }
            public String GetDescription () { return description; }
        }
    }

    int mRetryCnt = 0;
    public void FetchFacilitiesList(final int templeId) {
        MyPoojaApplication.GetAPIService().GetFacilitiesList(templeId,
                new retrofit.Callback<Facilities.Model>() {
                    @Override
                    public void success(Facilities.Model facilitiesList, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnFacilitiesListReceived(facilitiesList);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchFacilitiesList(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnFacilitiesListNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnFacilitiesListReceived(Model facilitiesList);
        public void OnFacilitiesListNotReceived(RetrofitError error);
    }
}
