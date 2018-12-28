package com.example.sss.wel.UI.Agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_settings);
        sharedPreferenceConfig=new SharedPreferenceConfig(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agent profile");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent bqack=new Intent(AgentProfileSettings.this, MainActivity.class);
        bqack.putExtra("agent registered",true);
        startActivity(bqack);
        finish();
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
                    Intent logout=new Intent(AgentProfileSettings.this,MainActivity.class);
                    startActivity(logout);
                    finish();
                }
                @Override
                public void onFailure(retrofit2.Call<Status> call, Throwable t) {
                }
            });

            if (item.getItemId()==android.R.id.home){
                Intent bqack=new Intent(AgentProfileSettings.this, MainActivity.class);
                bqack.putExtra("agent registered",true);
                startActivity(bqack);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
