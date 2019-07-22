package com.hd.nature.ghkwallpaper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.comman.GlobalApplication;
import com.hd.nature.ghkwallpaper.comman.NetworkConnection;
import com.hd.nature.ghkwallpaper.data_models.categoriesModel;
import com.hd.nature.ghkwallpaper.retrofit.ApiService;
import com.hd.nature.ghkwallpaper.retrofit.RetroClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Activity activity;
    private AdView adView;
    AdRequest adRequest;
    InterstitialAd interstitialAd;


    ArrayList<categoriesModel> categoriesModelArrayList = new ArrayList<>();
    CategoriesAdapter categoriesAdapter;
    String TAG = "TAG";

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        toolbar = findViewById(R.id.toolbar);
        findViews();
        initViews();
        initNavigationDrawer();

    }

    private void findViews() {

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adView = findViewById(R.id.ad_View);
    }

    private void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.privacy:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.help:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.apps:
                        Intent intent = new Intent(MainActivity.this, MoreAppsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.rating:
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                    case R.id.clrcache:
                        clearApplicationData();
                        Toast.makeText(MainActivity.this, "Cache Data is clear !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
/*        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("raj.amalw@learn2crack.com");*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout
                , toolbar
                , R.string.drawer_open
                , R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initViews() {

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);




/*
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                private void showInterstitial()
            {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
            }
        });
*/
        if (NetworkConnection.isNetworkAvailable(activity)) {

            try {
                ApiService api = RetroClient.getApiService();
                Call<String> call = api.getAllCategories();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Log.e(TAG, "onResponse: " + response.body());

                        if (response.body() != null) {

                            parseResponse(response.body(), "");

                        } else {
                            //showSomethingwentWrong();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //hideProgressbar();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(activity, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseResponse(String body, String s) {
        try {

            JSONArray jsonArray = new JSONArray(body);
            for (int i = 0; i < jsonArray.length(); i++) {

                categoriesModel categories_Model = new categoriesModel();
                JSONObject jsonobject = (JSONObject) jsonArray.get(i);
                categories_Model.setId(jsonobject.optString("id"));
                categories_Model.setCategoryImage(jsonobject.optString("category_img"));
                categories_Model.setCategories(jsonobject.getString("categories"));
                categoriesModelArrayList.add(categories_Model);

            }
            categoriesAdapter = new CategoriesAdapter(MainActivity.this, categoriesModelArrayList);
            recyclerView.setAdapter(categoriesAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        clearApplicationData();
        if(adView!=null)
        {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("Error", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
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
       /* if(adView!=null)
        {
            adView.resume();
        }*/

    }
    public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

        private ArrayList<categoriesModel> categoriesModelArrayList;
        String TAG = "TAG";
        private Context context;

        public CategoriesAdapter(Context context, ArrayList<categoriesModel> categoriesModelArrayList) {
            this.categoriesModelArrayList = categoriesModelArrayList;
            this.context = context;
        }


        @NonNull
        @Override
        public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_itemlayout, parent, false);
            return new CategoriesAdapter.ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            /*   TextView lblappname;
               ImageView imgappicon;
               RelativeLayout ryllayoutd;*/
            public ImageView catIamge;
            public TextView catName;
            public RelativeLayout relativeLayout;



            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                catIamge = itemView.findViewById(R.id.catImage);
                catName = itemView.findViewById(R.id.catName);
                relativeLayout = itemView.findViewById(R.id.rl);


            }
        }
        @Override
        public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, final int position) {

            Picasso.get().load(categoriesModelArrayList.get(position).getCategoryImage()).into(holder.catIamge);
            holder.catName.setText(categoriesModelArrayList.get(position).getCategories());


            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (!GlobalApplication.getInstance().requestNewInterstitial()) {

                        Intent intent = new Intent(context, ShowWallPepar_Activity.class);
                        intent.putExtra("cat_id", categoriesModelArrayList.get(position).getId());
                        context.startActivity(intent);

                    } else {
                        GlobalApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                GlobalApplication.getInstance().mInterstitialAd.setAdListener(null);
                                GlobalApplication.getInstance().mInterstitialAd = null;
                                GlobalApplication.getInstance().ins_adRequest = null;
                                GlobalApplication.getInstance().LoadAds();

                                Intent intent = new Intent(context, ShowWallPepar_Activity.class);
                                intent.putExtra("cat_id", categoriesModelArrayList.get(position).getId());
                                context.startActivity(intent);
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
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoriesModelArrayList.size();
        }
    }






}
