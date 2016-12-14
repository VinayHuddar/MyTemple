package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mytemple.androidapp.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    public static final int EVENT_TYPE_ABHISHEKA = 1;
    public static final int EVENT_TYPE_POOJA = 2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Activity mActivity;
    Context mContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TempleProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public EventsFragment() {
        // Required empty public constructor
    }

    class EventColor {
        int headingColor;
        int bodyColor;

        public EventColor(int _headingColor, int _bodyColor) {
            headingColor = _headingColor;
            bodyColor = _bodyColor;
        }
    }
    HashMap<Integer, EventColor> mEventColors = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = mActivity = getActivity();
    }

    DemoContent_MarutiMandir.SpecialEvent[] mEventList = DemoContent_MarutiMandir.SpecialEventData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_special_events, container, false);

        mEventColors.put(EVENT_TYPE_ABHISHEKA, new EventColor(R.color.event_type_color_abhisheka_heading, R.color.event_type_color_abhisheka_body));
        mEventColors.put(EVENT_TYPE_POOJA, new EventColor(R.color.event_type_color_homa_heading, R.color.event_type_color_homa_body));

        ListView eventList = (ListView)rootView.findViewById(R.id.EventList);
        eventList.setAdapter(new EventListAdapter());

        rootView.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

        return rootView;

    }

    class EventListAdapter extends BaseAdapter {
        public int getCount() {
            return mEventList.length;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final DemoContent_MarutiMandir.SpecialEvent currEvent = mEventList[position];

            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View eventItem;
            if (convertView == null) {
                eventItem = inflater.inflate(R.layout.layout_event_list_item, null);
            } else {
                eventItem = (View) convertView;
            }

            final int bodyColor = mActivity.getResources().getColor(mEventColors.get(currEvent.eventType).bodyColor);
            eventItem.findViewById(R.id.EventLayout).setBackgroundColor(bodyColor);

            TextView title = (TextView)eventItem.findViewById(R.id.EventTitle);
            title.setText(currEvent.title);

            TextView date = (TextView)eventItem.findViewById(R.id.EventDate);
            date.setText(currEvent.date);

            eventItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog eventDialog = new Dialog(mActivity);
                    eventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    eventDialog.setContentView(R.layout.dialog_layout_special_event);
                    eventDialog.setCancelable(true);
                    eventDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

                    eventDialog.show();

                    TextView eventTitle = (TextView)eventDialog.findViewById(R.id.EventTitle);
                    eventTitle.setText(currEvent.title);
                    int headingColor = mActivity.getResources().getColor(mEventColors.get(currEvent.eventType).headingColor);
                    eventTitle.setBackgroundColor(headingColor);

                    TextView eventDate = (TextView)eventDialog.findViewById(R.id.EventDate);
                    eventDate.setText(currEvent.date);
                    eventDate.setBackgroundColor(headingColor);

                    TextView eventDescription = (TextView)eventDialog.findViewById(R.id.EventDescription);
                    eventDescription.setText(Html.fromHtml(mActivity.getResources().getString(currEvent.description)));
                    eventDescription.setBackgroundColor(bodyColor);

                    eventDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

                }
            });

            return eventItem;
        }

        public long getItemId(int position) {
            return position;
        }

        public DemoContent_MarutiMandir.SpecialEvent getItem(int position) {
            return mEventList[position];
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOccassionsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onOccassionsFragmentInteraction(Uri uri);
    }

}
