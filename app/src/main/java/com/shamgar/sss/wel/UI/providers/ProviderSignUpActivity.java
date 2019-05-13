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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.shamgar.sss.wel.Models.Status;
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
    private EditText provider_signup_name,provider_signup_phone,provider_signup_website_url,provider_signup_virification,agentRefId;
    private EditText providerServiceDescription;
    private Button provider_submit_personal_details;
    private LinearLayout discountPrice,privacy;
    private EditText edtMinDiscount,edtMaxDiscount,provider_agentRefId,provider_landmark;



    private SharedPreferenceConfig sharedPreference;
    private CheckBox privacyShow,privacyHide;
    private String privacyStatus;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Provider SignUp");

        sharedPreference = new SharedPreferenceConfig(this);


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
        provider_landmark = findViewById(R.id.provider_landmark);

        agentRefId = findViewById(R.id.provider_agentRefId);
        discountPrice = findViewById(R.id.discountPrice);
        edtMinDiscount = findViewById(R.id.edtMinDiscount);
        edtMaxDiscount = findViewById(R.id.edtMaxDiscount);
        provider_agentRefId = findViewById(R.id.provider_agentRefId);
        privacy = findViewById(R.id.privacy);

        //initialize button widgets
        provider_submit_personal_details = findViewById(R.id.provider_submit_personal_details);


        //initializing imageview

        privacyShow = findViewById(R.id.privacyShow);
        privacyHide = findViewById(R.id.privacyHide);



        //implementing click listeners
        provider_submit_personal_details.setOnClickListener(this);






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
            if (TextUtils.isEmpty(provider_landmark.getText().toString())){
                provider_landmark.setError("Field cannot be blank");
                return;
            }
            if (providerServiceDescription.length()>500){
                providerServiceDescription.setError("input field max 50 characters");
                return;

            }
            if (!(privacyShow.isChecked() || privacyHide.isChecked())){
                Toast.makeText(getApplicationContext(),"select your privacy method",Toast.LENGTH_LONG).show();
                return;
            }
            if (privacyShow.isChecked())
                privacyStatus="1";

            if (privacyHide.isChecked())
                privacyStatus="2";

            sharedPreference.writeProviderName(provider_signup_name.getText().toString());
            sharedPreference.writeProviderPhone(provider_signup_phone.getText().toString());
            sharedPreference.writeProviderLocation(mSearchText.getText().toString());
            sharedPreference.writeProviderService(providerServiceType.getText().toString());
            sharedPreference.writeProviderServiceDesc(providerServiceDescription.getText().toString());
            sharedPreference.writeProviderDiscount(edtMinDiscount.getText().toString()+
            "% to "+edtMaxDiscount.getText().toString()+"%");
            sharedPreference.writeProviderREfID(provider_agentRefId.getText().toString());
            sharedPreference.writeProviderWebsite(provider_signup_website_url.getText().toString());
            sharedPreference.writeProviderStatus(privacyStatus);
            sharedPreference.writeProviderLatitude(latitude);
            sharedPreference.writeProviderLongitude(longitude);
            sharedPreference.writeProviderLandmark(provider_landmark.getText().toString());


            Log.e("provider name",sharedPreference.readProviderName());
            Log.e("provider phone",sharedPreference.readProviderPhone());
            Log.e("provider location",sharedPreference.readProviderLocation());
            Log.e("provider service" ,sharedPreference.readProviderService());
            Log.e("provider serDESC",sharedPreference.readProviderServiceDesc());
            Log.e("provider discount",sharedPreference.readProviderDiscount());
            Log.e("provider refID",sharedPreference.readProviderRefID());
            Log.e("provider website",sharedPreference.readProviderWebsite());
            Log.e("provider status",sharedPreference.readProviderStatus());
            Log.e("provider latitude",sharedPreference.readProviderLatitude());
            Log.e("provider longitude",sharedPreference.readProviderLongitude());
            Log.e("provider landmark",sharedPreference.readProviderLandmark());

            Intent data=new Intent(ProviderSignUpActivity.this,ProviderSignUpImage.class);
            startActivity(data);

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        provider_signup_name.setText(sharedPreference.readProviderName());
//        provider_signup_phone.setText(sharedPreference.readProviderPhone());
//        mSearchText.setText(sharedPreference.readProviderLocation());
//        providerServiceType.setText(sharedPreference.readProviderService());
//        providerServiceDescription.setText(sharedPreference.readProviderServiceDesc());
//        provider_agentRefId.setText(sharedPreference.readProviderRefID());
//        provider_signup_website_url.setText(sharedPreference.readProviderWebsite());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        provider_signup_name.setText(sharedPreference.readProviderName());
//        provider_signup_phone.setText(sharedPreference.readProviderPhone());
//        mSearchText.setText(sharedPreference.readProviderLocation());
//        providerServiceType.setText(sharedPreference.readProviderService());
//        providerServiceDescription.setText(sharedPreference.readProviderServiceDesc());
//        provider_agentRefId.setText(sharedPreference.readProviderRefID());
//        provider_signup_website_url.setText(sharedPreference.readProviderWebsite());
    }
}
