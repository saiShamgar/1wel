package com.shamgar.sss.wel.UI.Agent;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import com.shamgar.sss.wel.Adapters.PlaceAutocompleteAdapter;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

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
    private EditText agent_signup_name,agent_signup_phone,agent_signup_pass,agent_signup_confirm_pass,agent_signup_Refid;
    private Button agent_submit_personal_details,upload_image_agent,agent_reg_submit_btn,agent_verification_btn;


    private SharedPreferenceConfig sharedPreference;
    private String latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_sign_up);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promoter Sign Up");

        sharedPreference=new SharedPreferenceConfig(this);



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
        agent_signup_Refid=findViewById(R.id.agent_signup_Refid);


        //initialize button widgets
        agent_submit_personal_details=findViewById(R.id.agent_submit_personal_details);


        //implementing click listeners
        agent_submit_personal_details.setOnClickListener(this);

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
                return;
            }

            //Storing values
            sharedPreference.writeAgentEmail(agent_signup_name.getText().toString());
            sharedPreference.writeAgentphone(agent_signup_phone.getText().toString());
            sharedPreference.writeAgentLocation(mSearchText.getText().toString());
            sharedPreference.writeAgentPassword(agent_signup_confirm_pass.getText().toString());
            sharedPreference.writeAgentRefID(agent_signup_Refid.getText().toString());

            Log.e("Agent name",agent_signup_name.getText().toString());
            Log.e("Agent phone",agent_signup_phone.getText().toString());
            Log.e("Agent location",mSearchText.getText().toString());
            Log.e("Agent pass",agent_signup_confirm_pass.getText().toString());
            Log.e("Agent name",agent_signup_Refid.getText().toString());

            sharedPreference.writeAgentLatitude(latitude);
            sharedPreference.writeAgentLongitude(longitude);
            Intent bank=new Intent(AgentSignUpActivity.this,AgentSignUPBankDetails.class);
            startActivity(bank);

        }

    }
}