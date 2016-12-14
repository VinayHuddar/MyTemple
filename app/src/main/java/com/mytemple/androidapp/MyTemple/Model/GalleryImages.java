package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class GalleryImages {
    Callback mCallback;

    public GalleryImages(Callback callback) {
        mCallback = callback;
    }

    public class Model {
        Gallery[] galleries;

        class Gallery {
            int id;
            String image;
            String name;
            int image_count;
            Image[] images;
        }

        public class Image {
            int id;
            String image;
            String name;
            String date_added;

            public int GetImageId () { return id; }
            public String GetImageUrl () { return image; }
            public String GetImageName () { return name; }
        }

        public int GetGalleryCount () { return galleries.length; }
        public int GetId (int galleryIdx) { return galleries[galleryIdx].id; }
        public String GetGalleryImage (int galleryIdx) { return galleries[galleryIdx].image; }
        public String GetName (int galleryIdx) { return galleries[galleryIdx].name; }
        public int GetImageCount (int galleryIdx) { return galleries[galleryIdx].image_count; }

        public Image GetImage (int galleryIdx, int imageIdx) { return galleries[galleryIdx].images[imageIdx]; }
    }

    int mRetryCnt = 0;
    public void FetchGalleryImages(final int partnerId) {
        MyPoojaApplication.GetAPIService().GetGalleryImages(partnerId,
                new retrofit.Callback<GalleryImages.Model>() {
                    @Override
                    public void success(GalleryImages.Model galleries, Response response) {
                        mRetryCnt = 0;
                        mCallback.OnGalleryImagesReceived(galleries);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                            mRetryCnt++;
                            FetchGalleryImages(partnerId);
                        } else {
                            mRetryCnt = 0;
                            mCallback.OnGalleryImagesNotReceived(error);
                        }
                    }
                });
    }

    public interface Callback {
        public void OnGalleryImagesReceived(Model galleries);
        public void OnGalleryImagesNotReceived(RetrofitError error);
    }
}
