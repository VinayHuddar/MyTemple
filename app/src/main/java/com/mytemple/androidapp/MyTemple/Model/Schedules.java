package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Schedules {
    Callback mCallback;

    public Schedules(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        ScheduleGroups[] schedule_groups;

        class ScheduleGroups {
            int id;
            String name;
            String image;
            int schedule_count;
            Schedule[] schedules;
        }

        class Schedule {
            int id;
            String image;
            String name;
            String from_time;
            String to_time;
            String description;
            String date_added;
        }

        public String GetName (int groupIdx) { return schedule_groups[groupIdx].name; }

        public int GetScheduleCount (int groupIdx) { return schedule_groups[groupIdx].schedules.length; }
        public int GetScheduleId (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].id; }
        public String GetScheduleImage (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].image; }
        public String GetScheduleName (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].name; }
        public String GetScheduleFromTime (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].from_time; }
        public String GetScheduleToTime (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].to_time; }
        public String GetScheduleDescription (int groupIdx, int scheduleIdx) { return schedule_groups[groupIdx].schedules[scheduleIdx].description; }
    }

    int mRetryCnt = 0;
    public void FetchSchedules(final int templeId) {
        MyPoojaApplication.GetAPIService().GetTempleSchedules(templeId,
                new retrofit.Callback<Schedules.Model>() {
                    @Override
                    public void success(Schedules.Model schedules, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnTempleSchedulesReceived(schedules);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchSchedules(templeId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnTempleSchedulesNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnTempleSchedulesReceived(Model galleries);
        public void OnTempleSchedulesNotReceived(RetrofitError error);
    }
}
