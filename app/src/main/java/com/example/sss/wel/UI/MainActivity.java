package com.example.sss.wel.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.wel.Adapters.MainActivityRecyclerAdapter;
import com.example.sss.wel.Adapters.PlaceAutocompleteAdapter;
import com.example.sss.wel.Api.APIUrl;
import com.example.sss.wel.Api.ApiService;
import com.example.sss.wel.Models.Services;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.Agent.AgentLoginActivity;
import com.example.sss.wel.UI.providers.ProviderSignUpActivity;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

  //AutoComplete Textview variables
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
    private Button one_wel_agent,one_wel_provider;
    private String  agent_reg;
    private Spinner mainAcitivity_Category_Spinner,mainActivity_Sub_Category;
    private ArrayAdapter mainCategoryAdapter,subCategoryAdapter;
    ArrayList<String> mainCategoryList = new ArrayList<String>();

    //api interface
    private List<Services> services_subCategory;
    private ApiService apiService;
    private RecyclerView recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        one_wel_agent=findViewById(R.id.one_wel_agent);
        one_wel_provider=findViewById(R.id.one_wel_provider);
        mainAcitivity_Category_Spinner=findViewById(R.id.mainAcitivity_Category);
        mainActivity_Sub_Category=findViewById(R.id.mainActivity_Sub_Category);
        recycler_view=findViewById(R.id.recycler_view);
        boolean defaultValue = false;
        boolean defaultValue1 = false;
        boolean agent = getIntent().getBooleanExtra("agent registered", defaultValue);
        boolean provider = getIntent().getBooleanExtra("provider registered", defaultValue1);
         if (agent){
             one_wel_agent.setVisibility(View.GONE);
         }
         if (agent || provider){
             one_wel_agent.setVisibility(View.GONE);
         }


        one_wel_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agentActivity=new Intent(MainActivity.this, AgentLoginActivity.class);
                startActivity(agentActivity);
                finish();
            }
        });
        one_wel_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent provider_activity=new Intent(MainActivity.this, ProviderSignUpActivity.class);
                startActivity(provider_activity);
                finish();
            }
        });


        getLocationPermission();

        //Initializing google api client
        googleApiClient=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mSearchText=(AutoCompleteTextView)findViewById(R.id.mainActivity_location);
        mSearchText.setOnItemClickListener(mAutoCompleteListener);
        autocompleteAdapter=new PlaceAutocompleteAdapter(this,googleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(autocompleteAdapter);

        //adding main categoty spinner items
        mainCategoryList.add("Services");
        mainCategoryList.add("Hospital");
        mainCategoryList.add("Matrimony");
        mainCategoryList.add("1well Matrimony");
        mainCategoryAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item,mainCategoryList);
        mainCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainAcitivity_Category_Spinner.setAdapter(mainCategoryAdapter);

        mainAcitivity_Category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView service = (TextView)parent.getSelectedView();
               String result = service.getText().toString();
                apiService=APIUrl.getApiClient().create(ApiService.class);

                Call<List<Services>> call=apiService.getServices(result);

                call.enqueue(new Callback<List<Services>>() {
                    @Override
                    public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                        services_subCategory = response.body();
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add("Select SubCategory");
                        for (int i = 0; i < services_subCategory.size(); i++) {
                            temp.add(services_subCategory.get(i).getService());
                        }
                        subCategoryAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, temp);
                        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mainActivity_Sub_Category.setAdapter(subCategoryAdapter);
                        //Toast.makeText(getApplicationContext(),"Services "+temp,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<Services>> call, Throwable t) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


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

            Toast.makeText(getApplicationContext(),"location "+qLoc,Toast.LENGTH_LONG).show();
            places.release();
        }
    };
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
