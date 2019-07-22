package com.hd.nature.ghkwallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hd.nature.ghkwallpaper.R;
import com.hd.nature.ghkwallpaper.adapter.MoreAppsAdapter;
import com.hd.nature.ghkwallpaper.comman.NetworkConnection;
import com.hd.nature.ghkwallpaper.data_models.moreApps_Model;
import com.hd.nature.ghkwallpaper.retrofit.ApiService;
import com.hd.nature.ghkwallpaper.retrofit.RetroClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreAppsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<moreApps_Model> moreApps_modelArrayList = new ArrayList<>();
    MoreAppsAdapter moreAppsAdapter;
    Activity activity;
    String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_apps);
        activity=MoreAppsActivity.this;

        findViews();
        initViews();


    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerMoreApps);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        if (NetworkConnection.isNetworkAvailable(activity)) {

            try {
                ApiService api = RetroClient.getApiService();
                Call<String> call = api.getAllApps();
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
            Toast.makeText(MoreAppsActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseResponse(String body, String s) {
        try {

            JSONArray jsonArray = new JSONArray(body);
            for (int i = 0; i < jsonArray.length(); i++) {

                moreApps_Model model = new moreApps_Model();
                JSONObject jsonobject = (JSONObject) jsonArray.get(i);
                model.setAppIcon(jsonobject.optString("app_icon"));
                model.setAppName(jsonobject.optString("app_name"));
                model.setAppLink(jsonobject.getString("app_link"));
                moreApps_modelArrayList.add(model);

            }
            moreAppsAdapter = new MoreAppsAdapter(MoreAppsActivity.this, moreApps_modelArrayList);
            recyclerView.setAdapter(moreAppsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
