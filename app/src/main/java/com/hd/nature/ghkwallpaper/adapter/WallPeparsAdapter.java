package com.hd.nature.ghkwallpaper.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.data_models.WallPepars_Model;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

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

        final DotProgressBar dotProgressBar = view.findViewById (R.id.dot_progress_bar);
        dotProgressBar.setVisibility(View.VISIBLE);
        //dotsLoaderView = view.findViewById(R.id.dotsLoader);
        //dotsLoaderView.setVisibility(View.VISIBLE);
        // dotsLoaderView.show();

        Log.e("TAG", "instantiateItem: " + wallPepars_modelArrayList.get(position).getImage());

        Glide.with(context)
                .load(wallPepars_modelArrayList.get(position).getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        dotProgressBar.setVisibility (View.GONE);

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
