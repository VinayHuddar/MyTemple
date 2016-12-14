package com.mytemple.androidapp.MyTemple.View;

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

import com.mytemple.androidapp.R;

public class OnlineServicesFragment extends Fragment {
    OnlineServicesFragmentController mOnlineServicesFragmentController;
    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online_services, container, false);

        mOnlineServicesFragmentController = new OnlineServicesFragmentController(getActivity());

        GridView servicesList = (GridView)rootView.findViewById(R.id.OnlineServicesGrid);
        servicesList.setAdapter(new OnlineServicesAdapter());

        return rootView;
    }

    class OnlineServicesAdapter extends BaseAdapter {
        public int getCount() {
            return mOnlineServicesFragmentController.GetServiceItemCount();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View serviceItem;
            if (convertView == null) {
                serviceItem = inflater.inflate(R.layout.layout_online_services_item, null);
            } else {
                serviceItem = (View) convertView;
            }

            TextView fieldNameTV = (TextView) serviceItem.findViewById(R.id.Title);
            fieldNameTV.setText(mActivity.getResources().getString(mOnlineServicesFragmentController.GetServiceItemName(position)));

            ImageView imageView = (ImageView) serviceItem.findViewById(R.id.Icon);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(mOnlineServicesFragmentController.GetServiceItemIcon(position));

            serviceItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnlineServicesFragmentController.HandleClickEvent(position);
                }
            });

            return serviceItem;
        }

        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {
            return null;
        }
    }

    private OnFragmentInteractionListener mListener;

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onActivitiesFragmentInteraction(uri);
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
        public void onActivitiesFragmentInteraction(Uri uri);
    }

}
