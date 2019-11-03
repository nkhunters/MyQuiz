package com.codelaxy.myquiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codelaxy.myquiz.Activities.MainActivity;
import com.codelaxy.myquiz.Activities.StartQuiz;
import com.codelaxy.myquiz.Models.Quiz;
import com.codelaxy.myquiz.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context mContext;
    private ArrayList<Quiz> dataList;

    public QuizAdapter(Context mContext, ArrayList<Quiz> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.QuizViewHolder holder, int position) {

        Quiz quiz = dataList.get(position);

        holder.quizname.setText(quiz.getQuiz_name());
        holder.quizlevel.setText(quiz.getLevel());
        holder.reward_points.setText(quiz.getReward()+" Reward points");

        holder.playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, StartQuiz.class);
                intent.putExtra("quiz_id", quiz.getId());
                intent.putExtra("quiz_level", quiz.getLevel());
                intent.putExtra("quiz_time", quiz.getTime());
                intent.putExtra("reward_points", quiz.getReward());

                ((MainActivity)mContext).finish();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.quizname)
        TextView quizname;

        @BindView(R.id.quizlevel)
        TextView quizlevel;

        @BindView(R.id.reward_points)
        TextView reward_points;

        @BindView(R.id.playbtn)
        TextView playbtn;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
