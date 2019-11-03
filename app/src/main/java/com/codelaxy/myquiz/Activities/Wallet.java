package com.codelaxy.myquiz.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Wallet extends AppCompatActivity {

    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.cash_amount)
    TextView cash_amount;

    @BindView(R.id.withdraw_amt)
    TextView withdraw_amt;

    @OnClick(R.id.withdraw_amt)
    void showWithdrawDialog() {

        final Dialog dialog = new Dialog(Wallet.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.withdraw_dialog);
        dialog.setCancelable(true);
        dialog.show();

        EditText withdraw_amt = dialog.findViewById(R.id.withdraw_amt);
        Button sendRequest = dialog.findViewById(R.id.send_request);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                String str_withdraw_amt = withdraw_amt.getText().toString().trim();

                ProgressDialog progressDialog = new ProgressDialog(Wallet.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Single<DefaultResponse> createRequest = RetrofitClient.getInstance().getApi().createWithdrawRequest(str_email, str_user_name, str_withdraw_amt);
                createRequest.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<DefaultResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(DefaultResponse defaultResponse) {

                                progressDialog.cancel();
                                if(!defaultResponse.getError()){
                                    Toasty.success(Wallet.this, "Request Sent", Toasty.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(Wallet.this, MainActivity.class));
                                }
                                else
                                    Toasty.error(Wallet.this, "Request not Sent", Toasty.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                                progressDialog.cancel();
                                Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your internet", Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    String str_email;
    String str_user_name;
    int total_rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);

        str_user_name = SharedPrefManager.getInstance(Wallet.this).getUserName();
        str_email = SharedPrefManager.getInstance(Wallet.this).getUser();

        user_name.setText(str_user_name);
        email.setText(str_email);

        withdraw_amt.setEnabled(false);

        getUserRewards();
    }

    private void getUserRewards() {

        Single<DefaultResponse> rewardsSingle = RetrofitClient.getInstance().getApi().getUserRewards(str_email);
        rewardsSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        if (!defaultResponse.getError()) {
                            cash_amount.setText(defaultResponse.getMessage());
                            total_rewards = Integer.parseInt(defaultResponse.getMessage());
                            if (total_rewards >= 100)
                                withdraw_amt.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}
