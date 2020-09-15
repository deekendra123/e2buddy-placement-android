package com.placement.prepare.e2buddy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.PlacementPaperAdapter;
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

public class QuizFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewPopular, recyclerViewRecruiters, recyclerViewFeatured;
    private static final String TAG = QuizFragment.class.getSimpleName();

    public QuizFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        recyclerViewPopular  = view.findViewById(R.id.recyclerViewPopular);
        recyclerViewRecruiters  = view.findViewById(R.id.recyclerViewRecruiter);
        recyclerViewFeatured  = view.findViewById(R.id.recyclerViewFeatured);

        loadPopularPaper();

        return view;
    }

    private void loadPopularPaper() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopular.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFeatured.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
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

                            PlacementPaperAdapter placementPaperAdapter = new PlacementPaperAdapter(getActivity(), list);
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

}