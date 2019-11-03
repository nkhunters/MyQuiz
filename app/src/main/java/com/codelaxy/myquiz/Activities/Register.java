package com.codelaxy.myquiz.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    @BindView(R.id.userName)
    EditText userName;

    @BindView(R.id.mobile)
    EditText mobile;

    AnimationDrawable animationDrawable;

    @BindView(R.id.main_layout)
    ScrollView main_layout;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirm_pass)
    EditText confirm_pass;

    @BindView(R.id.register)
    CircularProgressButton register;

    @OnClick(R.id.have_account)
    void have_account() {
        startActivity(new Intent(Register.this, Login.class));
    }

    @OnClick(R.id.register)
    void register() {

        register.startAnimation();
        String strName = userName.getText().toString().trim();
        String strMobile = mobile.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        String strConfirmPass = confirm_pass.getText().toString().trim();

        if (TextUtils.isEmpty(strName)) {
            userName.setError("Name is required.");
            userName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strMobile)) {
            mobile.setError("Mobile No. is required");
            mobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strPassword)) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(strConfirmPass)) {
            confirm_pass.setError("Confirm password is required");
            confirm_pass.requestFocus();
            return;
        }

        if (!strPassword.equals(strConfirmPass)) {
            confirm_pass.setError("Password and confirm password didn't matched");
            confirm_pass.requestFocus();
            return;
        }

        /*Intent intent = new Intent(Register.this, EnterOtp.class);
        intent.putExtra("name", strName);
        intent.putExtra("mobile", strMobile);
        intent.putExtra("password", strPassword);
        startActivity(intent);*/
        saveData(strName, strMobile, strPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        animationDrawable = (AnimationDrawable) main_layout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
    }

    private void saveData(String strName, String strMobile, String strPassword) {

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().register(strPassword, strName, strMobile);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.code() == 201)
                {
                    SharedPrefManager.getInstance(Register.this).saveUser(strMobile);
                    SharedPrefManager.getInstance(Register.this).saveUserName(strName);
                    SharedPrefManager.getInstance(Register.this).saveMobile(strMobile);

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    register.revertAnimation(new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return null;
                        }
                    });
                    Intent intent = new Intent(Register.this, Splash.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                }
                else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    register.revertAnimation(new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return null;
                        }
                    });
                    Snackbar.make(findViewById(R.id.main_layout), "Invalid username or password", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                register.revertAnimation(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        return null;
                    }
                });
                Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
            }
        });

        /*Single<DefaultResponse> registerCall = RetrofitClient.getInstance().getApi().register(strPassword, strName, strMobile);
        registerCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        if()
                        {
                            SharedPrefManager.getInstance(Register.this).saveUser(strMobile);
                            SharedPrefManager.getInstance(Register.this).saveUserName(strName);
                            SharedPrefManager.getInstance(Register.this).saveMobile(strMobile);

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            register.revertAnimation(new Function0<Unit>() {
                                @Override
                                public Unit invoke() {
                                    return null;
                                }
                            });
                            Intent intent = new Intent(Register.this, Splash.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            register.revertAnimation(new Function0<Unit>() {
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

                        register.revertAnimation(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });
                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
*/    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }
}
