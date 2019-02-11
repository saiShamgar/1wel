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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.shamgar.sss.wel.R;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shamgar.sss.wel.Adapters.PlaceAutocompleteAdapter;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Services;
import com.shamgar.sss.wel.UI.MainActivity;
import com.shamgar.sss.wel.UI.PaymentActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    //location variables
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
    private AutoCompleteTextView mSearchText,providerServiceType;

    //registration Widgets
    private EditText provider_signup_name,provider_signup_phone,provider_signup_website_url,provider_signup_virification,provider_date_of_birth,agentRefId;
    private EditText providerServiceDescription;
    private Button provider_submit_personal_details,upload_image_provider,provider_reg_submit_btn,provider_verification_btn;
    private RadioButton provider_radio_btn_male,provider_radio_btn_female;
    private ImageView provider_image;
    private RadioGroup radioGroupProvider ;
    private TextView txt_otp_hint_provider;

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

    //private ArrayAdapter mainCategoryAdapter,subCategoryAdapter;
   // ArrayList<String> mainCategoryList = new ArrayList<String>();

    //api interface
    private List<Services> services_tyeps;
    private ApiService apiService;

    private String longitude,latitude;
  //  private int serviceMain, serviceSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_sign_up);
        mAuth = FirebaseAuth.getInstance();
        sharedPreference = new SharedPreferenceConfig(this);


        ActivityCompat.requestPermissions(ProviderSignUpActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        //Initializing google api client
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText = (AutoCompleteTextView) findViewById(R.id.provider_signup_Location);
        mSearchText.setOnItemClickListener(mAutoCompleteListener);
        autocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(autocompleteAdapter);

        //initializing editText widgets
        provider_signup_name = findViewById(R.id.provider_signup_name);
        provider_signup_phone = findViewById(R.id.provider_signup_phone);
        providerServiceType = findViewById(R.id.provider_services_type);
        providerServiceDescription = findViewById(R.id.provider_service_descp);
        provider_signup_website_url = findViewById(R.id.provider_signup_website_url);
        provider_signup_virification = findViewById(R.id.provider_signup_virification);
        provider_date_of_birth = findViewById(R.id.provider_date_of_birth);
        agentRefId = findViewById(R.id.provider_agentRefId);

        //initialize button widgets
        provider_submit_personal_details = findViewById(R.id.provider_submit_personal_details);
        upload_image_provider = findViewById(R.id.upload_image_provider);
        provider_reg_submit_btn = findViewById(R.id.provider_reg_submit_btn);
        provider_verification_btn = findViewById(R.id.provider_verification_btn);

        //initializing imageview
        provider_image = findViewById(R.id.provider_image);

        //intializing Textview
        txt_otp_hint_provider = findViewById(R.id.txt_otp_hint_provider);


        //initialize  RadioButtons
        provider_radio_btn_male = findViewById(R.id.provider_radio_btn_male);
        provider_radio_btn_female = findViewById(R.id.provider_radio_btn_female);
        radioGroupProvider = findViewById(R.id.radioGroupProvider);

        //implementing click listeners
        provider_submit_personal_details.setOnClickListener(this);
        upload_image_provider.setOnClickListener(this);
        provider_reg_submit_btn.setOnClickListener(this);
        provider_date_of_birth.setOnClickListener(this);
        provider_verification_btn.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Provider SignUp");

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

        providerServiceType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });



    }

    private void filter(String text) {
        apiService= APIUrl.getApiClient().create(ApiService.class);
        Call<List<Services>> call=apiService.getServices(text);
        call.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                services_tyeps=response.body();
                if (response.body()==null)
                    return;
                ArrayList<String> temp = new ArrayList<>();
                if (services_tyeps.isEmpty())
                    return;
                for (int i = 0; i < services_tyeps.size(); i++)
                    temp.add(services_tyeps.get(i).getService());
                  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_multiple_choice,temp);
                  providerServiceType.setThreshold(1);
                  providerServiceType.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {

            }
        });
    }

//    //getting location permissions
//    private void getLocationPermission() {
//        Log.d(TAG,"getting location permission: called");
//        String [] permissions={
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION};
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
//            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG,"permissions granted");
//                mlocation_permission_granted=true;
//            }
//            else {
//                Log.d(TAG,"permissions not granted");
//                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTED_CODE);
//            }
//        }
//        else {
//            Log.d(TAG,"permissions not granted");
//            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTED_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG,"on request permisions calling");
//        mlocation_permission_granted=false;
//        switch (requestCode) {
//            case LOCATION_PERMISSION_REQUESTED_CODE: {
//                if(grantResults.length>0) {
//                    for(int i=0;i<grantResults.length;i++) {
//                        if((grantResults[i] != PackageManager.PERMISSION_GRANTED)) {
//                            Log.d(TAG,"not get request permissions");
//                            mlocation_permission_granted=false;
//                        }
//                    }
//                    Log.d(TAG,"permissions granted");
//                    mlocation_permission_granted=true;
//                }
//            }
//        }
//    }

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
                Log.d(TAG,"on result : place Query did not complete Successfully  "+places.getStatus().toString());

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

        if (v.getId()==R.id.provider_submit_personal_details){

            if (TextUtils.isEmpty(provider_signup_name.getText().toString())) {
                provider_signup_name.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(provider_signup_phone.getText().toString())){
                provider_signup_phone.setError("Field cannot be blank");
                return;
            }
            if (!(provider_signup_phone.getText().toString().length()==10)){
                provider_signup_phone.setError("please enter valid number");
            }
            if (TextUtils.isEmpty(mSearchText.getText().toString())){
                mSearchText.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(providerServiceType.getText().toString())){
                providerServiceType.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(providerServiceDescription.getText().toString())){
                providerServiceDescription.setError("Field cannot be blank");
                return;
            }
            if (providerServiceDescription.length()>15){
                providerServiceDescription.setError("input field max 15 characters");
                return;
            }
            if (TextUtils.isEmpty(provider_date_of_birth.getText().toString())){
                provider_date_of_birth.setError("Field cannot be blank");
                return;
            }
            sharedPreference.writeProviderName(provider_signup_name.getText().toString());
            sharedPreference.writeProviderPhone(provider_signup_phone.getText().toString());
            sharedPreference.writeProviderLocation(mSearchText.getText().toString());
            sharedPreference.writeProviderWebsite(provider_signup_website_url.getText().toString());
            sharedPreference.writeProviderDob(provider_date_of_birth.getText().toString());

            provider_signup_name.setVisibility(View.GONE);
            provider_signup_phone.setVisibility(View.GONE);
            mSearchText.setVisibility(View.GONE);
            providerServiceDescription.setVisibility(View.GONE);
            provider_signup_website_url.setVisibility(View.GONE);
            provider_submit_personal_details.setVisibility(View.GONE);
            providerServiceType.setVisibility(View.GONE);
            provider_date_of_birth.setVisibility(View.GONE);
            agentRefId.setVisibility(View.GONE);

            radioGroupProvider.setVisibility(View.VISIBLE);
            provider_image.setVisibility(View.VISIBLE);
            upload_image_provider.setVisibility(View.VISIBLE);
            provider_reg_submit_btn.setVisibility(View.VISIBLE);
        }
        if (v.getId()==R.id.provider_reg_submit_btn){
            if (!(provider_radio_btn_male.isChecked() || provider_radio_btn_female.isChecked())){
                Toast.makeText(getApplicationContext(),"select your gender",Toast.LENGTH_LONG).show();
                return;
            }
            if (provider_radio_btn_male.isChecked())
                sharedPreference.writeProviderGender("male");
            if (provider_radio_btn_female.isChecked())
                sharedPreference.writeProviderGender("female");

            loadingbar=new ProgressDialog(this);
            //sending verification code

            String phoneNumber=provider_signup_phone.getText().toString();
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
        if (v.getId()==R.id.upload_image_provider){
            dialog.show();
        }
        if (v.getId()==R.id.provider_verification_btn){

            loadingbar.setTitle("Phone verification");
            loadingbar.setMessage("please wait,while we are authenticating with your phone");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, provider_signup_virification.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }



        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(getApplicationContext(),"code sent to the "+provider_signup_phone.getText(),Toast.LENGTH_LONG).show();

                radioGroupProvider.setVisibility(View.GONE);
                provider_image.setVisibility(View.GONE);
                upload_image_provider.setVisibility(View.GONE);
                provider_reg_submit_btn.setVisibility(View.GONE);


                txt_otp_hint_provider.setVisibility(View.VISIBLE);
                provider_verification_btn.setVisibility(View.VISIBLE);
                provider_signup_virification.setVisibility(View.VISIBLE);
            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();

                            double cost;
                            if (TextUtils.isEmpty(provider_signup_website_url.getText().toString())){
                                cost=100;
                            }else {
                                cost=500;
                            }

                            Intent payment=new Intent(ProviderSignUpActivity.this, PaymentActivity.class);
                            payment.putExtra("phone",sharedPreference.readProviderPhone());
                            payment.putExtra("cost",cost);
                            payment.putExtra("user_type","provider");
                            payment.putExtra("latitude",latitude);
                            payment.putExtra("longitude",longitude);
                            payment.putExtra("address",mSearchText.getText().toString());
                            payment.putExtra("service_type",providerServiceType.getText().toString());
                            payment.putExtra("service_descp",providerServiceDescription.getText().toString());
                            payment.putExtra("refId",agentRefId.getText().toString());
                            startActivity(payment);
                            finish();

//                            apiService=APIUrl.getApiClient().create(ApiService.class);
//
//                            Log.e("email",sharedPreference.readProviderName());
//                            Log.e("phone",sharedPreference.readProviderPhone());
//                            Log.e("latitude",latitude);
//                            Log.e("longitude",longitude);
//                           // Log.e("ser", String.valueOf(serviceMain));
//                           // Log.e("dfs", String.valueOf(serviceSub));
//                            Log.e("gender", sharedPreference.readProviderWebsite());
//                            Log.e("pic",imageToString(bmp));
//                            Log.e("addres",mSearchText.getText().toString());
//                            Log.e("dob",sharedPreference.readProviderDob());
//                            Log.e("dob",sharedPreference.readProviderGender());
//                            Log.e("dob",providerServiceDescription.getText().toString());
//                            Call<Status> call=apiService.ProviderRegistration(
//                                    sharedPreference.readProviderName(),
//                                    sharedPreference.readProviderPhone(),
//                                    latitude,
//                                    longitude,
//                                    sharedPreference.readProviderGender(),
//                                    imageToString(bmp),
//                                    mSearchText.getText().toString(),
//                                    "1WTOul0IBzwSF4EL",
//                                    sharedPreference.readProviderDob(),
//                                    sharedPreference.readProviderWebsite(),
//                                    providerServiceType.getText().toString(),
//                                    providerServiceDescription.getText().toString());
//                                 //   providerServiceDescription.getText().toString());
//                            call.enqueue(new Callback<Status>() {
//                                @Override
//                                public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
//                                    loadingbar.dismiss();
//                                    if (response.body()==null)
//                                    {
//                                        Toast.makeText(getApplicationContext(), "some error was occured/please register again", Toast.LENGTH_LONG).show();
//                                        Intent agentLogin = new Intent(ProviderSignUpActivity.this, MainActivity.class);
//                                        startActivity(agentLogin);
//                                        finish();
//                                        return;
//                                    }
//                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                                    Intent agentLogin = new Intent(ProviderSignUpActivity.this, MainActivity.class);
//                                    agentLogin.putExtra("provider registered", true);
//                                    startActivity(agentLogin);
//                                    finish();
//                                }
//
//                                @Override
//                                public void onFailure(Call<Status> call, Throwable t) {
//                                    loadingbar.dismiss();
//                                }
//                            });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Intent agentSignUp=new Intent(ProviderSignUpActivity.this,MainActivity.class);
            startActivity(agentSignUp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent agentSignUp=new Intent(ProviderSignUpActivity.this,MainActivity.class);
        startActivity(agentSignUp);
        finish();
    }

}
