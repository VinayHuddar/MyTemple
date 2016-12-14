package com.mytemple.androidapp.MyTemple.Model;

import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.MyPoojaApplication;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vinayhuddar on 05/10/15.
 */
public class Categories {
    static Callback mCallback;

    Categories (Callback callback) {
        mCallback = callback;
    }

    static Categories mCategories = null;
    public static Categories GetCategories (Callback callback) {
        if (mCategories == null) {
            mCategories = new Categories(callback);

            FetchCategories();
        }

        return mCategories;
    }

    public class Model {
        Category[] categories;

        public class Category {
            int category_id;
            String name;
            String image;
            String description;
            int partner_count;
            Category[] categories;

            public int GetCategoryId () { return category_id; }
            public String GetCategoryName () { return name; }
            public String GetCategoryImage () { return image; }
            public String GetDescription () { return description; }
            public int GetPartnerCount () { return partner_count; }

            public int GetSubCatCount() { return categories.length; }
            public Category GetCategory (int idx) { return categories[idx-1]; }
        }

        public int GetCategoryCount () { return categories.length; }
        public Category GetCategory (int idx) { return categories[idx-1]; }
    }

    static int mRetryCnt = 0;
    public static void FetchCategories() {
        MyPoojaApplication.GetAPIService().GetCategories(new retrofit.Callback<Categories.Model>() {
            @Override
            public void success(Categories.Model galleries, Response response) {
                mRetryCnt = 0;
                mCallback.OnCategoriesReceived(galleries);
            }

            @Override
            public void failure(RetrofitError error) {
                if (mRetryCnt < CommonDefinitions.RETRY_COUNT) {
                    mRetryCnt++;
                    FetchCategories();
                } else {
                    mRetryCnt = 0;
                    mCallback.OnCategoriesNotReceived(error);
                }
            }
        });
    }

    public interface Callback {
        public void OnCategoriesReceived (Model model);
        public void OnCategoriesNotReceived (RetrofitError error);
    }
}
