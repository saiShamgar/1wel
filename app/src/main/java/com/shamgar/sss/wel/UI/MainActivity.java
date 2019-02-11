package com.shamgar.sss.wel.UI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
import com.shamgar.sss.wel.Adapters.MainActivityRecyclerAdapter;
import com.shamgar.sss.wel.Adapters.PlaceAutocompleteAdapter;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.SearchItems;
import com.shamgar.sss.wel.Models.Services;
import com.shamgar.sss.wel.UI.Agent.AgentLoginActivity;
import com.shamgar.sss.wel.UI.Agent.AgentProfileSettings;
import com.shamgar.sss.wel.UI.providers.ProviderSignUpActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    boolean doubleBackToExitPressedOnce = false;

    //widgets
    private AutoCompleteTextView mSearchText,edt_search_items;
    private CircleImageView one_wel_logo;
    private TextView one_wel_agent,one_wel_provider,searchGo;
    ArrayList<String> mainCategoryList = new ArrayList<String>();
    private ImageButton searchItemBlockOption;

    //api interface
     private List<Services> services_types;
     private List<SearchItems> searchItems;
    private ApiService apiService;
    private RecyclerView recycler_view;
    private MainActivityRecyclerAdapter adapter;
    private SharedPreferenceConfig sharedPreferenceConfig;

    private String latitude,longitude;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferenceConfig=new SharedPreferenceConfig(this);

        one_wel_agent=findViewById(R.id.one_wel_agent);
        one_wel_provider=findViewById(R.id.one_wel_provider);
        one_wel_logo=findViewById(R.id.one_wel_logo);
        recycler_view=findViewById(R.id.recycler_view);
        edt_search_items=findViewById(R.id.edt_search_items);
        searchGo=findViewById(R.id.searchGo);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Getting Search Items");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);

        boolean defaultValue1 = false;
        final boolean provider = getIntent().getBooleanExtra("provider registered", defaultValue1);

         if (sharedPreferenceConfig.readAgentLoggedin().contains("agent registered")){
             one_wel_agent.setVisibility(View.GONE);
             SharedPreferences preferences=getApplicationContext().getSharedPreferences("userLog",MODE_PRIVATE);
             final String image=preferences.getString("image",null);
             final String name=preferences.getString("name",null);
             final String token=preferences.getString("token",null);

             Glide.with(this)
                     .load(image)
                     .placeholder(R.drawable.ic_person_black_24dp)
                     // read original from cache (if present) otherwise download it and decode it
                     .into(one_wel_logo);

             one_wel_logo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent profileSettings=new Intent(MainActivity.this, AgentProfileSettings.class);
                     profileSettings.putExtra("name",name);
                     profileSettings.putExtra("token",token);
                     profileSettings.putExtra("image",image);
                     startActivity(profileSettings);
                     finish();

                 }
             });
         }
         if (sharedPreferenceConfig.readAgentLoggedin().contains("agent registered") || provider){
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


        edt_search_items.addTextChangedListener(new TextWatcher() {
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

        //setting adapter
        searchGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<List<SearchItems>> call=apiService.search(latitude,longitude,edt_search_items.getText().toString());

                call.enqueue(new Callback<List<SearchItems>>() {
                    @Override
                    public void onResponse(Call<List<SearchItems>> call, Response<List<SearchItems>> response) {
                        searchItems=response.body();

                        if (response.body()==null){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"no Providers Found",Toast.LENGTH_LONG).show();
                            return;
                        }
                        progressDialog.dismiss();
                       // Toast.makeText(getApplicationContext(),"items"+services_types,Toast.LENGTH_LONG).show();
                        adapter=new MainActivityRecyclerAdapter(getApplicationContext(),searchItems);
                        recycler_view.setHasFixedSize(true);
                        recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycler_view.setAdapter(adapter);
                        recycler_view.setNestedScrollingEnabled(false);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<SearchItems>> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"some error occured",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void filter(String text) {
        apiService= APIUrl.getApiClient().create(ApiService.class);
        Call<List<Services>> call=apiService.getServices(text);
        call.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                services_types=response.body();
                if (response.body()==null)
                    return;
                ArrayList<String> temp = new ArrayList<>();
                if (services_types.isEmpty())
                    return;
                for (int i = 0; i < services_types.size(); i++)
                    temp.add(services_types.get(i).getService());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_multiple_choice,temp);
                edt_search_items.setThreshold(1);
                edt_search_items.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {

            }
        });
    }




//
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
