package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Temples {
    Callback mCallback;

    public Temples(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        int category_id;
        String name;
        String image;
        String description;
        Temple[] partners;

        public int GetTempleCount () { return partners.length; }
        public Temple GetTemple(int idx) { return partners[idx]; }
        public Temple[] GetAllTemples () { return partners; }
    }

    public class Temple {
        int id;
        String name;
        String image;
        double rating;
        String description;
        Address address;
        String[] attribute_groups;
        String href;

        public int GetTempleId() { return id; }
        public String GetName () { return name; }
        public String GetImage () { return image; }
        public double GetRating () { return rating; }
        public String GetDescription () { return description; }
        public String GetArea () { return address.area; }
        public String GetCity () { return address.city; }
        public String[] GetAttributes () { return attribute_groups; }

        class Address {
            String address_1;
            String address_2;
            String area;
            String city;
            String postcode;
        }
    }

    int mRetryCnt = 0;
    public void FetchTemples(final int categoryId) {
        MyPoojaApplication.GetAPIService().GetTemples(categoryId, new retrofit.Callback<Temples.Model>() {
            @Override
            public void success(Temples.Model partners, Response response) {
                mRetryCnt = 0;
                mCallback.OnTemplesReceived(categoryId, partners);
            }

            @Override
            public void failure(RetrofitError error) {
                if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                    mRetryCnt++;
                    FetchTemples(categoryId);
                } else {
                    mRetryCnt = 0;
                    mCallback.OnTemplesNotReceived(error);
                }
            }
        });
    }

    public interface Callback {
        public void OnTemplesReceived(int catId, Model model);
        public void OnTemplesNotReceived(RetrofitError error);
    }
}
