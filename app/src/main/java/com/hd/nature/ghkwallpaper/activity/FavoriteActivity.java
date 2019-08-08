package com.hd.nature.ghkwallpaper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.comman.DataBaseHelper;
import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;
import com.hd.nature.ghkwallpaper.data_models.fvrt_model;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<fvrt_model> fvrt_modelArrayList = new ArrayList<>();
    FvrtWallpeparAdapter fvrtWallpeparAdapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        database = new DataBaseHelper(getApplicationContext()).getWritableDatabase();

        findViews();
        initviews();

    }

    private void findViews() {

        viewPager = findViewById(R.id.viewPager1);
    }

    private void initviews() {


        Cursor cursor = database.query("wallpaper", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.e("Data", cursor.getColumnName(0) + " : " + cursor.getString(0) + ", "
                        + cursor.getColumnName(1) + " : " + cursor.getString(1) + ", ");
                fvrt_modelArrayList.add (new fvrt_model (cursor.getString (0), cursor.getString (1)));

                fvrtWallpeparAdapter = new FvrtWallpeparAdapter(FavoriteActivity.this, fvrt_modelArrayList);
                viewPager.setAdapter(fvrtWallpeparAdapter);
            }

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
