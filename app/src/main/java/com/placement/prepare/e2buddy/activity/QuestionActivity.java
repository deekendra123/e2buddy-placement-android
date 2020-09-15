package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.QuestionAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.QuestionData;
import com.placement.prepare.e2buddy.object.User;
import com.placement.prepare.e2buddy.preference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity {

    private List<QuestionData> list;
    private QuestionAdapter questionAdapter;
    private RecyclerView recyclerViewQuestions;
    public static QuestionActivity questionActivity;
    private User user;
    private ProgressBar progressBar;
    private int totalQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        progressBar = findViewById(R.id.progressBar);
        user = SessionManager.getInstance(QuestionActivity.this).getUser();
        questionActivity = this;
        getQuestion();

    }

    private void getQuestion(){
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuestionActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewQuestions.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        questionAdapter = new QuestionAdapter(QuestionActivity.this, list, user.getId());

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
                               // String description = object.getString("description");

                                String correctAnswer = object.getString("correctAnswer");

                                list.add(new QuestionData(questionId,question,option1,option2,option3,option4,correctAnswer,"description"));
                            }
                            recyclerViewQuestions.setAdapter(questionAdapter);
                            progressBar.setVisibility(View.GONE);
                            totalQuestion = list.size();
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

    public void btSubmit(View view) {
        Intent intent = new Intent(QuestionActivity.this, AnswerReviewActivity.class);
        intent.putExtra("totalQuestion", totalQuestion);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Alert")
                .setMessage("Are you sure you want to Close?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                        intent.putExtra("totalQuestion", totalQuestion);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


}