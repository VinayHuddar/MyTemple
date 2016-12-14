package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mytemple.androidapp.Common.IntentActionStrings;
import com.mytemple.androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TempleProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TempleProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempleProfileFragment extends Fragment {
    Activity mActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mTempleId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TempleProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TempleProfileFragment newInstance(String param1, String param2) {
        TempleProfileFragment fragment = new TempleProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TempleProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTempleId = getArguments().getInt(IntentActionStrings.TEMPLE_ID);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    TempleProfileFragmentController mTempleProfileFragmentController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_temple_profile, container, false);

        mTempleProfileFragmentController = new TempleProfileFragmentController(getActivity(), mTempleId);

        GridView dataGrid = (GridView)rootView.findViewById(R.id.TempleDataGrid);
        dataGrid.setAdapter(new TempleProfileGridAdapter());

        return rootView;
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

    public class TempleProfileGridAdapter extends BaseAdapter {
        public int getCount() {
            return mTempleProfileFragmentController.GetProfileItemCount();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            View gridItem;
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                gridItem = inflater.inflate(R.layout.layout_temple_profile_grid_item, null);
            } else {
                gridItem = (View) convertView;
            }

            TextView fieldNameTV = (TextView) gridItem.findViewById(R.id.Title);
            fieldNameTV.setText(mActivity.getResources().getString(mTempleProfileFragmentController.GetProfileItemName(position)));

            ImageView imageView = (ImageView) gridItem.findViewById(R.id.Icon);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(mTempleProfileFragmentController.GetProfileItemIcon(position));

            gridItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTempleProfileFragmentController.HandleClickEvent(position);
                }
            });

            return gridItem;
        }
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
        public void onProfileFragmentInteraction(Uri uri);
    }

}
