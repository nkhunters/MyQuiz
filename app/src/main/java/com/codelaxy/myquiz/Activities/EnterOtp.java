package com.codelaxy.myquiz.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class EnterOtp extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.enter_mobile_no_text)
    TextView enter_mobile_no_text;

    @BindView(R.id.go_back_btn)
    ImageView go_back_btn;

    @BindView(R.id.otp1)
    EditText otp1;

    @BindView(R.id.otp2)
    EditText otp2;

    @BindView(R.id.otp3)
    EditText otp3;

    @BindView(R.id.otp4)
    EditText otp4;

    @BindView(R.id.otp5)
    EditText otp5;

    @BindView(R.id.otp6)
    EditText otp6;

    @BindView(R.id.submit_otp)
    CircularProgressButton submit_otp;

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = "niraj";
    String mVerificationId;

    String str_name, str_mobile, str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        ButterKnife.bind(this);

        str_name = getIntent().getStringExtra("name");
        str_password = getIntent().getStringExtra("password");
        str_mobile = getIntent().getStringExtra("mobile");

        //saveData();

        /*enter_mobile_no_text.setText("Enter the 6-digit code sent to you at " + str_mobile + ".");

        mAuth = FirebaseAuth.getInstance();

        go_back_btn.setOnClickListener(this);
        submit_otp.setOnClickListener(this);
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1)
                    otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1)
                    otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1)
                    otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1)
                    otp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1)
                    otp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addCallBacks();
        sendOtp();
*/    }

    private void addCallBacks() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                String code = credential.getSmsCode();
                if(code != null) {
                    otp1.setText(code.charAt(0) + "");
                    otp2.setText(code.charAt(1) + "");
                    otp3.setText(code.charAt(2) + "");
                    otp4.setText(code.charAt(3) + "");
                    otp5.setText(code.charAt(4) + "");
                    otp6.setText(code.charAt(5) + "");

                    signInWithPhoneAuthCredential(credential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if (user != null)
                            {
                                //saveData();
                            }
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                AlertDialog dialog = new AlertDialog.Builder(EnterOtp.this)
                                        .setMessage("The verification code entered was invalid")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                otp1.setText("");
                                                otp2.setText("");
                                                otp3.setText("");
                                                otp4.setText("");
                                                otp5.setText("");
                                                otp6.setText("");
                                                otp1.requestFocus();
                                            }
                                        }).show();
                            }
                        }
                    }
                });
    }

    /*private void saveData() {

        Single<DefaultResponse> registerCall = RetrofitClient.getInstance().getApi().register(str_password, str_name, str_mobile);
        registerCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        if(!defaultResponse.getError())
                        {
                            SharedPrefManager.getInstance(EnterOtp.this).saveUser(str_mobile);
                            SharedPrefManager.getInstance(EnterOtp.this).saveUserName(str_name);
                            SharedPrefManager.getInstance(EnterOtp.this).saveMobile(str_mobile);

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            submit_otp.revertAnimation(new Function0<Unit>() {
                                @Override
                                public Unit invoke() {
                                    return null;
                                }
                            });

                            Intent intent = new Intent(EnterOtp.this, Splash.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            submit_otp.revertAnimation(new Function0<Unit>() {
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
                        submit_otp.revertAnimation(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });

                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your Internet.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }*/

    private void sendOtp() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + str_mobile,
                1,
                TimeUnit.MINUTES,
                this,
                mCallbacks
        );


    }

    @Override
    public void onClick(View v) {

        if (v == go_back_btn)
            finish();

        if (v == submit_otp) {

            String str_otp1, str_otp2, str_otp3, str_otp4, str_otp5, str_otp6;
            str_otp1 = otp1.getText().toString().trim();
            str_otp2 = otp2.getText().toString().trim();
            str_otp3 = otp3.getText().toString().trim();
            str_otp4 = otp4.getText().toString().trim();
            str_otp5 = otp5.getText().toString().trim();
            str_otp6 = otp6.getText().toString().trim();

            if (TextUtils.isEmpty(str_otp1)) {

                otp1.setError("Enter Otp!");
                otp1.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_otp2)) {

                otp2.setError("Enter Otp!");
                otp2.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_otp3)) {

                otp3.setError("Enter Otp!");
                otp3.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_otp4)) {

                otp4.setError("Enter Otp!");
                otp4.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_otp5)) {

                otp5.setError("Enter Otp!");
                otp5.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str_otp6)) {

                otp6.setError("Enter Otp!");
                otp6.requestFocus();
                return;
            }

            submit_otp.startAnimation();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            String code = str_otp1 + str_otp2 + str_otp3 + str_otp4 + str_otp5 + str_otp6;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

}
