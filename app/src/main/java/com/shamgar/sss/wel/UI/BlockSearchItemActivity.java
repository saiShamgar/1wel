package com.shamgar.sss.wel.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.shamgar.sss.wel.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Status;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlockSearchItemActivity extends AppCompatActivity implements View.OnClickListener{

    //verification variables
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    private Button submit,verify;
    private EditText reg_otp,reg_num;

    private ApiService apiService;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_search_item);

        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);

        submit=(Button)findViewById(R.id.submit);
        reg_otp=(EditText)findViewById(R.id.reg_otp);
        reg_num=(EditText)findViewById(R.id.reg_num);
        verify=(Button)findViewById(R.id.verify);

        user_id=getIntent().getExtras().getString("user_id");

       // Toast.makeText(getApplicationContext(),user_id,Toast.LENGTH_SHORT).show();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Deleting Provider");

        submit.setOnClickListener(this);
        verify.setOnClickListener(this);

    }
    @Override
    public void onClick(View v)
    {
        if (v.getId()==R.id.submit){

            loadingbar.setTitle("User Verification");
            loadingbar.setMessage("please wait,while we are verifying with your registered number");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            apiService= APIUrl.getApiClient().create(ApiService.class);
            Call<Status> call=apiService.checkProvider(user_id,reg_num.getText().toString());
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body()==null){
                        loadingbar.dismiss();
                        Toast.makeText(getApplicationContext(),"response null",Toast.LENGTH_LONG).show();
                        return;
                    }
                    loadingbar.dismiss();

                    if (TextUtils.equals(response.body().getMessage(),"verify successfully.")){
                        loadingbar.setTitle("Mobile Verification");
                        loadingbar.setMessage("please wait,while we are authenticating with your phone");
                        loadingbar.setCanceledOnTouchOutside(false);
                        loadingbar.show();
                        String phoneNum=reg_num.getText().toString();
                       PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNum,
                        60,
                        TimeUnit.SECONDS,
                        BlockSearchItemActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e)
                            {
                                loadingbar.dismiss();
                                reg_num.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.VISIBLE);

                                reg_otp.setVisibility(View.GONE);
                                verify.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(),"Invalid phone number, please enter valid number",Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(String verificationId,
                                                   PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number, we
                                // now need to ask the user to enter the code and then construct a credential
                                // by combining the code with a verification ID.
                                mVerificationId = verificationId;
                                mResendToken = token;
                                loadingbar.dismiss();

                                Toast.makeText(getApplicationContext(),"code sent to the "+reg_num.getText(),Toast.LENGTH_LONG).show();

                                reg_num.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);

                                reg_otp.setVisibility(View.VISIBLE);
                                verify.setVisibility(View.VISIBLE);

                            }
                        });       // OnVerificationStateChangedCallbacks

                    }
                    Toast.makeText(getApplicationContext(),"response "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(),"something went wrong ",Toast.LENGTH_LONG).show();

                }
            });

//
//
        }
        if (v.getId()==R.id.verify){

            loadingbar.setTitle("Phone verification");
            loadingbar.setMessage("please wait,while we are authenticating with your phone");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, reg_otp.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();

                           // loadingbar=new ProgressDialog(getApplicationContext());
                            loadingbar.setTitle("Deleting Provider");
                            loadingbar.setMessage("please wait...,");
                            loadingbar.setCanceledOnTouchOutside(false);
                            loadingbar.show();

                            apiService= APIUrl.getApiClient().create(ApiService.class);
                            Call<Status> call=apiService.deleteprovider(user_id,reg_num.getText().toString());
                            call.enqueue(new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                    if (response.body()==null){
                                        loadingbar.dismiss();
                                        Toast.makeText(getApplicationContext(),"response null",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    loadingbar.dismiss();
                                    Toast.makeText(getApplicationContext(),"response "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                                    Intent payment=new Intent(BlockSearchItemActivity.this, MainActivity.class);
                                    startActivity(payment);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {
                                    loadingbar.dismiss();

                                }
                            });

                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            loadingbar.dismiss();
                        }
                    }
                });
    }
}
