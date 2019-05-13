package com.shamgar.sss.wel.UI.Agent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewStructure;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Status;
import com.shamgar.sss.wel.R;
import com.shamgar.sss.wel.UI.MainActivity;
import com.shamgar.sss.wel.UI.PaymentActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class AgentSignUpImage extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    //camera images
    private static final int PICK_UP_CAM =1 ;
    private static final int PICK_FROM_FILE =2 ;
    boolean boolean_permission;
    public static final int REQUEST_PERMISSIONS = 1;
    private Uri imagecaptureuri;

    private  AlertDialog dialog;
    private Bitmap bmp;

    private RadioButton radio_btn_male,radio_btn_female,radio_btn_others;
    private RadioGroup radioGroup;

    private SharedPreferenceConfig sharedPreference;
    private ProgressDialog loadingbar;

    //verification variables
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private boolean isBoolean_permission=false;

    private ImageView agent_image;
    private Button upload_image_agent;
    private Button agent_reg_submit_btn;
    private TextView txt_otp_hint;
    private EditText agent_signup_virification;
    private Button agent_verification_btn;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_sign_up_image);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promoter Sign Up");
        mAuth=FirebaseAuth.getInstance();

        sharedPreference=new SharedPreferenceConfig(this);
        loadingbar=new ProgressDialog(this);

        //initialize  RadioButtons
        radio_btn_male=findViewById(R.id.radio_btn_male);
        radio_btn_female=findViewById(R.id.radio_btn_female);
        radioGroup=findViewById(R.id.radioGroup);
        radio_btn_others=findViewById(R.id.radio_btn_others);
        upload_image_agent=findViewById(R.id.upload_image_agent);
        agent_reg_submit_btn=findViewById(R.id.agent_reg_submit_btn);
        txt_otp_hint=findViewById(R.id.txt_otp_hint);
        agent_signup_virification=findViewById(R.id.agent_signup_virification);
        agent_verification_btn=findViewById(R.id.agent_verification_btn);
        agent_image=findViewById(R.id.agent_image);

        agent_reg_submit_btn.setOnClickListener(this);

        upload_image_agent.setOnClickListener(this);
        agent_verification_btn.setOnClickListener(this);


        ActivityCompat.requestPermissions(AgentSignUpImage.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        //getting images
        final String[]items=new String[]{"from camera","from sd card"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String >(getApplicationContext(),android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("select Image");
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0) {
                    try {
                        captureImage();
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, PICK_UP_CAM);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(i==1) {
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"select file"),PICK_FROM_FILE);
                }
                dialogInterface.cancel();
            }
        });
        dialog=builder.create();
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
    public void onClick(View v) {
        if (v.getId()==R.id.agent_reg_submit_btn){

            if (!(radio_btn_male.isChecked() || radio_btn_female.isChecked() || radio_btn_others.isChecked())){
                Toast.makeText(getApplicationContext(),"select your gender",Toast.LENGTH_LONG).show();
                return;
            }
            if (radio_btn_male.isChecked())
                sharedPreference.writeAgentGender("male");
            if (radio_btn_female.isChecked())
                sharedPreference.writeAgentGender("female");
            if (radio_btn_others.isChecked())
                sharedPreference.writeAgentGender("others");

            loadingbar=new ProgressDialog(this);
            //sending verification code

            String phoneNumber=sharedPreference.readAgentPhone();
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

                            radioGroup.setVisibility(View.VISIBLE);
                            agent_image.setVisibility(View.VISIBLE);
                            upload_image_agent.setVisibility(View.VISIBLE);
                            agent_reg_submit_btn.setVisibility(View.VISIBLE);
                            agent_reg_submit_btn.setText("Re-submit");

                            txt_otp_hint.setVisibility(View.GONE);
                            agent_signup_virification.setVisibility(View.GONE);
                            agent_verification_btn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                            // super.onCodeSent(s, forceResendingToken);
                            // The SMS verification code has been sent to the provided phone number, we
                            // now need to ask the user to enter the code and then construct a credential
                            // by combining the code with a verification ID.
                            mVerificationId = verificationId;
                            mResendToken = token;
                            loadingbar.dismiss();
                            Toast.makeText(getApplicationContext(),"code sent to the "+sharedPreference.readAgentPhone(),Toast.LENGTH_LONG).show();

                            radioGroup.setVisibility(View.GONE);
                            agent_image.setVisibility(View.GONE);
                            upload_image_agent.setVisibility(View.GONE);
                            agent_reg_submit_btn.setVisibility(View.GONE);

                            txt_otp_hint.setVisibility(View.VISIBLE);
                            agent_signup_virification.setVisibility(View.VISIBLE);
                            agent_verification_btn.setVisibility(View.VISIBLE);
                        }
                    });       // OnVerificationStateChangedCallbacks

        }

        if (v.getId()==R.id.upload_image_agent){
            dialog.show();
        }
        if (v.getId()==R.id.agent_verification_btn){

            loadingbar.setTitle("Phone verification");
            loadingbar.setMessage("please wait,while we are authenticating with your phone");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, agent_signup_virification.getText().toString());
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
                            startPayment();
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            loadingbar.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==this.RESULT_CANCELED){
            isBoolean_permission=false;
            fn_permission();
        }
        if(resultCode == Activity.RESULT_OK)
        {
            isBoolean_permission=true;
            if(requestCode==PICK_UP_CAM) {
                Bundle bundle=data.getExtras();
                bmp=(Bitmap)bundle.get("data");
                agent_image.setImageBitmap(bmp);
                sharedPreference.writeAgentPic(imageToString(bmp));
            }
            else if(requestCode==PICK_FROM_FILE) {
                imagecaptureuri=data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagecaptureuri);
                    Log.e("bitmap", bmp.toString());
                    sharedPreference.writeAgentPic(imageToString(bmp));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(imagecaptureuri)
                        .into(agent_image);
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

    //date validating

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.INTERNET))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            isBoolean_permission = true;
        }
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay");
            options.put("description", "Including all Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount",100*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "1welsupport@gmail.com");
            preFill.put("contact",sharedPreference.readAgentPhone());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try
        {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            loadingbar.setTitle("please wait");
            loadingbar.setMessage("storing details into records");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            apiService = APIUrl.getApiClient().create(ApiService.class);
            Call<Status> call = apiService.agentRegistration(
                    sharedPreference.readAgentEmail(),
                    sharedPreference.readAgentPhone(),
                    sharedPreference.readAgentLatitude(),
                    sharedPreference.readAgentLongitude(),
                    sharedPreference.readAgentPassword(),
                    sharedPreference.readAgentAadhar(),
                    sharedPreference.readAgentBankName(),
                    sharedPreference.readAgentBankNumber(),
                    sharedPreference.readAgentBankIfsc(),
                    "null",
                    sharedPreference.readAgentGender(),
                    sharedPreference.readAgentPic(),
                    sharedPreference.readAgentLocation(),
                    sharedPreference.readAgentRefID());
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                    loadingbar.dismiss();
                    if (response.body() == null) {
                        Toast.makeText(getApplicationContext(), "agent not registered", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "status " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                     Intent agentLogin = new Intent(AgentSignUpImage.this, MainActivity.class);
                    agentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(agentLogin);
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(), "some error occurred", Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            Log.e("exception",e.toString());
        }

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}
