package com.mytemple.androidapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;

import com.mytemple.androidapp.Common.APIService;
import com.mytemple.androidapp.Common.ApplicationSettings;
import com.mytemple.androidapp.Common.Model.AuthenticationData;
import com.mytemple.androidapp.Common.CommonDefinitions;
import com.mytemple.androidapp.Common.Model.TokenResponse;
import com.mytemple.androidapp.Common.Model.UserAccountData;
import com.mytemple.androidapp.Common.View.NavDrawerItemList;
import com.mytemple.androidapp.MyTemple.View.TempleListingScreen;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Vinay on 07-06-2015.
 */
public class MyPoojaApplication extends Application implements ApplicationSettings.Callback {
    private static final String PREFERENCES_FILE = "my_app_settings"; //TODO: change this to your file

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rHESkVmE35i89v2IhppL3IkA7";
    private static final String TWITTER_SECRET = "z7Hh05m6EQRpDL4jlsME16PEHCxMuyzh0iUy1lXEBob1OYuunN";

    private static AuthenticationData mAuthenticationData = null;
    private RestAdapter mRestAdapter;
    private static APIService mAPIService;
    private String Url = "http://ec2-54-173-60-3.compute-1.amazonaws.com/Nukkad/api/v1";

    private Context mApplCtxt;

    public static APIService GetAPIService () {
        return mAPIService;
    }

    public void onCreate() {
        //PsiMethod:onCreateFabric.with(this, new Crashlytics());
        /*try {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new Digits());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        mApplCtxt = this;
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Url)
                .build();
        mAPIService = mRestAdapter.create(APIService.class);

        mAuthenticationData = AuthenticationData.GetInstance();

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                                               @Override
                                               public void onActivityCreated(Activity activity,
                                                                             Bundle savedInstanceState) {

                                                   // new activity created; force its orientation to portrait
                                                   activity.setRequestedOrientation(
                                                           ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                               }

                                               @Override
                                               public void onActivityDestroyed(Activity activity) {
                                               }

                                               @Override
                                               public void onActivityResumed(Activity activity) {
                                               }

                                               @Override
                                               public void onActivityPaused(Activity activity) {
                                               }

                                               @Override
                                               public void onActivitySaveInstanceState(Activity activity,
                                                                                       Bundle savedInstanceState) {
                                               }

                                               @Override
                                               public void onActivityStarted(Activity activity) {
                                               }

                                               @Override
                                               public void onActivityStopped(Activity activity) {

                                               }
                                           }
        );

        //InitAppSettingsDemo();

        /*if (UserAccountData.GetInstance().GetAccountData(this) != null) {
            mAuthenticationData.SetAuthenticationToken(UserAccountData.GetInstance().GetAuthenticationToken(this));
            InitAppSettings();
        }
        else*/
            FetchAuthKeyAndInitApp();
    }

    private int fetchAuthKeyRetryCnt = 0;
    void FetchAuthKeyAndInitApp() {
        //AuthenticationData authData = AuthenticationData.GetInstance();
        String strAuthHdr = "Basic ZGV2aWNlY2xpZW50MzAxMjpyYWplc2g5OTg2MDE4NjQzYW5pdGhhOTk4NjAwNjA2Mw=="; // Base64 encoded string for "deviceclient3012:rajesh9986018643anitha9986006063"
        mAPIService.GetAuthenticationToken("application/json", strAuthHdr, "client_credentials", "", new Callback<TokenResponse>() {
            @Override
            public void success(TokenResponse tokenResponse, Response response) {
                mAuthenticationData.SetAuthenticationToken(tokenResponse.GetAccessToken());

                InitAppSettings();
            }

            @Override
            public void failure(RetrofitError error) {
                fetchAuthKeyRetryCnt++;
                if (fetchAuthKeyRetryCnt < CommonDefinitions.RETRY_COUNT)
                    FetchAuthKeyAndInitApp();
            }
        });
    }

    void InitAppSettings() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Authorization", "Bearer ".concat(mAuthenticationData.GetAuthenticationToken())); //mTokenResponse.GetTokenType()
                //request.addHeader("Accept-Language", "kn_IN"); //mTokenResponse.GetTokenType()
            }
        };

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Url)
                .setClient(new OkClient(new OkHttpClient()))
                .setRequestInterceptor(requestInterceptor)
                .build();
        mAPIService = mRestAdapter.create(APIService.class);


        ApplicationSettings applicationSettings = ApplicationSettings.GetInstance();
        applicationSettings.Init(mApplCtxt, this);
        applicationSettings.FetchAppSettings();
    }

    void InitAppSettingsDemo() {
        ApplicationSettings applicationSettings = ApplicationSettings.GetInstance();
        applicationSettings.Init(mApplCtxt, this);
        applicationSettings.FetchAppSettings();
    }

    Typeface mKannadaFont = null;
    public void OnApplicationSettingsInitialized () {
        NavDrawerItemList.GetInstance().PrepareNavDrawerLists(getApplicationContext());

        /*mKannadaFont = Typeface.createFromAsset(getAssets(), "fonts/akshar.ttf");
        Locale locale = new Locale("kn");
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/

        //Intent intent = new Intent(getApplicationContext(), MainScreen_PujariSamagriSeva.class);
        Intent intent = new Intent(getApplicationContext(), TempleListingScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public Typeface GetKannadaTypeface () {
        return mKannadaFont;
    }
}
