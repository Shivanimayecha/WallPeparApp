package com.hd.nature.ghkwallpaper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.comman.DataBaseHelper;
import com.hd.nature.ghkwallpaper.comman.GlobalApplication;
import com.hd.nature.ghkwallpaper.comman.NetworkConnection;
import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;
import com.hd.nature.ghkwallpaper.data_models.fvrt_model;
import com.hd.nature.ghkwallpaper.retrofit.ApiService;
import com.hd.nature.ghkwallpaper.retrofit.RetroClient;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class ShowWallPepar_Activity extends Activity {

    //  private ArrayList<File> al_my_photos = new ArrayList<>();

    ArrayList<fvrt_model> arrayList = new ArrayList<>();

    String TAG = "TAG";
    String cat_id;
    int idd;
    ViewPager viewPager;
    Activity activity;
    WallPeparsAdapter wallPeparsAdapter;
    ArrayList<WallPepars_Model> wallPepars_modelArrayList = new ArrayList<>();
    ImageButton save, back;
    ImageView favorite;
    ImageView share;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private AdView adView;
    AdRequest adRequest;
    SQLiteDatabase database;

    ImageView imageView;

    String id = "id";
    String img = "img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallpepar_);
        activity = ShowWallPepar_Activity.this;

        database = new DataBaseHelper(getApplicationContext()).getWritableDatabase();
        findViews();

        initviews();

    }

    private void findViews() {
        viewPager = findViewById(R.id.viewPager);
        favorite = findViewById(R.id.btn_fvrt);
        save = findViewById(R.id.btn_download);
        back = findViewById(R.id.btn_back);
        share = findViewById(R.id.btn_share);
        adView = findViewById(R.id.ad_View);
        //   imageView = findViewById(R.id.imageView);
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {

                Log.e(TAG, "onPageScrollStateChanged: ");

                String idd = wallPepars_modelArrayList.get(viewPager.getCurrentItem()).getId();
                String titlee = wallPepars_modelArrayList.get(viewPager.getCurrentItem()).getImage();

                String qu = "select * from wallpaper";

                Cursor cursor = database.rawQuery(qu, null);
                int count = cursor.getCount();

                if (count == 0) {

                    Log.e(TAG, "count == 0: ");
                } else {

                    Log.e(TAG, "count == else: ");

                    while (cursor.moveToNext()) {

                        if (cursor.getString(0).equalsIgnoreCase(idd)) {

                            favorite.setBackgroundResource(R.drawable.fillheart);

                        } else {

                            favorite.setBackgroundResource(R.drawable.blankheart);
                        }
                    }
                }


            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.e(TAG, "onPageScrolled: " + position);

            }

            public void onPageSelected(int position) {


                Log.e(TAG, "onPageSelected: " + position);
                // Check if this is the page you want.
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!GlobalApplication.getInstance().requestNewInterstitial()) {

                } else {
                    GlobalApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            GlobalApplication.getInstance().mInterstitialAd.setAdListener(null);
                            GlobalApplication.getInstance().mInterstitialAd = null;
                            GlobalApplication.getInstance().ins_adRequest = null;
                            GlobalApplication.getInstance().LoadAds();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                        }
                    });
                }
                new ImageSave().execute();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Intent intent = new Intent(ShowWallPepar_Activity.this, MainActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ImageShare().execute();
            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String idd = wallPepars_modelArrayList.get(viewPager.getCurrentItem()).getId();
                String titlee = wallPepars_modelArrayList.get(viewPager.getCurrentItem()).getImage();

                String qu = "select * from wallpaper";

                Cursor cursor = database.rawQuery(qu, null);
                int count = cursor.getCount();

                if (count == 0) {

                    favorite.setBackgroundResource(R.drawable.fillheart);
                    String ROW1 = "INSERT INTO " + "wallpaper" + " (" + id + "," + img + ") Values ('" + idd + "', '" + titlee + "')";
                    database.execSQL(ROW1);

                    Log.e(TAG, "onClick: cursor null");

                } else {

                    while (cursor.moveToNext()) {

                        Log.e("Data", cursor.getColumnName(0) + " : " + cursor.getString(0) + ", "
                                + cursor.getColumnName(1) + " : " + cursor.getString(1) + ", ");

                        Log.e(TAG, "cursor-->: " + cursor.getString(0));
                        Log.e(TAG, "idd--> " + idd);
                        if (cursor.getString(0).equalsIgnoreCase(idd)) {

                            Log.e(TAG, "onClick:if ");
                            String del = "DELETE FROM " + "wallpaper" + " WHERE " + id + "='" + idd + "'";
                            database.execSQL(del);

                            favorite.setBackgroundResource(R.drawable.blankheart);

                        } else {

                            Log.e(TAG, "onClick:else ");
                            String ROW1 = "INSERT INTO " + "wallpaper" + " (" + id + "," + img + ") Values ('" + idd + "', '" + titlee + "')";
                            database.execSQL(ROW1);

                            favorite.setBackgroundResource(R.drawable.fillheart);
                        }
                    }
                    //Toast.makeText(ShowWallPepar_Activity.this, "Successfully added in to favorite !", Toast.LENGTH_SHORT).show();

                }


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

    private class ImageShare extends AsyncTask<Void, Void, Void> {

        //Bitmap mbitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   onProgressBar();
            Log.e(TAG, "onPreExecute: ");*/
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPhotos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            hideProgressBar();
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(intent, "Share"));

        }
    }

    private class ImageSave extends AsyncTask<Void, Void, Void> {

        Bitmap mbitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressBar();
            Log.e(TAG, "onPreExecute: ");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPhotos();
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "WallPepar");


            if (!file.exists() && !file.mkdirs()) ;
            /*{
                Toast.makeText ( getApplicationContext (), "Don't Have Folder", Toast.LENGTH_SHORT ).show ();
                return null;
            } else {
                Toast.makeText ( getApplicationContext (), "Image Saved", Toast.LENGTH_SHORT ).show ();
            }*/


            String simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss").format(new Date());

            mbitmap = bitmap;
            try {
                if (mbitmap != null) {
                    File new_file = new File(file, simpleDateFormat + ".jpeg");

                    if (!new_file.exists())
                        new_file.createNewFile();

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(new_file);
                        mbitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.close();
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{new_file.getAbsolutePath()},
                                null, new MediaScannerConnection.MediaScannerConnectionClient() {
                                    @Override
                                    public void onMediaScannerConnected() {
                                    }

                                    @Override
                                    public void onScanCompleted(String path, final Uri uri) {
                                    }
                                });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fileOutputStream != null)
                                fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e("TAG", "Not Saved Image---->");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressBar();
            Toast.makeText(ShowWallPepar_Activity.this, "Image saved Successfully..Check Your Gallery !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPhotos() {
        String url = wallPepars_modelArrayList.get(viewPager.getCurrentItem()).getImage();
        bitmap = getBitmapFromURL(url);
    }

    private Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void onProgressBar() {
        progressDialog = new ProgressDialog(ShowWallPepar_Activity.this);
        progressDialog.setMessage("Downloading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void hideProgressBar() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }
    }

    @Override
    protected void onPause() {

        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {

        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    public class WallPeparsAdapter extends PagerAdapter {


        Context context;
        public ArrayList<WallPepars_Model> wallPepars_modelArrayList = new ArrayList<>();

        public WallPeparsAdapter(Context context, ArrayList<WallPepars_Model> wallPepars_modelArrayList) {
            this.context = context;
            this.wallPepars_modelArrayList = wallPepars_modelArrayList;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.viewpager_layout, container, false);

            ((ViewPager) container).addView(view);
            ImageView imag = (ImageView) view.findViewById(R.id.imageView);

            final DotProgressBar dotProgressBar = view.findViewById(R.id.dot_progress_bar);
            dotProgressBar.setVisibility(View.VISIBLE);
            //dotsLoaderView = view.findViewById(R.id.dotsLoader);
            //dotsLoaderView.setVisibility(View.VISIBLE);
            // dotsLoaderView.show();

            Log.e("TAG", "instantiateItem: " + wallPepars_modelArrayList.get(position).getImage());

            Glide.with(context)
                    .load(wallPepars_modelArrayList.get(position).getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            dotProgressBar.setVisibility(View.GONE);

                      /* dotsLoaderView.setVisibility(View.GONE);
                        dotsLoaderView.hide();*/
                            return false;
                        }
                    })
                    .into(imag);
            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return wallPepars_modelArrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

}
