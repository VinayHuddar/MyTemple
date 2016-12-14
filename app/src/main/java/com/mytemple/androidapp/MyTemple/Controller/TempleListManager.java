package com.mytemple.androidapp.MyTemple.Controller;

import com.mytemple.androidapp.MyTemple.Model.Temples;

import java.util.HashMap;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 06/10/15.
 */
public class TempleListManager implements Temples.Callback{
    static TempleListManager instance = null;
    public static TempleListManager GetInstance () {
        if (instance == null)
            instance = new TempleListManager();

        return instance;
    }

    static HashMap<Integer, Temples.Model> mPartnerListHash;
    static Temples mTemples;

    TempleListManager() {
        mPartnerListHash = new HashMap<>();
        mTemples = new Temples(this);
    }

    Callback mCallback = null;
    public void SetCallback (Callback callback) {
        mCallback = callback;
    }

    public void GetTemples(int catId) {
        Temples.Model partnerList = mPartnerListHash.get(catId);
        if (partnerList != null) {
            mCallback.OnTemplesReceived(partnerList);
        } else {
            mTemples.FetchTemples(catId);
        }
    }

    public void OnTemplesReceived(int catId, Temples.Model model) {
        mPartnerListHash.put(catId, model);
        mCallback.OnTemplesReceived(model);
    }

    public void OnTemplesNotReceived(RetrofitError error) {
        mCallback.OnTemplesNotReceived(error);
    }

    public interface Callback {
        public void OnTemplesReceived(Temples.Model model);
        public void OnTemplesNotReceived(RetrofitError error);
    }
}
