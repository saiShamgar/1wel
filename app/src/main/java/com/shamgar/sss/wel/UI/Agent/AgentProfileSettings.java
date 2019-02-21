package com.shamgar.sss.wel.UI.Agent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.shamgar.sss.wel.R;
import com.shamgar.sss.wel.Adapters.MainActivityRecyclerAdapter;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.ProvidersDataModel;
import com.shamgar.sss.wel.Models.SearchItems;
import com.shamgar.sss.wel.Models.Status;
import com.shamgar.sss.wel.UI.MainActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentProfileSettings extends AppCompatActivity {
    private SharedPreferenceConfig sharedPreferenceConfig;
    private ApiService apiService;
    private Toolbar toolbar;
    private ImageView expandedImage;
    private TextView agent_refered_count,agent_refered_id_text,logoutAgent;
    private RecyclerView agent_profile_recyclerview;
    private ProgressDialog progressDialog;

    private List<SearchItems> providersData;
    private MainActivityRecyclerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_settings);
        sharedPreferenceConfig=new SharedPreferenceConfig(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Getting referred people");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar=(Toolbar)findViewById(R.id.agentProfileSettingsToolbar);
        expandedImage=(ImageView)findViewById(R.id.expandedImage);
        agent_refered_count=(TextView)findViewById(R.id.agent_refered_count);
        agent_refered_id_text=(TextView)findViewById(R.id.agent_refered_id_text);
        agent_profile_recyclerview=(RecyclerView)findViewById(R.id.agent_profile_recyclerview);
        logoutAgent=(TextView) findViewById(R.id.logoutAgent);
        String Username = getIntent().getStringExtra("name");
        String token = getIntent().getStringExtra("token");
        String image = getIntent().getStringExtra("image");
        agent_refered_id_text.setText(token);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Username);

        //agent image
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(this)
                .load(image)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.one_wel_loge)
                // read original from cache (if present) otherwise download it and decode it
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(expandedImage);


        //api service for getting referred people
        apiService= APIUrl.getApiClient().create(ApiService.class);

        progressDialog.show();
        Call<ProvidersDataModel> call=apiService.providersDataModel(token);
        call.enqueue(new Callback<ProvidersDataModel>() {
            @Override
            public void onResponse(Call<ProvidersDataModel> call, Response<ProvidersDataModel> response) {
                if (response.body()==null){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"responce null",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                String size= String.valueOf(response.body().getMessage().size());
                agent_refered_count.setText(size);

                providersData=response.body().getMessage();
                adapter=new MainActivityRecyclerAdapter(getApplicationContext(),providersData);
                agent_profile_recyclerview.setHasFixedSize(true);
                agent_profile_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                agent_profile_recyclerview.setAdapter(adapter);
                agent_profile_recyclerview.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();
               // Toast.makeText(getApplicationContext(),"status "+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProvidersDataModel> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

        logoutAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Logging out");
                progressDialog.setMessage("Please wait...,");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                retrofit2.Call<Status> call=apiService.agentLogout();
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
                        if (response.body()==null) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
                            return;
                        }
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"responce "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                        sharedPreferenceConfig.writeAgentLoggedIn("logout");
                        SharedPreferences preferences=getApplicationContext().getSharedPreferences("userLog",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent logout=new Intent(AgentProfileSettings.this,MainActivity.class);
                        startActivity(logout);
                        finish();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<Status> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.agent_menu_options, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==R.id.agentLogout){
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
