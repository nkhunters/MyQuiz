package com.codelaxy.myquiz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codelaxy.myquiz.Adapters.HistoryAdapter;
import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.HistoryResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class History extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String str_email;

    @OnClick(R.id.menu_btn)
    void openDrawer()
    {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav)
    NavigationView navigationView;

    private ActionBarDrawerToggle togle;
    View headerView;
    TextView username, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        togle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(togle);
        togle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        headerView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        username = headerView.findViewById(R.id.username);
        user_email = headerView.findViewById(R.id.user_email);

        str_email = SharedPrefManager.getInstance(History.this).getUser();

        username.setText(SharedPrefManager.getInstance(History.this).getUserName());
        user_email.setText(str_email);

        setRecyclerView();
    }

    private void setRecyclerView() {

        ProgressDialog progressDialog = new ProgressDialog(History.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Single<HistoryResponse> historyResponseSingle = RetrofitClient.getInstance().getApi().getHistory(str_email);
        historyResponseSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HistoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(HistoryResponse historyResponse) {

                        progressDialog.cancel();
                        if(!historyResponse.getError())
                        {
                            ArrayList<com.codelaxy.myquiz.Models.History> dataList = historyResponse.getReward_history();
                            if(!dataList.isEmpty()){

                                recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
                                HistoryAdapter adapter = new HistoryAdapter(History.this, dataList);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                Snackbar.make(findViewById(R.id.main_layout), "You haven't made any Withdraw request yet", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                            Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please wait your internet.", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                        progressDialog.cancel();
                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please wait your internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.total_rupees:
                Intent cashout = new Intent(History.this, Wallet.class);
                startActivity(cashout);
                break;

            case R.id.change_password:
                Intent changePwd = new Intent(History.this, EditProfile.class);
                startActivity(changePwd);
                break;

            case R.id.logout:
                SharedPrefManager.getInstance(History.this).clear();
                Intent intent = new Intent(History.this, Login.class);
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
}
