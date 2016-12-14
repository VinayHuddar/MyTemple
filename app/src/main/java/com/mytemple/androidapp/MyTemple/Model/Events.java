package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 06/10/15.
 */
public class Events {
    Callback mCallback;

    public Events(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        EventGroup[] event_groups;

        public int GetGroupCount() {
            return event_groups.length;
        }

        public EventGroup GetEventGroup (int groupId) {
            return event_groups[groupId];
        }

        public Event GetEvent (int groupId, int eventId) {
            return event_groups[groupId].events[eventId];
        }

        public int GetEventCount(int groupId) {
            return event_groups[groupId].event_count;
        }

        class EventGroup {
            int id;
            String image;
            String name;
            int event_count;
            Event[] events;

            public int GetId () { return id; }
            public String GetImage () { return image; }
            public int GetEventCount () { return event_count; }
        }
        public String GetGroupName (int groupIdx) { return event_groups[groupIdx].name; }

        public class Event {
            int id;
            String image;
            String name;
            String start_date;
            String end_date;
            String description;
            String date_added;
            String[] schedules;

            public int GetId () { return id; }
            public String GetImage  () { return image; }
            public String GetName  () { return name; }
            public String GetStartDate  () { return start_date; }
            public String GetEndDate () { return end_date; }
            public String GetDescription () { return description; }
            public String GetDateAdded () { return date_added; }
            public String[] GetSchedules () { return schedules; }
        }
    }

    int mRetryCnt = 0;
    public void FetchEvents (final int templeId) {
        MyPoojaApplication.GetAPIService().GetEvents(templeId, new retrofit.Callback<Model>() {
            @Override
            public void success(Model model, Response response) {
                mRetryCnt = 0;
                mCallback.OnEventsReceived(model);
            }

            @Override
            public void failure(RetrofitError error) {
                if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                    mRetryCnt++;
                    FetchEvents(templeId);
                } else {
                    mRetryCnt = 0;
                    mCallback.OnEventsNotReceived(error);
                }
            }
        });
    }

    public interface Callback {
        public void OnEventsReceived(Model model);
        public void OnEventsNotReceived(RetrofitError error);
    }
}
