package com.mytemple.androidapp.Common;

import com.mytemple.androidapp.Common.Model.AccountData;
import com.mytemple.androidapp.Common.Model.AppSettingsData;
import com.mytemple.androidapp.Common.Model.TokenResponse;
import com.mytemple.androidapp.MyTemple.Model.Categories;
import com.mytemple.androidapp.MyTemple.Model.Donors;
import com.mytemple.androidapp.MyTemple.Model.Events;
import com.mytemple.androidapp.MyTemple.Model.Facilities;
import com.mytemple.androidapp.MyTemple.Model.GalleryImages;
import com.mytemple.androidapp.MyTemple.Model.History;
import com.mytemple.androidapp.MyTemple.Model.Pandits;
import com.mytemple.androidapp.MyTemple.Model.Schedules;
import com.mytemple.androidapp.MyTemple.Model.Sevas;
import com.mytemple.androidapp.MyTemple.Model.Temple;
import com.mytemple.androidapp.MyTemple.Model.Temples;
import com.mytemple.androidapp.MyTemple.Model.TempleGallery;
import com.mytemple.androidapp.MyTemple.Model.Trusts;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Vinay on 08-06-2015.
 */
public interface APIService {
    @POST("/oauth2/token")
    void GetAuthenticationToken(@Header("Accept") String accept, @Header("Authorization") String authHdr, @Query("grant_type") String grantType, @Body String emptyString,
                                Callback<TokenResponse> callback);

    @GET("/common/settings")
    void GetAppSettings(Callback<AppSettingsData> callback);

    /****************** Account APIs ******************/

    @FormUrlEncoded
    @POST("/account/login")
    void PostLogin (
            @Field("email") String email,
            @Field("password") String password,
            Callback<AccountData> callback);

    @GET("/account/logout")
    void GetLogout (Callback<String> callback);

    /******************* Temple Listing related APIs *****************/

    @GET("/partner/gallery/{partner_id}")
    void GetGalleries(@Path("partner_id") int partner_id, Callback<TempleGallery.Model> callback);

    @GET("/partner/image/{partner_id}")
    void GetGalleryImages(@Path("partner_id") int partner_id, Callback<GalleryImages.Model> callback);

    @GET("/partner/category")
    void GetCategories(Callback<Categories.Model> callback);

    @GET("/partner/category/{category_id}")
    void GetTemples(@Path("category_id") int category_id, Callback<Temples.Model> callback);

    @GET("/partner/event/{partner_id}")
    void GetEvents(@Path("partner_id") int partner_id, Callback<Events.Model> callback);

    @GET("/partner/partner/{partner_id}")
    void GetTempleInfo(@Path("partner_id") int partner_id, Callback<Temple.Model> callback);

    @GET("/partner/schedule/{partner_id}")
    void GetTempleSchedules(@Path("partner_id") int partner_id, Callback<Schedules.Model> callback);

    @GET("/partner/service/{partner_id}")
    void GetTempleSevaList(@Path("partner_id") int partner_id, Callback<Sevas.Model> callback);

    @GET("/partner/pandit/{partner_id}")
    void GetPanditsList(@Path("partner_id") int partner_id, Callback<Pandits.Model> callback);

    @GET("/partner/trust/{partner_id}")
    void GetTempleTrusts(@Path("partner_id") int partner_id, Callback<Trusts.Model> callback);

    @GET("/partner/history/{partner_id}")
    void GetTempleHistory(@Path("partner_id") int partner_id, Callback<History.Model> callback);

    @GET("/partner/donor/{partner_id}")
    void GetDonors(@Path("partner_id") int partner_id, Callback<Donors.Model> callback);

    @GET("/partner/facility/{partner_id}")
    void GetFacilitiesList(@Path("partner_id") int partner_id, Callback<Facilities.Model> callback);
}
