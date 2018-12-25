package com.example.sss.wel.UI.Agent;

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
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sss.wel.Adapters.PlaceAutocompleteAdapter;
import com.example.sss.wel.Api.APIUrl;
import com.example.sss.wel.Api.ApiService;
import com.example.sss.wel.Models.AgentRegistration;
import com.example.sss.wel.Models.Services;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;
import com.example.sss.wel.Utils.SharedPreferenceConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136));
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUESTED_CODE = 1234;
    private GoogleApiClient googleApiClient;
    private Boolean mlocation_permission_granted = false;

    //widgets
    private AutoCompleteTextView mSearchText;

    //registration Widgets
    private EditText agent_signup_name,agent_signup_phone,agent_signup_pass,agent_signup_confirm_pass
            ,agent_signup_AadharNum,agent_signup_Bank_name,agent_signup_Bank_Account_num,agent_signup_Bank_Ifsc_Code,
            agent_signup_Pan_number,agent_signup_virification;
    private Button agent_submit_personal_details,agent_submit_bank_details,upload_image_agent,agent_reg_submit_btn,agent_verification_btn;
    private RadioButton radio_btn_male,radio_btn_female;
    private ImageView agent_image;
    private RadioGroup radioGroup;
    private TextView txt_otp_hint;

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
    private  AlertDialog dialog;
    private Bitmap bmp;

    private ApiService apiService;
    private String latitude;
    private String longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_sign_up);
        getLocationPermission();


        mAuth=FirebaseAuth.getInstance();
        sharedPreference=new SharedPreferenceConfig(this);

        ActivityCompat.requestPermissions(AgentSignUpActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        //Initializing google api client
        googleApiClient=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mSearchText=(AutoCompleteTextView)findViewById(R.id.agent_signup_Location);
        mSearchText.setOnItemClickListener(mAutoCompleteListener);
        autocompleteAdapter=new PlaceAutocompleteAdapter(this,googleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(autocompleteAdapter);

        //initializing editText widgets
        agent_signup_name=findViewById(R.id.agent_signup_name);
        agent_signup_phone=findViewById(R.id.agent_signup_phone);
        agent_signup_pass=findViewById(R.id.agent_signup_pass);
        agent_signup_confirm_pass=findViewById(R.id.agent_signup_confirm_pass);
        agent_signup_AadharNum=findViewById(R.id.agent_signup_AadharNum);
        agent_signup_Bank_name=findViewById(R.id.agent_signup_Bank_name);
        agent_signup_Bank_Account_num=findViewById(R.id.agent_signup_Bank_Account_num);
        agent_signup_Bank_Ifsc_Code=findViewById(R.id.agent_signup_Bank_Ifsc_Code);
        agent_signup_Pan_number=findViewById(R.id.agent_signup_Pan_number);
        agent_signup_virification=findViewById(R.id.agent_signup_virification);


        //initialize button widgets
        agent_submit_personal_details=findViewById(R.id.agent_submit_personal_details);
        agent_submit_bank_details=findViewById(R.id.agent_submit_bank_details);
        upload_image_agent=findViewById(R.id.upload_image_agent);
        agent_reg_submit_btn=findViewById(R.id.agent_reg_submit_btn);
        agent_verification_btn=findViewById(R.id.agent_verification_btn);

        //initializing image view
        agent_image=findViewById(R.id.agent_image);

        //initializing text view
        txt_otp_hint=findViewById(R.id.txt_otp_hint);

        //initialize  RadioButtons
        radio_btn_male=findViewById(R.id.radio_btn_male);
        radio_btn_female=findViewById(R.id.radio_btn_female);
        radioGroup=findViewById(R.id.radioGroup);

        //implementing click listeners
        agent_submit_personal_details.setOnClickListener(this);
        agent_submit_bank_details.setOnClickListener(this);
        upload_image_agent.setOnClickListener(this);
        agent_reg_submit_btn.setOnClickListener(this);
        agent_verification_btn.setOnClickListener(this);

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

    //getting location permissions
    private void getLocationPermission() {
        Log.d(TAG,"getting location permission: called");
        String [] permissions={
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"permissions granted");
                mlocation_permission_granted=true;
            }
            else {
                Log.d(TAG,"permissions not granted");
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTED_CODE);
            }
        }
        else {
            Log.d(TAG,"permissions not granted");
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTED_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"on request permisions calling");
        mlocation_permission_granted=false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUESTED_CODE: {
                if(grantResults.length>0) {
                    for(int i=0;i<grantResults.length;i++) {
                        if((grantResults[i] != PackageManager.PERMISSION_GRANTED)) {
                            Log.d(TAG,"not get request permissions");
                            mlocation_permission_granted=false;
                        }
                    }
                    Log.d(TAG,"permissions granted");
                    mlocation_permission_granted=true;
                }
            }
        }
    }

    //setting location places to editText
    private AdapterView.OnItemClickListener mAutoCompleteListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final AutocompletePrediction item=autocompleteAdapter.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi
                    .getPlaceById(googleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallBack);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallBack=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG,"onresult : place Query did not complete Successfully  "+places.getStatus().toString());

                //you can use lat with qLoc.latitude;
                //and long with qLoc.longitude;

            places.release();
                return;
            }
            final Place mPlace = places.get(0);
            LatLng qLoc = mPlace.getLatLng();
             latitude= String.valueOf(qLoc.latitude);
             longitude= String.valueOf(qLoc.longitude);



            Toast.makeText(getApplicationContext(),"location "+qLoc,Toast.LENGTH_LONG).show();
            places.release();
        }
    };
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.agent_submit_personal_details){

            if (TextUtils.isEmpty(agent_signup_name.getText().toString())) {
                agent_signup_name.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_phone.getText().toString())){
                agent_signup_phone.setError("Field cannot be blank");
                return;
            }
            if (!(agent_signup_phone.getText().toString().length()==10)){
                agent_signup_phone.setError("please enter valid number");
                return;
            }
            if (TextUtils.isEmpty(mSearchText.getText().toString())){
                mSearchText.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_pass.getText().toString())) {
                agent_signup_pass.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_confirm_pass.getText().toString())) {
                agent_signup_confirm_pass.setError("Field cannot be blank");
                return;
            }
            if (!(agent_signup_pass.getText().toString().equals(agent_signup_confirm_pass.getText().toString()))){
                agent_signup_confirm_pass.setError("re enter the same password");
            }

            //Storing values
            sharedPreference.writeAgentEmail(agent_signup_name.getText().toString());
            sharedPreference.writeAgentphone(agent_signup_phone.getText().toString());
            sharedPreference.writeAgentLocation(mSearchText.getText().toString());
            sharedPreference.writeAgentPassword(agent_signup_confirm_pass.getText().toString());




            agent_signup_name.setVisibility(View.GONE);
            agent_signup_phone.setVisibility(View.GONE);
            mSearchText.setVisibility(View.GONE);
            agent_signup_pass.setVisibility(View.GONE);
            agent_signup_confirm_pass.setVisibility(View.GONE);
            agent_submit_personal_details.setVisibility(View.GONE);

            agent_signup_AadharNum.setVisibility(View.VISIBLE);
            agent_signup_Bank_name.setVisibility(View.VISIBLE);
            agent_signup_Bank_Account_num.setVisibility(View.VISIBLE);
            agent_signup_Bank_Ifsc_Code.setVisibility(View.VISIBLE);
            agent_signup_Pan_number.setVisibility(View.VISIBLE);
            agent_submit_bank_details.setVisibility(View.VISIBLE);



        }
        if (v.getId()==R.id.agent_submit_bank_details){

            if (TextUtils.isEmpty(agent_signup_AadharNum.getText().toString())){
                agent_signup_AadharNum.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_name.getText().toString())) {
                agent_signup_Bank_name.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_Account_num.getText().toString())) {
                agent_signup_Bank_Account_num.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_Ifsc_Code.getText().toString())) {
                agent_signup_Bank_Ifsc_Code.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Pan_number.getText().toString())) {
                agent_signup_Pan_number.setError("Field cannot be blank");
                return;
            }

            sharedPreference.writeAgentAadhar(agent_signup_AadharNum.getText().toString());
            sharedPreference.writeAgentBankName(agent_signup_Bank_name.getText().toString());
            sharedPreference.writeAgentBankNumber(agent_signup_Bank_Account_num.getText().toString());
            sharedPreference.writeAgentBankIfsc(agent_signup_Bank_Ifsc_Code.getText().toString());
            sharedPreference.writeAgentPanNumber(agent_signup_Pan_number.getText().toString());

            agent_signup_AadharNum.setVisibility(View.GONE);
            agent_signup_Bank_name.setVisibility(View.GONE);
            agent_signup_Bank_Account_num.setVisibility(View.GONE);
            agent_signup_Bank_Ifsc_Code.setVisibility(View.GONE);
            agent_signup_Pan_number.setVisibility(View.GONE);
            agent_submit_bank_details.setVisibility(View.GONE);

            radioGroup.setVisibility(View.VISIBLE);
            agent_image.setVisibility(View.VISIBLE);
            upload_image_agent.setVisibility(View.VISIBLE);
            agent_reg_submit_btn.setVisibility(View.VISIBLE);
        }
        if (v.getId()==R.id.agent_reg_submit_btn){

            if (!(radio_btn_male.isChecked() || radio_btn_female.isChecked())){
                Toast.makeText(getApplicationContext(),"select your gender",Toast.LENGTH_LONG).show();
                return;
            }
            if (radio_btn_male.isChecked())
                sharedPreference.writeAgentGender("male");
            if (radio_btn_female.isChecked())
                sharedPreference.writeAgentGender("female");

             loadingbar=new ProgressDialog(this);
             //sending verification code

                String phoneNumber=agent_signup_phone.getText().toString();
                loadingbar.setTitle("Phone verification");
                loadingbar.setMessage("please wait,while we are authenticating with your phone");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                   callbacks);       // OnVerificationStateChangedCallbacks

        }
        if (v.getId()==R.id.upload_image_agent){
            dialog.show();
        }


        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
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
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(getApplicationContext(),"code sent to the "+agent_signup_phone.getText(),Toast.LENGTH_LONG).show();

                radioGroup.setVisibility(View.GONE);
                agent_image.setVisibility(View.GONE);
                upload_image_agent.setVisibility(View.GONE);
                agent_reg_submit_btn.setVisibility(View.GONE);

                txt_otp_hint.setVisibility(View.VISIBLE);
                agent_signup_virification.setVisibility(View.VISIBLE);
                agent_verification_btn.setVisibility(View.VISIBLE);
            }
        };


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("email",sharedPreference.readAgentEmail());
                            Log.e("phone",sharedPreference.readAgentPhone());
                            Log.e("latitude",latitude);
                            Log.e("longitude",longitude);
                            Log.e("pass",sharedPreference.readAgentPassword());
                            Log.e("aadhar",sharedPreference.readAgentAadhar());
                            Log.e("bank name",sharedPreference.readAgentBankName());
                            Log.e("bank num",sharedPreference.readAgentBankNumber());
                            Log.e("bank ifsc",sharedPreference.readAgentBankIfsc());
                            Log.e("pan num",agent_signup_Pan_number.getText().toString());
                            Log.e("gender",sharedPreference.readAgentGender());
                            Log.e("pic",sharedPreference.readAgentPic());
                            Log.e("location",mSearchText.getText().toString());
                            apiService= APIUrl.getApiClient().create(ApiService.class);
                            loadingbar.dismiss();
                            Call<AgentRegistration> call=apiService.agentRegistration(
                                            sharedPreference.readAgentEmail(),
                                            sharedPreference.readAgentPhone(),
                                            latitude,
                                            longitude,
                                            sharedPreference.readAgentPassword(),
                                            sharedPreference.readAgentAadhar(),
                                            sharedPreference.readAgentBankName(),
                                            sharedPreference.readAgentBankNumber(),
                                            sharedPreference.readAgentBankIfsc(),
                                            agent_signup_Pan_number.getText().toString(),
                                            sharedPreference.readAgentGender(),
                                            "gdhfhd",
                                            mSearchText.getText().toString(),
                                            "1");

                           call.enqueue(new Callback<AgentRegistration>() {
                               @Override
                               public void onResponse(Call<AgentRegistration> call, Response<AgentRegistration> response) {

                                   Toast.makeText(getApplicationContext(),"status success "+response.body() ,Toast.LENGTH_LONG).show();
                                   Intent agentLogin=new Intent(AgentSignUpActivity.this, MainActivity.class);
                                   agentLogin.putExtra("agent registered",true);
                                   startActivity(agentLogin);
                                   finish();
                               }

                               @Override
                               public void onFailure(Call<AgentRegistration> call, Throwable t) {
                                   loadingbar.dismiss();

                               }
                           });

                            //sendUserToMainActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
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
                sharedPreference.writeAgentPic(getFileDataFromDrawable(bmp).toString());
                agent_image.setImageBitmap(bmp);
            }
            else if(requestCode==PICK_FROM_FILE) {
                imagecaptureuri=data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagecaptureuri);
                    sharedPreference.writeAgentPic(getFileDataFromDrawable(bmp).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(imagecaptureuri)
                        .into(agent_image);
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(AgentSignUpActivity.this, MainActivity.class);
        startActivity(back);
        finish();
    }

    //upload images
    public byte[] getFileDataFromDrawable(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}
