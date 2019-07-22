package com.hd.nature.ghkwallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.adapter.WallPeparsAdapter;
import com.hd.nature.ghkwallpaper.comman.NetworkConnection;
import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;
import com.hd.nature.ghkwallpaper.retrofit.ApiService;
import com.hd.nature.ghkwallpaper.retrofit.RetroClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowWallPepar_Activity extends AppCompatActivity {


    String TAG = "TAG";
    String cat_id;
    int idd;
    ViewPager viewPager;
    Activity activity;
    WallPeparsAdapter wallPeparsAdapter;
    ArrayList<WallPepars_Model> wallPepars_modelArrayList = new ArrayList<>();
    Button save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private AdView adView;
    AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpepar_);
        activity = ShowWallPepar_Activity.this;


        findViews();

        initviews();

    }

    private void findViews() {
        viewPager = findViewById(R.id.viewPager);
        save = findViewById(R.id.btn_save);
        adView = findViewById(R.id.ad_View);
    }

    private void initviews() {

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        cat_id = getIntent().getStringExtra("cat_id");
        idd = Integer.parseInt(cat_id);
        Log.e(TAG, "onCreate: " + idd);

        if (NetworkConnection.isNetworkAvailable(activity)) {
            try {

                ApiService api = RetroClient.getApiService();
                Call<String> call = api.getAllWallpepars(idd);
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        // Toast.makeText(ShowWallPepar_Activity.this, "hiii", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponseWallpepar: " + response.body());

                        if (response.body() != null) {
                            callWallPaperAPI(response.body(), "");
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "onFailure: ");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(activity, "Please check your network connection", Toast.LENGTH_SHORT).show();

        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new ImageSave().execute();
            }
        });
    }

    private void callWallPaperAPI(String body, String s) {

        try {

            wallPepars_modelArrayList.clear();

            JSONArray jsonArray = new JSONArray(body);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                WallPepars_Model wallPeparsModel = new WallPepars_Model();

                wallPeparsModel.setId(jsonObject.optString("id"));
                wallPeparsModel.setImage(jsonObject.optString("image"));
                wallPeparsModel.setCategoryId(jsonObject.optString("category_id"));
                wallPepars_modelArrayList.add(wallPeparsModel);

            }
            wallPeparsAdapter = new WallPeparsAdapter(ShowWallPepar_Activity.this, wallPepars_modelArrayList);
            viewPager.setAdapter(wallPeparsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class ImageSave extends AsyncTask<Void,Void,Void> {

        Bitmap mbitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressBar ();
            Log.e (TAG, "onPreExecute: ");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPhotos ();
            File file = new File (Environment.getExternalStorageDirectory ().getPath () + File.separator + "WallPepar");


            if (!file.exists () && !file.mkdirs ()) ;
            /*{
                Toast.makeText ( getApplicationContext (), "Don't Have Folder", Toast.LENGTH_SHORT ).show ();
                return null;
            } else {
                Toast.makeText ( getApplicationContext (), "Image Saved", Toast.LENGTH_SHORT ).show ();
            }*/


            String simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss").format (new Date());

            mbitmap = bitmap;
            try {
                if (mbitmap != null) {
                    File new_file = new File (file, simpleDateFormat + ".jpeg");

                    if (!new_file.exists ())
                        new_file.createNewFile ();

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream (new_file);
                        mbitmap.compress (Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.close ();
                        MediaScannerConnection.scanFile (getApplicationContext (), new String[]{new_file.getAbsolutePath ()},
                                null, new MediaScannerConnection.MediaScannerConnectionClient () {
                                    @Override
                                    public void onMediaScannerConnected() {
                                    }

                                    @Override
                                    public void onScanCompleted(String path, final Uri uri) {
                                    }
                                });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace ();
                    } finally {
                        try {
                            if (fileOutputStream != null)
                                fileOutputStream.flush ();
                            fileOutputStream.close ();
                        } catch (IOException e) {
                            e.printStackTrace ();
                        }
                    }
                } else {
                    Log.e ("TAG", "Not Saved Image---->");
                }

            } catch (Exception e) {
                e.printStackTrace ();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressBar ();
            Toast.makeText (ShowWallPepar_Activity.this, "Image saved Successfully..Check Your Gallery !", Toast.LENGTH_SHORT).show ();
        }
    }

    private void getPhotos() {
        String url = wallPepars_modelArrayList.get (viewPager.getCurrentItem ()).getImage ();
        bitmap = getBitmapFromURL (url);
    }

    private Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL (src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
            connection.setDoInput (true);
            connection.connect ();
            InputStream input = connection.getInputStream ();
            Bitmap myBitmap = BitmapFactory.decodeStream (input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }

    private void onProgressBar() {
        progressDialog = new ProgressDialog (ShowWallPepar_Activity.this);
        progressDialog.setMessage ("Downloading...");
        progressDialog.setIndeterminate (false);
        progressDialog.setCancelable (false);
        progressDialog.setCanceledOnTouchOutside (false);
        progressDialog.show ();
    }


    private void hideProgressBar() {
        try {
            if (progressDialog != null && progressDialog.isShowing ()) {
                progressDialog.dismiss ();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace ();
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            progressDialog = null;
        }
    }


    @Override
    protected void onPause() {

        if(adView!=null)
        {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adView!=null)
        {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {

        if(adView!=null)
        {
            adView.destroy();
        }
        super.onDestroy();
    }
}
