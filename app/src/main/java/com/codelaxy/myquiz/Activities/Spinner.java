package com.codelaxy.myquiz.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Spinner extends AppCompatActivity {

    private static final String[] sectors = {"10", "20", "25", "30",
            "35", "0", "40", "45", "50", "55", "60", "5"};
    private static final Random RANDOM = new Random();
    private static final float HALF_SECTOR = 360f / 12f / 2f;
    @BindView(R.id.spinBtn)
    Button spinBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;
    @BindView(R.id.wheel)
    ImageView wheel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;


    private int degree = 0, degreeOld = 0;
    private String uid;

    private int attempt, spinAmmount, wallet;

    private String point;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.spinBtn)
    public void spin(View v) {

        boolean spin_available = SharedPrefManager.getInstance(Spinner.this).isSpinAvailable();
        if(!spin_available)
        {
            Snackbar.make(coordinatorlayout, "No Spins Available. Play Quiz to get more Spins",Snackbar.LENGTH_LONG).show();
        }
        else {

            degreeOld = degree % 360;
            // we calculate random angle for rotation of our wheel
            degree = RANDOM.nextInt(360) + 720;
            // rotation effect on the center of the wheel
            RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(3600);
            rotateAnim.setFillAfter(true);
            rotateAnim.setInterpolator(new DecelerateInterpolator());
            rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // we empty the result text view when the animation start
                    resultTv.setText("");

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // we display the correct sector pointed by the triangle at the end of the rotate animation
                    try {
                        String points = getSector(360 - (degree % 360));
                        updateRewards(points);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // we start the animation
            wheel.startAnimation(rotateAnim);

        }
    }

    private void updateRewards(String points) {

        Log.d("niraj", points);
        String str_email = SharedPrefManager.getInstance(Spinner.this).getUser();
        Single<DefaultResponse> updateRewards = RetrofitClient.getInstance().getApi().updateRewardCount(str_email, Integer.parseInt(points));
        updateRewards.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        if (!defaultResponse.getError()) {
                            if (points.equalsIgnoreCase("0")) {
                                resultTv.setText("Better luck next time");
                            } else {
                                resultTv.setText("You earned " + points + " rewards points");
                            }

                            SharedPrefManager.getInstance(Spinner.this).updateSpin(false);
                        }
                        else Snackbar.make(coordinatorlayout, "Something went wrong. Please check your internet.", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Snackbar.make(coordinatorlayout, "Something went wrong. Please check your internet.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private String getSector(int degrees) {
        int i = 0;
        String text = null;

        do {
            // start and end of each sector on the wheel
            float start = HALF_SECTOR * (i * 2 + 1);
            float end = HALF_SECTOR * (i * 2 + 3);

            if (degrees >= start && degrees < end) {

                text = sectors[i];

            }

            i++;
        } while (text == null && i < sectors.length);

        return text;
    }
}
