package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.AnswerAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.AnswerData;
import com.placement.prepare.e2buddy.object.User;
import com.placement.prepare.e2buddy.preference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAnswerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private User user;
    private List<AnswerData> answerViewList;
    private AnswerAdapter answerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);


        recyclerView = findViewById(R.id.recyclerViewResult);


        user = SessionManager.getInstance(this).getUser();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewAnswerActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        answerViewList = new ArrayList<>();


        answerAdapter = new AnswerAdapter(ViewAnswerActivity.this, answerViewList);

        getAnswer();
    }

    private void getAnswer(){


        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_question(1,1);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i=0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                int questionId = object.getInt("questionId");
                                String question = object.getString("question");

                                String option1 = object.getString("option1");
                                String option2 = object.getString("option2");
                                String option3 = object.getString("option3");
                                String option4 = object.getString("option4");

                                String correctAnswer = object.getString("correctAnswer");

                                getUserAnswer(questionId,question,correctAnswer);

                            }

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

    private void getUserAnswer(final int questionId, final String question, final String correctAnswer){
        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_user_result(user.getId(), 1, questionId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){

                            JSONObject object = jsonObject.getJSONObject("data");

                            String answer = object.getString("answer");
                            answerViewList.add(new AnswerData(questionId,question,correctAnswer,answer));

                        }
                        else {
                            answerViewList.add(new AnswerData(questionId, question, correctAnswer, "Not Attempted"));
                        }

                        Collections.sort(answerViewList, new Comparator<AnswerData>() {
                            @Override
                            public int compare(AnswerData lhs, AnswerData rhs) {
                                return lhs.getQuestionId()- rhs.getQuestionId();
                            }
                        });

                        recyclerView.setAdapter(answerAdapter);


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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

}