package com.placement.prepare.e2buddy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.MonthAdapter;
import com.placement.prepare.e2buddy.adapter.SpinnerAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.InternShipDomain;
import com.placement.prepare.e2buddy.object.User;
import com.placement.prepare.e2buddy.preference.SessionManager;
import com.placement.prepare.e2buddy.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternShipFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = InternShipFragment.class.getSimpleName();
   private String duration="", domain="";
   private int domainId=0;
    private Spinner spMonth,spDomain;
    private EditText etName,etEmail,etNumber;
    private TextView tvDomainName,tvDomainName1,tvDomainName2,tvStartDate,tvEndDate;
    private User user;
    private Button btSubmit;
    private RelativeLayout relativeLayoutRequest,relativeLayoutApplied,relativeLayoutResponse,relativeLayoutAccepted;

    private ProgressBar progressBar;
    public InternShipFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InternShipFragment newInstance(String param1, String param2) {
        InternShipFragment fragment = new InternShipFragment();
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
        View view = inflater.inflate(R.layout.fragment_placement, container, false);
        spMonth = view.findViewById(R.id.spMonth);
        spDomain = view.findViewById(R.id.spDomain);
        etName = view.findViewById(R.id.etName);
        relativeLayoutRequest = view.findViewById(R.id.relativeLayoutRequest);
        relativeLayoutApplied = view.findViewById(R.id.relativeLayoutApplied);
        relativeLayoutResponse = view.findViewById(R.id.relativeLayoutResponse);
        relativeLayoutAccepted = view.findViewById(R.id.relativeLayoutAccepted);
        tvDomainName = view.findViewById(R.id.tvDomainName);
        tvDomainName1 = view.findViewById(R.id.tvDomainName1);
        tvDomainName2 = view.findViewById(R.id.tvDomainName2);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        btSubmit = view.findViewById(R.id.btSubmit);
        tvEndDate = view.findViewById(R.id.tvEndDate);

        progressBar = view.findViewById(R.id.progressBar);
        getSpin();
        selectDuration(spMonth);

        insertRequest();

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()){
                    Utils.showToast("Enter your name");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    insertRequest();
                }
            }
        });

        return view;
    }

    private void getSpin(){
        ArrayList<InternShipDomain> list = new ArrayList<>();

        list.add(new InternShipDomain(0, "Domain Name", "Openings Left"));

        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_internship_domain();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                int domainId = object.getInt("domainId");
                                String domainName = object.getString("domainName");
                                String numberOfOpening = object.getString("numberOfOpening");

                                list.add(new InternShipDomain(domainId, domainName, numberOfOpening));
                            }

                            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), 0,
                                    list);
                            spDomain.setAdapter(spinnerAdapter);

                            spDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    InternShipDomain internShipDomain = spinnerAdapter.getItem(position);
                                    domain = internShipDomain.getDomainName();
                                    domainId = internShipDomain.getDomainId();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


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

    private void insertRequest(){
        user = SessionManager.getInstance(getActivity()).getUser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy::HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date());

        API api = ApiClient.getClient().create(API.class);
        Call call = api.insert_request(user.getId(), etName.getText().toString(), domainId, date, duration);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                    Log.e(TAG, new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){
                            JSONObject object = jsonObject.getJSONObject("data");
                            int status = object.getInt("status");
                            String domainName = object.getString("domainName");
                            String startDate = object.getString("startDate");
                            String endDate = object.getString("endDate");

                            progressBar.setVisibility(View.GONE);

                            if (status==0){
                                relativeLayoutRequest.setVisibility(View.GONE);
                                relativeLayoutApplied.setVisibility(View.VISIBLE);
                                tvDomainName1.setText("for "+domainName);

                            }
                            else if (status==1){
                                relativeLayoutResponse.setVisibility(View.VISIBLE);
                                tvDomainName.setText(domainName+" Internship");
                            }
                            else if (status==2){
                                relativeLayoutAccepted.setVisibility(View.VISIBLE);
                                tvDomainName2.setText(""+domainName+ " Internship");
                                tvStartDate.setText("START DATE : "+startDate);
                                tvEndDate.setText("END DATE : " + endDate);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //Log.e(TAG, t.getMessage());

                relativeLayoutRequest.setVisibility(View.VISIBLE);

                Log.e("TAGkj", t.getMessage());
            }
        });
    }

    private void selectDuration(Spinner spMonth){

        List<String> months = new ArrayList<String>();
        months.add("1");
        months.add("2");
        months.add("3");
        months.add("4");
        months.add("5");
        months.add("6");
        months.add("7");
        months.add("8");
        months.add("9");
        months.add("10");
        months.add("11");
        months.add("12");

       MonthAdapter adapter = new MonthAdapter(getActivity(),
                R.layout.spinner_item,
                months);

       spMonth.setAdapter(adapter);

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duration = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}