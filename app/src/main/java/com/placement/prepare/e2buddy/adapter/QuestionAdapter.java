package com.placement.prepare.e2buddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.QuestionData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {


    private Context mCtx;
    private List<QuestionData> list;
    private String ans;
    private SimpleDateFormat sdf;
    private int userId;

    public QuestionAdapter(Context mCtx, List<QuestionData> list, int userId) {
        this.mCtx = mCtx;
        this.list = list;
        this.userId = userId;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.question_layout, parent, false);
        return new QuestionHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionHolder holder, int position) {

        final QuestionData questionData = list.get(position);
        int queId = position+1;
        holder.tvQuestionId.setText("Ques. "+queId);
        holder.tvQuestion.setText(""+questionData.getQuestion());

        holder.op1.setText(""+questionData.getOption1());
        holder.op2.setText(""+questionData.getOption2());
        holder.op3.setText(""+questionData.getOption3());
        holder.op4.setText(""+questionData.getOption4());


        sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());


        holder.op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.op2.setChecked(false);
                holder.op3.setChecked(false);
                holder.op4.setChecked(false);


                if (holder.op1.isChecked()) {
                        ans = holder.op1.getText().toString();
                        insertAnswer(questionData.getQuestionId(), ans, "1");

                    } else {
                    ans = holder.op1.getText().toString();
                     insertAnswer(questionData.getQuestionId(), ans, "0");
                }


                }
        });

        holder.op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op3.setChecked(false);
                holder.op4.setChecked(false);

                if (holder.op2.isChecked()) {

                        ans = holder.op2.getText().toString();

                      insertAnswer(questionData.getQuestionId(), ans, "1");

                } else {
                        ans = holder.op2.getText().toString();
                        insertAnswer(questionData.getQuestionId(), ans, "0");
                }
                }

        });

        holder.op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op2.setChecked(false);
                holder.op4.setChecked(false);

                    if (holder.op3.isChecked()) {
                        ans = holder.op3.getText().toString();
                        insertAnswer(questionData.getQuestionId(), ans, "1");

                    } else {
                        ans = holder.op3.getText().toString();
                        insertAnswer(questionData.getQuestionId(), ans, "0");

                    }


            }
        });

        holder.op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.op1.setChecked(false);
                holder.op2.setChecked(false);
                holder.op3.setChecked(false);


                    if (holder.op4.isChecked()) {
                        ans = holder.op4.getText().toString();
                        insertAnswer(questionData.getQuestionId(), ans, "1");

                    } else {
                        ans = holder.op4.getText().toString();

                        insertAnswer(questionData.getQuestionId(), ans, "0");

                    }

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion;
        CheckBox op1, op2, op3, op4;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            op1 =  itemView.findViewById(R.id.option1);
            op2 =  itemView.findViewById(R.id.option2);
            op3 =  itemView.findViewById(R.id.option3);
            op4 =  itemView.findViewById(R.id.option4);

        }
    }

    private void insertAnswer(int questionId, String ans, String status){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String date = sdfDate.format(new Date());
        String time = sdfTime.format(new Date());

        API api = ApiClient.getClient().create(API.class);
        Call call = api.insert_user_answer(1,userId,questionId,ans,status,date,time,"0");

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
}
