package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.AnswerReviewAdapter;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.AnswerData;
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

public class AnswerReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnswers;
    private Button btsubmit,btEdit;
    private AnswerReviewAdapter adapter;
    private List<AnswerData> list;
    public static AnswerReviewActivity answerReviewActivity;
    private ProgressBar progressBar;
    private User user;
    private int totalQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_review);

        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        btsubmit = findViewById(R.id.btsubmit);
        btEdit = findViewById(R.id.btEdit);
        answerReviewActivity=this;
        progressBar = findViewById(R.id.progressBar);
        user = SessionManager.getInstance(AnswerReviewActivity.this).getUser();

        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);
        getAnswer();
        onClick();

    }

    private void onClick(){


            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });


        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(AnswerReviewActivity.this)
                        .setTitle("CONGRATULATIONS!")
                        .setMessage("Your answers are Successfully submitted. \n\n Click DONE.")
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(AnswerReviewActivity.this, ResultActivity.class);
                                intent.putExtra("totalQuestion", totalQuestion);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }).create().show();


            }
        });
    }

    private void getAnswer(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AnswerReviewActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewAnswers.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AnswerReviewActivity.this,
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));

        recyclerViewAnswers.addItemDecoration(dividerItemDecoration);
        list = new ArrayList<>();
        adapter = new AnswerReviewAdapter(AnswerReviewActivity.this, list, user.getId());

        API api = ApiClient.getClient().create(API.class);
        Call call = api.get_user_answer(user.getId(), 1);
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
                                String correctAnswer = object.getString("correctAnswer");
                                String answer = object.getString("answer");
                                list.add(new AnswerData(questionId,question,correctAnswer,answer));
                            }

                            recyclerViewAnswers.setAdapter(adapter);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}