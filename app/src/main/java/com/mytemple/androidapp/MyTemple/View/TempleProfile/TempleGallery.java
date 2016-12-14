package com.mytemple.androidapp.MyTemple.View.TempleProfile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.MyTemple.Model.GalleryImages;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;

/**
 * Created by vinayhuddar on 13/10/15.
 */
class TempleGallery implements TempleProfileFragmentController.ItemClickEventHandler, GalleryImages.Callback {
    private static final int DEFAULT_GALLERY_IDX = 0;

    Dialog mGalleryGridDialog;
    Activity mActivity;
    int mTempleId;

    public TempleGallery (Activity activity, int templeId) {
        mActivity = activity;
        mTempleId = templeId;
    }

    public void HandleEvent() {
        mGalleryGridDialog = new Dialog(mActivity);
        mGalleryGridDialog.setContentView(R.layout.dialog_layout_gallery);
        mGalleryGridDialog.setTitle(Html.fromHtml("<font color='#E93200'>".concat(mActivity.getResources().getString(R.string.gallery)).concat("</font>")));
        mGalleryGridDialog.setCancelable(true);
        mGalleryGridDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_white_box));

        mGalleryGridDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        mGalleryGridDialog.findViewById(R.id.GalleryGrid).setVisibility(View.INVISIBLE);

        mGalleryGridDialog.show();

        (new GalleryImages(this)).FetchGalleryImages(mTempleId);
    }

    public void OnGalleryImagesReceived (GalleryImages.Model model) {
        mGalleryGridDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        mGalleryGridDialog.findViewById(R.id.GalleryGrid).setVisibility(View.VISIBLE);

        GridView dataGrid = (GridView) mGalleryGridDialog.findViewById(R.id.GalleryGrid);
        dataGrid.setAdapter(new GalleryImagesGridAdapter(model));
    }

    public void OnGalleryImagesNotReceived(RetrofitError error) {
    }


    public class GalleryImagesGridAdapter extends BaseAdapter implements ImageDownloader.Callback {
        GalleryImages.Model mGalleryImages;
        ImageDownloader mImageDownloader = new ImageDownloader(this);

        public GalleryImagesGridAdapter (GalleryImages.Model model) {
            mGalleryImages = model;
        }
        public int getCount() {
            return mGalleryImages.GetImageCount(DEFAULT_GALLERY_IDX);
        }

        public GalleryImages.Model.Image getItem(int position) {
            return mGalleryImages.GetImage(DEFAULT_GALLERY_IDX, position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Context mContext = mParentActivity;
            View gridItem;
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                gridItem = inflater.inflate(R.layout.layout_gallery_item, null);
            } else {
                gridItem = (View) convertView;
            }

            final GalleryImages.Model.Image currImage = mGalleryImages.GetImage(DEFAULT_GALLERY_IDX, position);
            ImageView imageView = (ImageView) gridItem.findViewById(R.id.Image);
            mImageDownloader.download(currImage.GetImageUrl(), imageView);
            //image.setImageDrawable(mActivity.getResources().getDrawable(DemoContent_MarutiMandir.GalleryImages[position]));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog eventDialog = new Dialog(mActivity);
                    eventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    eventDialog.setContentView(R.layout.dialog_layout_gallery_image);
                    eventDialog.setCancelable(true);
                    eventDialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.round_corner_transparent_box));

                    eventDialog.findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
                    ImageView imageView = (ImageView) eventDialog.findViewById(R.id.ImageEnlarged);
                    //imageView.setVisibility(View.INVISIBLE);

                    eventDialog.show();

                    mImageDownloader.download(currImage.GetImageUrl(), imageView);
                    //image.setImageDrawable(mActivity.getResources().getDrawable(DemoContent_MarutiMandir.GalleryImagesBig[position]));

                    eventDialog.findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
                }
            });

            return gridItem;
        }

        public void OnImageReceived () {
        }
    }
}

