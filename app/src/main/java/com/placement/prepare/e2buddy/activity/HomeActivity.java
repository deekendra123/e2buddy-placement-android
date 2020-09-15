package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.PlacementPaperAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.CategoryData;
import com.placement.prepare.e2buddy.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPopular, recyclerViewRecruiters, recyclerViewFeatured;
    private static final String TAG = HomeActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewPopular  = findViewById(R.id.recyclerViewPopular);
        recyclerViewRecruiters  = findViewById(R.id.recyclerViewRecruiter);
        recyclerViewFeatured  = findViewById(R.id.recyclerViewFeatured);

        loadPopularPaper();

}

    private void loadPopularPaper() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopular.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFeatured.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewRecruiters.setLayoutManager(layoutManager2);

        List<CategoryData> list = new ArrayList<>();

        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_category();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                    Log.e(TAG, new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                int categoryId = object.getInt("categoryId");
                                String categoryName = object.getString("categoryName");

                                list.add(new CategoryData(categoryId, categoryName));
                            }

                            PlacementPaperAdapter placementPaperAdapter = new PlacementPaperAdapter(HomeActivity.this, list);
                            recyclerViewPopular.setAdapter(placementPaperAdapter);
                            recyclerViewFeatured.setAdapter(placementPaperAdapter);
                            recyclerViewRecruiters.setAdapter(placementPaperAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //Log.e(TAG, t.getMessage());

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.showToast("Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}