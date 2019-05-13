package com.shamgar.sss.wel.UI.providers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.shamgar.sss.wel.R;
import com.shamgar.sss.wel.UI.MainActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class ProviderSignUpImage extends AppCompatActivity implements View.OnClickListener {

    //verification variables
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    private SharedPreferenceConfig sharedPreference;

    //camera images
    private static final int PICK_UP_CAM =1 ;
    private static final int PICK_FROM_FILE =2 ;
    boolean boolean_permission;
    public static final int REQUEST_PERMISSIONS = 1;
    private Uri imagecaptureuri;
    private AlertDialog dialog;
    private Bitmap bmp;

    private RadioButton provider_radio_btn_male,provider_radio_btn_female,provider_radio_btn_others;
    private RadioGroup radioGroupProvider;
    private Button upload_image_provider,provider_reg_submit_btn,provider_verification_btn;
    private ImageView provider_image;
    private TextView txt_otp_hint_provider;
    private EditText provider_signup_virification;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_sign_up_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Provider SignUp");
        mAuth = FirebaseAuth.getInstance();
        sharedPreference = new SharedPreferenceConfig(this);

        ActivityCompat.requestPermissions(ProviderSignUpImage.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        upload_image_provider = findViewById(R.id.upload_image_provider);
        provider_reg_submit_btn = findViewById(R.id.provider_reg_submit_btn);
        provider_verification_btn = findViewById(R.id.provider_verification_btn);
        provider_signup_virification = findViewById(R.id.provider_signup_virification);
        provider_image = findViewById(R.id.provider_image);

        //intializing Textview
        txt_otp_hint_provider = findViewById(R.id.txt_otp_hint_provider);


        //initialize  RadioButtons
        provider_radio_btn_male = findViewById(R.id.provider_radio_btn_male);
        provider_radio_btn_female = findViewById(R.id.provider_radio_btn_female);
        radioGroupProvider = findViewById(R.id.radioGroupProvider);
        provider_radio_btn_others = findViewById(R.id.provider_radio_btn_others);

        upload_image_provider.setOnClickListener(this);
        provider_reg_submit_btn.setOnClickListener(this);
        provider_verification_btn.setOnClickListener(this);

        //getting images
        final String[] items = new String[]{"from camera", "from sd card"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    try {
                        captureImage();
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, PICK_UP_CAM);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (i == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "select file"), PICK_FROM_FILE);
                }
                dialogInterface.cancel();
            }
        });
        dialog = builder.create();

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.provider_reg_submit_btn){
            if (!(provider_radio_btn_male.isChecked() || provider_radio_btn_female.isChecked() || provider_radio_btn_others.isChecked())){
                Toast.makeText(getApplicationContext(),"select your gender",Toast.LENGTH_LONG).show();
                return;
            }
            if (provider_radio_btn_male.isChecked())
                sharedPreference.writeProviderGender("male");
            if (provider_radio_btn_female.isChecked())
                sharedPreference.writeProviderGender("female");
            if (provider_radio_btn_others.isChecked())
                sharedPreference.writeProviderGender("others");

            loadingbar=new ProgressDialog(this);
            //sending verification code

            String phoneNumber=sharedPreference.readProviderPhone();
            loadingbar.setTitle("Phone verification");
            loadingbar.setMessage("please wait,while we are authenticating with your phone");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            loadingbar.dismiss();
                            Toast.makeText(getApplicationContext(),"Invalid phone number, please enter valid number",Toast.LENGTH_LONG).show();

                            radioGroupProvider.setVisibility(View.VISIBLE);
                            provider_image.setVisibility(View.VISIBLE);
                            upload_image_provider.setVisibility(View.VISIBLE);
                            provider_reg_submit_btn.setVisibility(View.VISIBLE);
                            provider_reg_submit_btn.setText("Re-submit");


                            txt_otp_hint_provider.setVisibility(View.GONE);
                            provider_verification_btn.setVisibility(View.GONE);
                            provider_signup_virification.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                            super.onCodeSent(verificationId, token);
                            // The SMS verification code has been sent to the provided phone number, we
                            // now need to ask the user to enter the code and then construct a credential
                            // by combining the code with a verification ID.
                            mVerificationId = verificationId;
                            mResendToken = token;
                            loadingbar.dismiss();
                            //Toast.makeText(getApplicationContext(),"code sent to the "+provider_signup_phone.getText(),Toast.LENGTH_LONG).show();

                            radioGroupProvider.setVisibility(View.GONE);
                            provider_image.setVisibility(View.GONE);
                            upload_image_provider.setVisibility(View.GONE);
                            provider_reg_submit_btn.setVisibility(View.GONE);


                            txt_otp_hint_provider.setVisibility(View.VISIBLE);
                            provider_verification_btn.setVisibility(View.VISIBLE);
                            provider_signup_virification.setVisibility(View.VISIBLE);
                        }
                    });       // OnVerificationStateChangedCallbacks
        }
        if (v.getId()==R.id.upload_image_provider){
            dialog.show();
        }
        if (v.getId()==R.id.provider_verification_btn){

            loadingbar.setTitle("Phone verification");
            loadingbar.setMessage("please wait,while we are authenticating with your phone");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            String code = provider_signup_virification.getText().toString().trim();
            if(mVerificationId != null && code!= null ){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }else {
                Toast.makeText(this, ""+code+mVerificationId, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                           loadingbar.dismiss();
                           Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                            loadingbar.setTitle("Please Wait");
                            loadingbar.setMessage("please wait,while we are registering your details..");
                            loadingbar.setCanceledOnTouchOutside(false);
                            loadingbar.show();

                            apiService= APIUrl.getApiClient().create(ApiService.class);
                            Call<Status> call=apiService.ProviderRegistration(
                                    sharedPreference.readProviderName(),
                                    sharedPreference.readProviderPhone(),
                                    sharedPreference.readProviderLatitude(),
                                    sharedPreference.readProviderLongitude(),
                                    sharedPreference.readProviderGender(),
                                    imageToString(bmp),
                                    sharedPreference.readProviderLocation(),
                                    sharedPreference.readProviderRefID(),
                                    sharedPreference.readProviderDiscount(),
                                    sharedPreference.readProviderStatus(),
                                    sharedPreference.readProviderWebsite(),
                                    sharedPreference.readProviderService(),
                                    sharedPreference.readProviderLandmark(),
                                    sharedPreference.readProviderServiceDesc());
                            //   providerServiceDescription.getText().toString());
                            call.enqueue(new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                                    if (response.body()==null)
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(getApplicationContext(), "some error was occured/please register again", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    loadingbar.dismiss();
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent agentLogin = new Intent(ProviderSignUpImage.this, MainActivity.class);
                                    agentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(agentLogin);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {
                                    loadingbar.dismiss();
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                                }
                            });

                            //sendUserToMainActivity();


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                loadingbar.dismiss();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    //image uploading code
    private void captureImage()   {
        if( ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        PICK_UP_CAM);
            }
            else {
                // Open your camera here.
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode==PICK_UP_CAM) {
                Bundle bundle=data.getExtras();
                bmp=(Bitmap)bundle.get("data");
                provider_image.setImageBitmap(bmp);
                sharedPreference.writeProviderPic(imageToString(bmp));
            }
            else if(requestCode==PICK_FROM_FILE) {
                imagecaptureuri=data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagecaptureuri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(imagecaptureuri)
                        .into(provider_image);
                sharedPreference.writeProviderPic(imageToString(bmp));
            }

        }

    }

    //upload images
    public String  imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }
}
