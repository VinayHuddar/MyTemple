package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Temple {
    Callback mCallback;

    public Temple(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        TempleInfo partner;
        public TempleInfo GetTempleInfo () { return partner; }

        public class TempleInfo {
            int partner_id;
            String image;
            String name;
            String email;
            String telephone;
            String mobile;
            String deity_name;
            String twitter;
            String facebook;
            String theme;
            String website;
            String banner;
            double rating;
            int viewed;
            String description;
            Address address;
            String[] attribute_groups;
            String[] tags;

            public String GetName () { return name; }
            public String GetDescription () { return description; }
            public String GetEmail () { return email; }
            public String GetTelephone () { return telephone; }
            public String GetMobile () { return mobile; }
            public String GetTwitter () { return twitter; }
            public String GetFacebook () { return facebook; }
            public String GetWebsite () { return website; }
            public Address GetAddress () { return address; }
        }

        public class Address {
            String address_1;
            String address_2;
            String area;
            String city;
            String postcode;

            public String GetAddress_1 () { return address_1; }
            public String GetAddress_2 () { return address_2; }
            public String GetArea () { return area; }
            public String GetCity () { return city; }
            public String GetPostcode () { return postcode; }
        }
    }

    int mRetryCnt = 0;
    public void FetchTempleInfo(final int templeId) {
        MyPoojaApplication.GetAPIService().GetTempleInfo(templeId,
                new retrofit.Callback<Temple.Model>() {
                    @Override
                    public void success(Temple.Model templeInfo, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnTempleInfoReceived(templeInfo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchTempleInfo(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnTempleInfoNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnTempleInfoReceived(Model templeInfo);
        public void OnTempleInfoNotReceived(RetrofitError error);
    }
}
