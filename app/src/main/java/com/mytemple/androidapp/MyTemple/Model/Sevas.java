package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Sevas {
    Callback mCallback;

    public Sevas(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        SevaGroups[] service_groups;

        class SevaGroups {
            int id;
            String name;
            String image;
            int service_count;
            Seva[] services;
        }

        public class Seva {
            int id;
            String image;
            String name;
            int quantity;
            double price;
            double special;
            double duration;
            int points;
            String start_date;
            String end_date;
            String date_available;
            String description;
            String item_description;
            String images;
            String date_added;
        }

        public String GetName (int groupIdx) { return service_groups[groupIdx].name; }

        public Seva GetSeva (int groupIdx, int sevaIdx) { return service_groups[groupIdx].services[sevaIdx]; }
        public int GetSevaCount (int groupIdx) { return service_groups[groupIdx].services.length; }
        public int GetSevaId (int groupIdx, int sevaIdx) { return service_groups[groupIdx].services[sevaIdx].id; }
        public String GetSevaName (int groupIdx, int sevaIdx) { return service_groups[groupIdx].services[sevaIdx].name; }
        public String GetSevaDescription (int groupIdx, int sevaIdx) { return service_groups[groupIdx].services[sevaIdx].description; }
        public double GetSevaPrice (int groupIdx, int sevaIdx) { return service_groups[groupIdx].services[sevaIdx].price; }
    }

    int mRetryCnt = 0;
    public void FetchSevas(final int templeId) {
        MyPoojaApplication.GetAPIService().GetTempleSevaList(templeId,
                new retrofit.Callback<Sevas.Model>() {
                    @Override
                    public void success(Sevas.Model schedules, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnSevaListReceived(schedules);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchSevas(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnSevaListNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnSevaListReceived(Model sevas);
        public void OnSevaListNotReceived(RetrofitError error);
    }
}
