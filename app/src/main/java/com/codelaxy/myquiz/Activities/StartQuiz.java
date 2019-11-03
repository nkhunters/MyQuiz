package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.Question;
import com.codelaxy.myquiz.Models.QuestionResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerStates;
import io.github.krtkush.lineartimer.LinearTimerView;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartQuiz extends AppCompatActivity implements LinearTimer.TimerListener {

    String quiz_id, quiz_level, quiz_time, reward_points;
    int position = 0;
    ArrayList<Question> questionArrayList;

    ArrayList<Integer> rightAnswers = new ArrayList<>();

    @BindView(R.id.main_layout)
    ScrollView main_layout;

    @BindView(R.id.timer)
    RelativeLayout timer;

    @BindView(R.id.options)
    LinearLayout options;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.linearTimer)
    LinearTimerView linearTimerView;
    LinearTimer linearTimer;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.question)
    TextView txt_question;

    @BindView(R.id.question_no)
    TextView question_no;

    @BindView(R.id.option1)
    Button option1;

    @BindView(R.id.option2)
    Button option2;

    @BindView(R.id.option3)
    Button option3;

    @BindView(R.id.option4)
    Button option4;

    @OnClick(R.id.option1)
    void option1() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Question question = questionArrayList.get(position);

        if (question.getAnswer().equals("option1")) {
            Toasty.success(StartQuiz.this, "Right Answer", Toast.LENGTH_SHORT, true).show();
            //option1.setBackground(getResources().getDrawable(R.drawable.button_background_green));
            rightAnswers.add(position);
        } else {
            //option1.setBackground(getResources().getDrawable(R.drawable.button_background_red));
            Toasty.error(StartQuiz.this, "Wrong Answer", Toast.LENGTH_SHORT, true).show();
        }

        position++;
        setQuestionAndStarQuiz();

    }

    @OnClick(R.id.option2)
    void option2() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Question question = questionArrayList.get(position);

        if (question.getAnswer().equals("option2")) {
            Toasty.success(StartQuiz.this, "Right Answer", Toast.LENGTH_SHORT, true).show();
            //option2.setBackground(getResources().getDrawable(R.drawable.button_background_green));
            rightAnswers.add(position);
        } else {
            //option2.setBackground(getResources().getDrawable(R.drawable.button_background_red));
            Toasty.error(StartQuiz.this, "Wrong Answer", Toast.LENGTH_SHORT, true).show();
        }

        position++;
        setQuestionAndStarQuiz();
    }

    @OnClick(R.id.option3)
    void option3() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Question question = questionArrayList.get(position);

        if (question.getAnswer().equals("option3")) {
            Toasty.success(StartQuiz.this, "Right Answer", Toast.LENGTH_SHORT, true).show();
            //option3.setBackground(getResources().getDrawable(R.drawable.button_background_green));
            rightAnswers.add(position);
        } else {
            //option3.setBackground(getResources().getDrawable(R.drawable.button_background_red));
            Toasty.error(StartQuiz.this, "Wrong Answer", Toast.LENGTH_SHORT, true).show();
        }

        position++;
        setQuestionAndStarQuiz();
    }

    @OnClick(R.id.option4)
    void option4() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Question question = questionArrayList.get(position);

        if (question.getAnswer().equals("option4")) {
            Toasty.success(StartQuiz.this, "Right Answer", Toast.LENGTH_SHORT, true).show();
            //option4.setBackground(getResources().getDrawable(R.drawable.button_background_green));
            rightAnswers.add(position);
        } else {
            //option4.setBackground(getResources().getDrawable(R.drawable.button_background_red));
            Toasty.error(StartQuiz.this, "Wrong Answer", Toast.LENGTH_SHORT, true).show();
        }

        position++;
        setQuestionAndStarQuiz();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        ButterKnife.bind(this);

        quiz_id = getIntent().getStringExtra("quiz_id");
        quiz_level = getIntent().getStringExtra("quiz_level");
        quiz_time = getIntent().getStringExtra("quiz_time");
        reward_points = getIntent().getStringExtra("reward_points");

        linearTimer = new LinearTimer.Builder()
                .linearTimerView(linearTimerView)
                .duration(Integer.parseInt(quiz_time) * 1000)
                .timerListener(this)
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                .build();

        //linearTimer.startTimer();

        getQuestions();
    }

    private void getQuestions() {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Single<QuestionResponse> questions = RetrofitClient.getInstance().getApi().getQuestions(quiz_id);
        questions.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<QuestionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(QuestionResponse questionResponse) {

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);

                        questionArrayList = questionResponse.getQuestions();
                        if (!questionArrayList.isEmpty()) {

                            setQuestionAndStarQuiz();
                        } else
                            Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void setQuestionAndStarQuiz() {

        if (position == questionArrayList.size() - 1) {
            submitQuiz();
        }

        if (position < questionArrayList.size()) {
            option1.setBackground(getResources().getDrawable(R.drawable.button_background_white));
            option2.setBackground(getResources().getDrawable(R.drawable.button_background_white));
            option3.setBackground(getResources().getDrawable(R.drawable.button_background_white));
            option4.setBackground(getResources().getDrawable(R.drawable.button_background_white));

            Question question = questionArrayList.get(position);
            if (question != null) {
                question_no.setText(position + 1 + ".");
                txt_question.setText(question.getQuestion());
                option1.setText(question.getOption1());
                option2.setText(question.getOption2());
                option3.setText(question.getOption3());
                option4.setText(question.getOption4());

                options.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                timer.setVisibility(View.VISIBLE);
                //linearTimer.startTimer();
                if (linearTimer.getState() == LinearTimerStates.ACTIVE) {
                    linearTimer.pauseTimer();
                    linearTimer.restartTimer();
                } else if (linearTimer.getState() == LinearTimerStates.INITIALIZED)
                    linearTimer.startTimer();
                else {
                    linearTimer = new LinearTimer.Builder()
                            .linearTimerView(linearTimerView)
                            .duration(Integer.parseInt(quiz_time) * 1000)
                            .timerListener(this)
                            .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                            .build();

                    linearTimer.startTimer();
                }
            }
        }

    }

    private void submitQuiz() {

        if (linearTimer.getState() == LinearTimerStates.ACTIVE) {
            linearTimer.pauseTimer();
        }

        SharedPrefManager.getInstance(StartQuiz.this).updateSpin(true);

        Intent intent = new Intent(StartQuiz.this, CompleteQuiz.class);
        intent.putExtra("reward_points", reward_points);
        intent.putExtra("right_count", rightAnswers.size());
        finish();
        startActivity(intent);
    }

    @Override
    public void animationComplete() {

    }

    @Override
    public void timerTick(long tickUpdateInMillis) {

        Log.i("Time left", String.valueOf(tickUpdateInMillis));

        String formattedTime = String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(tickUpdateInMillis)
                        - TimeUnit.MINUTES
                        .toSeconds(TimeUnit.MILLISECONDS.toHours(tickUpdateInMillis)));

        time.setText(formattedTime);

        if (String.valueOf(tickUpdateInMillis).equals("0")) {
            position++;
            setQuestionAndStarQuiz();
        }
    }

    @Override
    public void onTimerReset() {


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
