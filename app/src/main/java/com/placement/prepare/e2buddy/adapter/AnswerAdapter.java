package com.placement.prepare.e2buddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.object.AnswerData;

import java.util.List;


public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.QuestionHolder> {

    private Context mCtx;
    private List<AnswerData> list;


    public AnswerAdapter(Context mCtx, List<AnswerData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.result_layout, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {

        AnswerData answerData = list.get(position);
        holder.tvQuestionId.setText("Ques. "+answerData.getQuestionId());
        holder.tvQuestion.setText(""+answerData.getQuestion());
        holder.btUserAmswer.setText(""+answerData.getUserAnswer());
        holder.btCorrectAnswer.setText(""+answerData.getCorrAnswer());

        if (answerData.getUserAnswer().equals(answerData.getCorrAnswer())){
            holder.btUserAmswer.setBackgroundResource(R.drawable.buttons2);
            holder.btCorrectAnswer.setVisibility(View.INVISIBLE);

        }
        else {
            holder.btUserAmswer.setBackgroundResource(R.drawable.buttons4);
            holder.btCorrectAnswer.setBackgroundResource(R.drawable.buttons2);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionId,tvQuestion;
        Button btUserAmswer,btCorrectAnswer;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionId);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            btUserAmswer = itemView.findViewById(R.id.btUserAmswer);
            btCorrectAnswer = itemView.findViewById(R.id.btCorrectAnswer);

        }
    }


}
