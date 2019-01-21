package com.example.sss.wel.UI.Agent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sss.wel.Api.APIUrl;
import com.example.sss.wel.Api.ApiService;
import com.example.sss.wel.Models.Status;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;
import com.example.sss.wel.Utils.SharedPreferenceConfig;

import retrofit2.Callback;
import retrofit2.Response;

public class AgentProfileSettings extends AppCompatActivity {
    private SharedPreferenceConfig sharedPreferenceConfig;
    private ApiService apiService;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_settings);
        sharedPreferenceConfig=new SharedPreferenceConfig(this);
        toolbar=(Toolbar)findViewById(R.id.agentProfileSettingsToolbar);
        String Username = getIntent().getStringExtra("name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.agent_menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.agentLogout){
            apiService= APIUrl.getApiClient().create(ApiService.class);
            retrofit2.Call<Status> call=apiService.agentLogout();
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
                    if (response.body()==null) {
                        Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
                        return;
                    }
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
                }
            });
        }
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
