package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Trusts {
    Callback mCallback;

    public Trusts(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        Trust[] trusts;

        class Trust {
            int id;
            String name;
            String image;
            int member_count;
            //Trustee[] members;
        }

        public int GetTrustGroupCount () { return trusts.length; }
        public String GetTrustGroupImage (int groupIdx) { return trusts[groupIdx].image; }
        public String GetTrustGroupDescription (int groupIdx) { return "13-Oct-2015"; }
        public String GetTrustName (int groupIdx) { return trusts[groupIdx].name; }

        /*public class Trustee {
            int id;
            String image;
            String name;
            String designation;
            String description;
            String join_date;
            String date_added;

            public int GetId () { return id; }
            public String GetImage () { return image; }
            public String GetName  () { return name; }
            public String GetDesignation () { return designation; }
            public String GetDescription () { return description; }
        }

        public int GetMemberCount (int groupIdx) { return trusts[groupIdx].members.length; }
        public Trustee GetTrustee (int groupIdx, int trusteeIdx) { return trusts[groupIdx].members[trusteeIdx]; }

        public int GetMemberId (int groupIdx, int scheduleIdx) { return trusts[groupIdx].members[scheduleIdx].id; }
        public String GetMemberImage (int groupIdx, int scheduleIdx) { return trusts[groupIdx].members[scheduleIdx].image; }
        public String GetMemberName (int groupIdx, int scheduleIdx) { return trusts[groupIdx].members[scheduleIdx].name; }
        public String GetMemberDesignation (int groupIdx, int scheduleIdx) { return trusts[groupIdx].members[scheduleIdx].designation; }
        public String GetMemberDescription (int groupIdx, int scheduleIdx) { return trusts[groupIdx].members[scheduleIdx].description; }*/
    }

    int mRetryCnt = 0;
    public void FetchTrusts(final int templeId) {
        MyPoojaApplication.GetAPIService().GetTempleTrusts(templeId,
                new retrofit.Callback<Trusts.Model>() {
                    @Override
                    public void success(Trusts.Model members, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnTempleTrustsReceived(members);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchTrusts(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnTempleTrustsNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnTempleTrustsReceived(Model trusts);
        public void OnTempleTrustsNotReceived(RetrofitError error);
    }
}
