package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Pandits {
    Callback mCallback;

    public Pandits(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        Pandit[] pandits;
        public int GetPanditCount() { return pandits.length; }
        public Pandit GetPandit (int panditIdx) { return pandits[panditIdx]; }

        public class Pandit {
            int id;
            String image;
            String name;
            String description;
            String date_added;

            public String GetImage () { return image; }
            public String GetName () { return name; }
            public String GetDescription () { return description; }
        }

    }

    int mRetryCnt = 0;
    public void FetchPanditList(final int templeId) {
        MyPoojaApplication.GetAPIService().GetPanditsList(templeId,
                new retrofit.Callback<Pandits.Model>() {
                    @Override
                    public void success(Pandits.Model panditList, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnPanditListReceived(panditList);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchPanditList(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnPanditListNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnPanditListReceived(Model panditList);
        public void OnPanditListNotReceived(RetrofitError error);
    }
}
