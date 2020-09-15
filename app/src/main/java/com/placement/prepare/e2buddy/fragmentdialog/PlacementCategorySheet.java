package com.placement.prepare.e2buddy.fragmentdialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.PlacementSubCategoryAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.CategoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacementCategorySheet extends BottomSheetDialogFragment {


    RecyclerView recyclerView;
    List<CategoryData> list;
    PlacementSubCategoryAdapter answerAdapter;
    private String categoryName;
    private int categoryId;
    private TextView tvName;
    private static final String TAG = PlacementCategorySheet.class.getSimpleName();
    public static PlacementCategorySheet placementCategorySheet;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.category_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvName = view.findViewById(R.id.tvName);

        categoryId = getArguments().getInt("categoryId");
        categoryName = getArguments().getString("categoryName");

        tvName.setText(""+categoryName);
        loadSubCategory();

        placementCategorySheet = this;

        return view;
    }

    private void loadSubCategory(){

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutM
        list = new ArrayList<>();
        answerAdapter = new PlacementSubCategoryAdapter(getActivity(), list);

        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_sub_category(categoryId);
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

                                int subCategoryId = object.getInt("subCategoryId");
                                String subCategoryName = object.getString("subCategoryName");

                                list.add(new CategoryData(subCategoryId, subCategoryName));
                            }

                            recyclerView.setAdapter(answerAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.category_bottom_sheet,null,false);
       // Unbinder unbinder = ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);
        FrameLayout bottomSheet = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bglinear);

        super.setupDialog(dialog, style);
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }


}
