package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class TempleGallery {
    Callback mCallback;

    public TempleGallery (Callback callback) {
        mCallback = callback;
    }

    public class Model {
        Gallery[] galleries;

        class Gallery {
            int id;
            String image;
            String name;
            int image_count;
            String date_added;
        }

        public int GetGalleryCount () { return galleries.length; }
        public int GetId (int galleryId) { return galleries[galleryId].id; }
        public String GetImage (int galleryId) { return galleries[galleryId].image; }
        public String GetName (int galleryId) { return galleries[galleryId].name; }
        public int GetImgCount (int galleryId) { return galleries[galleryId].image_count; }
        public String GetDateAdded (int galleryId) { return galleries[galleryId].date_added; }
    }

    int mRetryCnt = 0;
    public void GetTempleGallery(final int partnerId) {
        MyPoojaApplication.GetAPIService().GetGalleries(partnerId,
                new retrofit.Callback<TempleGallery.Model>() {
                    @Override
                    public void success(TempleGallery.Model galleries, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnTempleGalleryReceived(galleries);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            GetTempleGallery(partnerId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnTempleGalleryNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnTempleGalleryReceived(Model galleries);
        public void OnTempleGalleryNotReceived(RetrofitError error);
    }
}
