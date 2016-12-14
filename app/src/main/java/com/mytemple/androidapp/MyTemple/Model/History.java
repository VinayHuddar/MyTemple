package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class History {
    Callback mCallback;

    public History(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        HistoryGroup[] history_groups;

        class HistoryGroup {
            int id;
            String image;
            String name;
            int history_count;
            Milestone[] histories;
        }

        class Milestone {
            int id;
            String image;
            String name;
            String description;
            String date_added;
        }

        public String GetName (int groupIdx) { return history_groups[groupIdx].name; }
        public int GetMilestoneCount (int groupIdx) { return history_groups[groupIdx].histories.length; }

        public int GetMilestoneId (int groupIdx, int milestoneIdx) { return history_groups[groupIdx].histories[milestoneIdx].id; }
        public String GetMilestoneImage (int groupIdx, int milestoneIdx) { return history_groups[groupIdx].histories[milestoneIdx].image; }
        public String GetMilestoneName (int groupIdx, int milestoneIdx) { return history_groups[groupIdx].histories[milestoneIdx].name; }
        public String GetMilestoneDescription (int groupIdx, int milestoneIdx) { return history_groups[groupIdx].histories[milestoneIdx].description; }
    }

    int mRetryCnt = 0;
    public void FetchTempleHistory(final int templeId) {
        MyPoojaApplication.GetAPIService().GetTempleHistory(templeId,
                new retrofit.Callback<History.Model>() {
                    @Override
                    public void success(History.Model milestones, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnTempleHistoryReceived(milestones);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchTempleHistory(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnTempleHistoryNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnTempleHistoryReceived(Model model);
        public void OnTempleHistoryNotReceived(RetrofitError error);
    }
}
