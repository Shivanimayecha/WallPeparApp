package com.hd.nature.ghkwallpaper.comman;

import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class GlobalApplication extends MultiDexApplication {



    public PublisherInterstitialAd mInterstitialAd;
    public PublisherAdRequest ins_adRequest;


    public static SharePrefs sharePrefs;
    private static final String TAG = "Application";

    private static GlobalApplication appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        MultiDex.install(this);
        //TODO to solve camera issue

        sharePrefs = new SharePrefs(getApplicationContext());

        LoadAds();

    }

    public void LoadAds() {

        try {
            Log.e("WallPeparApplication", "LoadAds Called");
            mInterstitialAd = new PublisherInterstitialAd(this);

            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

            ins_adRequest = new PublisherAdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("EA965DE183B804F71E5E6D353E6607DE")
                    .addTestDevice("5CE992DB43E8F2B50F7D2201A724526D")
                    .addTestDevice("6E5543AE954EAD6702405BFCCC34C9A2")
                    .addTestDevice("28373E4CC308EDBD5C5D39795CD4956A") //samsung
                    .addTestDevice("79E8DED973BDF7477739501E228D88E1") //samdung max|
                    .addTestDevice("879B80CCA184B787248F4CD69D349258") //vivo
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        try {
            if (mInterstitialAd.isLoaded()) {
                Log.e("WallPeparApplication", "requestNewInterstitial isLoaded Called");
                mInterstitialAd.show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoaded() {

        try {
            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized GlobalApplication getInstance() {
        return appInstance;
    }

}
