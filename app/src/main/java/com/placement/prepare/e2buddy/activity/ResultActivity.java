package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.adapter.AnswerReviewAdapter;
import com.placement.prepare.e2buddy.fragmentdialog.PlacementCategorySheet;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.AnswerData;
import com.placement.prepare.e2buddy.object.User;
import com.placement.prepare.e2buddy.preference.SessionManager;

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

public class ResultActivity extends AppCompatActivity {

    private TextView tvTotalQuestion,tvAttemptQuestion,tvCorrectQuestion,tvWrongQuestion,tvUserName;
    private Button btViewAnswer;
    private User user;
    private int corrAnswer=0, wrongAnswer=0, totalQuestion=0, totalAttempted=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);
        tvAttemptQuestion = findViewById(R.id.tvAttemptQuestion);
        tvCorrectQuestion = findViewById(R.id.tvCorrectQuestion);
        tvWrongQuestion = findViewById(R.id.tvWrongQuestion);
        btViewAnswer = findViewById(R.id.btViewAnswer);
        tvUserName = findViewById(R.id.tvUserName);

        user = SessionManager.getInstance(this).getUser();
        totalQuestion = getIntent().getIntExtra("totalQuestion",-1);

        getResult();
        tvTotalQuestion.setText(""+totalQuestion);
        tvUserName.setText(""+user.getName());
    }

    private void getResult(){

        List<String> list = new ArrayList<>();

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

                                if (correctAnswer.equals(answer)){
                                    corrAnswer = corrAnswer+1;
                                    updateUserAnswer(questionId, "1");
                                }
                                else {
                                    wrongAnswer = wrongAnswer+1;
                                    updateUserAnswer(questionId, "0");

                                }

                                list.add(question);
                            }

                            totalAttempted = list.size();
                            tvAttemptQuestion.setText(""+list.size());
                            tvCorrectQuestion.setText(""+corrAnswer);
                            tvWrongQuestion.setText(""+wrongAnswer);
                            insertMark();
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

    private void updateUserAnswer(int questionId, String status){
        API api = ApiClient.getClient().create(API.class);
        Call call = api.insert_user_answer(1,user.getId(),questionId,"ans","2","date","time", status);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void insertMark(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String date = sdfDate.format(new Date());
        String time = sdfTime.format(new Date());

        int marks = corrAnswer*3;

        API api = ApiClient.getClient().create(API.class);
        Call call = api.insert_user_marks(1, user.getId(), corrAnswer,wrongAnswer,marks,date, time);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

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
        QuestionActivity.questionActivity.finish();
        AnswerReviewActivity.answerReviewActivity.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void btViewAnswer(View view) {
        Intent intent = new Intent(ResultActivity.this, ViewAnswerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }
}