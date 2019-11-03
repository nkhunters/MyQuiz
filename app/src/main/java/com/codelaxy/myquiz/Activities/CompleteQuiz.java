package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class CompleteQuiz extends AppCompatActivity {

    @BindView(R.id.congra)
    TextView quiz_complete_text;

    @BindView(R.id.continueBtn)
    CircularProgressButton continueBtn;

    @OnClick(R.id.continueBtn)
    void updateRewards() {

        continueBtn.startAnimation();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String email = SharedPrefManager.getInstance(CompleteQuiz.this).getUser();
        Single<DefaultResponse> updateRewards = RetrofitClient.getInstance().getApi().updateRewardCount(email, total_rewards);
        updateRewards.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        continueBtn.revertAnimation(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });

                        finish();
                        startActivity(new Intent(CompleteQuiz.this, Splash.class));
                    }

                    @Override
                    public void onError(Throwable e) {

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        continueBtn.revertAnimation(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });

                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong! Please check your internet", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    String reward_points;
    int right_count;
    int total_rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_quiz);
        ButterKnife.bind(this);

        reward_points = getIntent().getStringExtra("reward_points");
        right_count = getIntent().getIntExtra("right_count", 0);
        total_rewards = Integer.parseInt(reward_points) * right_count;

        quiz_complete_text.setText("Congratulations! Quiz completed, " + total_rewards + " reward points added to your wallet");
    }
}
