package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.codelaxy.myquiz.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Splash extends AppCompatActivity {

    boolean sound_on = true;

    @BindView(R.id.sound_img)
    ImageView sound_img;

    @OnClick(R.id.sound_img)
    void sound(){

        if(mp.isPlaying())
        {
            mp.pause();
            sound_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker_off));
        }
        else {

            mp.start();
            sound_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_on));
        }
    }

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;

    @OnClick(R.id.dailyrewardsbtn)
    void rewardClick(){
        //startActivity(new Intent(Splash.this, MainActivity.class));
    }

    @OnClick(R.id.playbtn)
    void playClick(){
        startActivity(new Intent(Splash.this, MainActivity.class));
    }

    @OnClick(R.id.spinbtn)
    void spinClick(){
        startActivity(new Intent(Splash.this, Spinner.class));
    }

    AnimationDrawable animationDrawable;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        animationDrawable = (AnimationDrawable) main_layout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.game_sound);
        mp.start();
        mp.setLooping(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }
}
