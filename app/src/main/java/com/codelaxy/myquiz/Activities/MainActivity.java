package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codelaxy.myquiz.Adapters.QuizAdapter;
import com.codelaxy.myquiz.Adapters.ViewPagerAdapter;
import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.Quiz;
import com.codelaxy.myquiz.Models.QuizResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView[] dots;
    ArrayList<Quiz> dataList;

    @OnClick(R.id.menu_btn)
    void openDrawer()
    {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.SliderDots)
    LinearLayout sliderDotsPanel;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav)
    NavigationView navigationView;

    @BindView(R.id.quizRecyclerView)
    RecyclerView quizRecyclerView;

    private ActionBarDrawerToggle togle;
    View headerView;
    TextView username, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setViewPagerAdapter();

        togle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(togle);
        togle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        headerView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        username = headerView.findViewById(R.id.username);
        user_email = headerView.findViewById(R.id.user_email);

        username.setText(SharedPrefManager.getInstance(MainActivity.this).getUserName());
        user_email.setText(SharedPrefManager.getInstance(MainActivity.this).getUser());

        setRecyclerView();
    }

    private void setRecyclerView() {

        Single<QuizResponse> quizResponseSingle = RetrofitClient.getInstance().getApi().getQuizes();
        quizResponseSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<QuizResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(QuizResponse quizResponse) {

                        dataList = quizResponse.getQuizes();
                        if(!dataList.isEmpty()){

                            quizRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            QuizAdapter adapter = new QuizAdapter(MainActivity.this, dataList);
                            quizRecyclerView.setAdapter(adapter);
                        }

                        else {
                            Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void setViewPagerAdapter() {
        ArrayList<Integer> bannerList = new ArrayList<>();
        bannerList.add(R.drawable.app_banner1);
        bannerList.add(R.drawable.banner2);

        ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity.this, bannerList);
        viewPager.setAdapter(adapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 3000);

        int dotscount = adapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);
        }
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.total_rupees:
                Intent cashout = new Intent(MainActivity.this, Wallet.class);
                startActivity(cashout);
                break;

            case R.id.change_password:
            Intent changePwd = new Intent(MainActivity.this, EditProfile.class);
            startActivity(changePwd);
            break;

            case R.id.view_history:
                Intent intent_history = new Intent(MainActivity.this, History.class);
                startActivity(intent_history);
                break;

            case R.id.logout:
                SharedPrefManager.getInstance(MainActivity.this).clear();
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;

            /*case R.id.privacyPolicy:
                String url = "http://apps.teenpattitycoon.com/app/privacy-policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.termsCondition:
                String url1 = "http://apps.teenpattitycoon.com/app/term-conditions";
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(url1));
                startActivity(i1);
                break;*/
        }
        return true;
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (viewPager.getCurrentItem() < dots.length - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

}
