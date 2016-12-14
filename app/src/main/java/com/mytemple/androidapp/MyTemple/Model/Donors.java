package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Donors {
    Callback mCallback;

    public Donors(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        DonorsGroup[] donor_groups;

        class DonorsGroup {
            int id;
            String image;
            String name;
            int donor_count;
            Donor[] donors;
        }

        class Donor {
            int id;
            String image;
            String name;
            String description;
            String donation_date;
            String date_added;
        }

        public String GetName (int groupIdx) { return donor_groups[groupIdx].name; }
        public int GetDonorsCount(int groupIdx) { return donor_groups[groupIdx].donor_count; }

        public int GetDonorId (int groupIdx, int DonorIdx) { return donor_groups[groupIdx].donors[DonorIdx].id; }
        public String GetDonorImage (int groupIdx, int DonorIdx) { return donor_groups[groupIdx].donors[DonorIdx].image; }
        public String GetDonorName (int groupIdx, int DonorIdx) { return donor_groups[groupIdx].donors[DonorIdx].name; }
        public String GetDonorDescription (int groupIdx, int DonorIdx) { return donor_groups[groupIdx].donors[DonorIdx].description; }
    }

    int mRetryCnt = 0;
    public void FetchDonorsList(final int templeId) {
        MyPoojaApplication.GetAPIService().GetDonors(templeId,
                new retrofit.Callback<Donors.Model>() {
                    @Override
                    public void success(Donors.Model donors, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnDonorsReceived(donors);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchDonorsList(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnDonorsNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnDonorsReceived(Model model);
        public void OnDonorsNotReceived(RetrofitError error);
    }
}
