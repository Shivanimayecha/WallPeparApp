package com.hd.nature.ghkwallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.activity.MainActivity;
import com.hd.nature.ghkwallpaper.activity.ShowWallPepar_Activity;
import com.hd.nature.ghkwallpaper.data_models.categoriesModel;
import com.hd.nature.ghkwallpaper.data_models.moreApps_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoreAppsAdapter extends RecyclerView.Adapter<MoreAppsAdapter.ViewHolder> {

    private ArrayList<moreApps_Model> moreApps_modelArrayList;
    String TAG = "TAG";
    private Context context;

    public MoreAppsAdapter(Context context, ArrayList<moreApps_Model> moreApps_modelArrayList) {
        this.moreApps_modelArrayList = moreApps_modelArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public MoreAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.moreapps_design, parent, false);
        return new MoreAppsAdapter.ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView appicon;
        public TextView appname;
        public Button applink;
        public RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appicon = itemView.findViewById(R.id.appIcon);
            appname = itemView.findViewById(R.id.appName);
            applink = itemView.findViewById(R.id.btn_install);
            relativeLayout = itemView.findViewById(R.id.rl1);

        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(moreApps_modelArrayList.get(position).getAppIcon()).into(holder.appicon);
        holder.appname.setText(moreApps_modelArrayList.get(position).getAppName());
        Log.e (TAG, "onBindViewHolder: " + moreApps_modelArrayList.get (position).getAppIcon ());
        holder.applink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(moreApps_modelArrayList.get(position).getAppLink()));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return moreApps_modelArrayList.size();
    }
}
