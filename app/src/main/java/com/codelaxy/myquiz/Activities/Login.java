package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

public class Login extends AppCompatActivity {

    AnimationDrawable animationDrawable;

    @BindView(R.id.main_layout)
    CoordinatorLayout main_layout;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    CircularProgressButton login;

    /*@BindView(R.id.google_sign_in_button)
    SignInButton google_sign_in_button;*/

    @OnClick(R.id.no_account)
    void no_account(){
        startActivity(new Intent(Login.this, Register.class));
    }

    @OnClick(R.id.login)
    void login()
    {

        String strMobile = mobile.getText().toString().trim();
        String strPassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(strMobile))
        {
            mobile.setError("Mobile No. is required");
            mobile.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(strPassword)){

            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        login.startAnimation();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Single<DefaultResponse> loginCall = RetrofitClient.getInstance().getApi().login(strMobile, strPassword);
        loginCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        if(!defaultResponse.getError())
                        {
                            String name = defaultResponse.getMessage();
                            SharedPrefManager.getInstance(Login.this).saveUser(strMobile);
                            SharedPrefManager.getInstance(Login.this).saveUserName(name);

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            login.revertAnimation(new Function0<Unit>() {
                                @Override
                                public Unit invoke() {
                                    return null;
                                }
                            });

                            Intent intent = new Intent(Login.this, Splash.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            login.revertAnimation(new Function0<Unit>() {
                                @Override
                                public Unit invoke() {
                                    return null;
                                }
                            });

                            Snackbar.make(findViewById(R.id.main_layout), defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        login.revertAnimation(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });

                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        animationDrawable =(AnimationDrawable)main_layout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mobile = SharedPrefManager.getInstance(Login.this).getUser();
        if(mobile != null)
        {
            finish();
            startActivity(new Intent(Login.this, Splash.class));
        }
    }
}
