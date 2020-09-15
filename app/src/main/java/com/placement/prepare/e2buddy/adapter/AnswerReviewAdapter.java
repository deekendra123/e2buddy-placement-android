package com.placement.prepare.e2buddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.object.AnswerData;

import java.util.List;

public class AnswerReviewAdapter extends RecyclerView.Adapter<AnswerReviewAdapter.QuestionHolder> {

    private Context mCtx;
    private List<AnswerData> list;
    private int userId;


    public AnswerReviewAdapter(Context mCtx, List<AnswerData> list, int userId) {
        this.mCtx = mCtx;
        this.list = list;
        this.userId = userId;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mCtx).inflate(R.layout.review_answer_layout, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {

        AnswerData answerData = list.get(position);
        holder.tvQuestionId.setText("Ques. "+answerData.getQuestionId());
        holder.tvQuestion.setText(""+answerData.getQuestion());
        holder.tvAnswer.setText(""+answerData.getUserAnswer());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion,tvAnswer;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);

        }
    }


}
