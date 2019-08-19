package com.hd.nature.ghkwallpaper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.hd.nature.ghkwallpaper.data_models.fvrt_model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FavoriteActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageView share, fvrt, save, back;
    AdView adView;
    AdRequest adRequest;
    ArrayList<fvrt_model> fvrt_modelArrayList = new ArrayList<>();
    ArrayList<String> arrayList1 = new ArrayList<>();
    FvrtWallpeparAdapter fvrtWallpeparAdapter;
    SQLiteDatabase database;
    Bitmap bitmap;
    Activity activity;
    ProgressDialog progressDialog;
    String id = "id";
    String img = "img";
    String TAG = "TAG";
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        activity = FavoriteActivity.this;

        database = new DataBaseHelper(getApplicationContext()).getWritableDatabase();

        findViews();
        initviews();

    }

    private void findViews() {

        viewPager = findViewById(R.id.viewPager1);
        back = findViewById(R.id.back_btn);
        share = findViewById(R.id.share_btn);
        fvrt = findViewById(R.id.fvrt_btn);
        save = findViewById(R.id.download_btn);
        adView = findViewById(R.id.ad);
    }

    private void initviews() {

        builder = new AlertDialog.Builder(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Cursor cursor = database.query("wallpaper", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.e("Data", cursor.getColumnName(0) + " : " + cursor.getString(0) + ", "
                        + cursor.getColumnName(1) + " : " + cursor.getString(1) + ", ");
                fvrt_modelArrayList.add(new fvrt_model(cursor.getString(0), cursor.getString(1)));

                fvrtWallpeparAdapter = new FvrtWallpeparAdapter(FavoriteActivity.this, fvrt_modelArrayList);
                viewPager.setAdapter(fvrtWallpeparAdapter);
            }

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageShare().execute();
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.e(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                Log.e(TAG, "onPageScrollStateChanged: ");


            }
        });

        if (fvrt_modelArrayList.size() > 0) {


            fvrt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String idd = fvrt_modelArrayList.get(viewPager.getCurrentItem()).getFid();
                    String titlee = fvrt_modelArrayList.get(viewPager.getCurrentItem()).getUrl();

                    String qu = "SELECT count(*) FROM wallpaper";
                    Cursor cursor = database.rawQuery(qu, null);
                    cursor.moveToFirst();


                    String del = "DELETE FROM " + "wallpaper" + " WHERE " + id + "='" + idd + "'";
                    database.execSQL(del);

                    fvrt.setBackgroundResource(R.drawable.blankheart);
                   // Toast.makeText(activity, "Successfully remove from favorite !", Toast.LENGTH_SHORT).show();

                    onBackPressed();

                }


            });
        } else {
           // builder.setMessage("Your Favorite List is empty ...Make Favorite !") .setTitle("Alert");

            //Setting message manually and performing action on button click
            builder.setMessage("Your Favorite List Is Empty")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                       /*     Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    });

            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Alert");
            alert.show();
            // Toast.makeText(activity, "Make your favorite list !", Toast.LENGTH_SHORT).show();

        }

    }

    private class ImageShare extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPhotos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
            Uri bitmapURI = Uri.parse(bitmapPath);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(intent.EXTRA_STREAM, bitmapURI);
            startActivity(Intent.createChooser(intent, "Share Using"));
        }

    }

    private class ImageSave extends AsyncTask<Void, Void, Void> {

        Bitmap mbitmap;

        @Override
        protected void onPreExecute() {
            onProgressBar();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPhotos();
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "WallPepar");

            if (!file.exists() && file.mkdir()) ;

            String simpleDateFormate = new SimpleDateFormat("yyyymmsshhmmss").format(new Date());

            mbitmap = bitmap;
            try {
                if (mbitmap != null) {
                    File newFile = new File(file, simpleDateFormate + ".jpeg");
                    if (!newFile.exists())
                        newFile.createNewFile();

                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(newFile);
                        mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.close();
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{newFile.getAbsolutePath()},
                                null, new MediaScannerConnection.MediaScannerConnectionClient() {
                                    @Override
                                    public void onMediaScannerConnected() {

                                    }

                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {

                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outputStream != null)
                                outputStream.flush();
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
            Toast.makeText(activity, "Image saved Successfully..Check Your Gallery !", Toast.LENGTH_SHORT).show();
        }


    }

    private void getPhotos() {

        String url = fvrt_modelArrayList.get(viewPager.getCurrentItem()).getUrl();
        bitmap = getBitmapFromURL(url);

    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void onProgressBar() {
        progressDialog = new ProgressDialog(FavoriteActivity.this);
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


    public class FvrtWallpeparAdapter extends PagerAdapter {

        Context context;
        public ArrayList<fvrt_model> fvrt_modelArrayList = new ArrayList<>();

        public FvrtWallpeparAdapter(Context context, ArrayList<fvrt_model> fvrt_modelArrayList) {
            this.context = context;
            this.fvrt_modelArrayList = fvrt_modelArrayList;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.fvrt_viewpager_layout, container, false);

            ((ViewPager) container).addView(view);
            ImageView imag = (ImageView) view.findViewById(R.id.imageView1);

            final DotProgressBar dotProgressBar = view.findViewById(R.id.dot_progress_bar1);
            dotProgressBar.setVisibility(View.VISIBLE);

            Log.e("TAG", "instantiateItem: " + fvrt_modelArrayList.get(position).getUrl());

            Glide.with(context)
                    .load(fvrt_modelArrayList.get(position).getUrl())
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
            return fvrt_modelArrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }


}
