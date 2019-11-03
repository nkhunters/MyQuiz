package com.codelaxy.myquiz.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.codelaxy.myquiz.Api.RetrofitClient;
import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.R;
import com.codelaxy.myquiz.SharedPrefrences.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

public class EditProfile extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.old_password)
    EditText old_password;

    @BindView(R.id.new_password)
    EditText new_password;

    @BindView(R.id.confirm_password)
    EditText confirm_password;

    @OnClick(R.id.change_pass_submit)
    void submit(){

        String str_old_password = old_password.getText().toString().trim();
        String str_new_password = new_password.getText().toString().trim();
        String str_confirm_password = confirm_password.getText().toString().trim();

        if(TextUtils.isEmpty(str_old_password))
        {
            old_password.setError("Old Password is required");
            old_password.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(str_new_password))
        {
            new_password.setError("New Password is required");
            new_password.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(str_confirm_password))
        {
            confirm_password.setError("Confirm Password is required");
            confirm_password.requestFocus();
            return;
        }
        if(!str_new_password.equals(str_confirm_password))
        {
            confirm_password.setError("Passwords didn't matched");
            confirm_password.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Single<DefaultResponse> changePwd = RetrofitClient.getInstance().getApi().changePassword(str_old_password, str_new_password, str_mobile);
        changePwd.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {

                        progressDialog.cancel();
                        if(defaultResponse.getMessage() != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                            builder.setMessage(defaultResponse.getMessage());
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        progressDialog.cancel();
                        Snackbar.make(findViewById(R.id.main_layout), "Something went wrong. Please check your internet", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    String str_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        str_mobile = SharedPrefManager.getInstance(EditProfile.this).getUser();

    }
}
