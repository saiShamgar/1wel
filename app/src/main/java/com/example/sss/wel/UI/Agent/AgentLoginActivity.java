package com.example.sss.wel.UI.Agent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sss.wel.Api.APIUrl;
import com.example.sss.wel.Api.ApiService;

import com.example.sss.wel.Models.AgentData;
import com.example.sss.wel.Models.AgentLoginData;
import com.example.sss.wel.Models.ProvidersData;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;
import com.example.sss.wel.Utils.SharedPreferenceConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class AgentLoginActivity extends AppCompatActivity {
    private TextView txt_agent_signUp;
    private EditText agent_login_name,agent_login_pass;
    private Button agent_login_button;

    private ApiService apiService;
    private SharedPreferenceConfig sharedPreferenceConfig;

    private ProgressDialog progressDialog;

    private List<AgentLoginData> userData;
    private List<ProvidersData> providersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Agent Login");

        txt_agent_signUp=findViewById(R.id.txt_agent_signUp);
        agent_login_name=findViewById(R.id.agent_login_name);
        agent_login_pass=findViewById(R.id.agent_login_pass);
        agent_login_button=findViewById(R.id.agent_login_button);

        sharedPreferenceConfig=new SharedPreferenceConfig(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Verifying your credentials");
        progressDialog.setMessage("Please wait...,");
        progressDialog.setCanceledOnTouchOutside(false);

        txt_agent_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agentSignUp=new Intent(AgentLoginActivity.this,AgentSignUpActivity.class);
                startActivity(agentSignUp);
                finish();
            }
        });

        agent_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(agent_login_name.getText().toString())){
                     agent_login_name.setError("Field cannot be blank");
                      return;
                 }
                 if (TextUtils.isEmpty(agent_login_pass.getText().toString())) {
                     agent_login_pass.setError("Field cannot be blank");
                     return;
                 }


                progressDialog.show();

                apiService=APIUrl.getApiClient().create(ApiService.class);
                final retrofit2.Call<AgentData> call=apiService.agentLogin(
                        agent_login_name.getText().toString(),
                        agent_login_pass.getText().toString());
                call.enqueue(new Callback<AgentData>() {
                    @Override
                    public void onResponse(retrofit2.Call<AgentData> call, Response<AgentData> response) {
                        if (response.body()==null) {
                            Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            return;
                        }

                        if (response.body().getData().size()!=0){
                            progressDialog.dismiss();
                            userData=response.body().getData();
                           // Toast.makeText(getApplicationContext(),userData.toString(),Toast.LENGTH_LONG).show();
                            Intent agentLogin=new Intent(AgentLoginActivity.this, MainActivity.class);
                            SharedPreferences preferences=getApplicationContext().getSharedPreferences("userLog",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("name",userData.get(0).getUsername());
                            editor.putString("image",userData.get(0).getProfile_pic());
                            editor.putString("token",userData.get(0).getToken());
                            editor.commit();
                            startActivity(agentLogin);
                            finish();
                            sharedPreferenceConfig.writeAgentLoggedIn("agent registered");
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<AgentData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

//
//              Call<AgentData> call=apiService.agentLogin();
//
//              call.enqueue(new Callback<AgentData>() {
//                  @Override
//                  public void onResponse(Call<AgentData> call, retrofit2.Response<AgentData> response) {
//                      if (response.body()==null) {
//                            Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
//                            return;
//                        }
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"status "+response.body().getMessage(),Toast.LENGTH_LONG).show();
//                        Intent agentLogin=new Intent(AgentLoginActivity.this, MainActivity.class);
//                        startActivity(agentLogin);
//                        finish();
//                        sharedPreferenceConfig.writeAgentLoggedIn("agent registered");
//                  }
//
//                  @Override
//                  public void onFailure(Call<AgentData> call, Throwable t) {
//                      progressDialog.dismiss();
//
//                  }
//              });

//                Call<List<AgentData>> call=apiService.agentLogin(
//                        agent_login_name.getText().toString(),agent_login_pass.getText().toString());
//
//
//                call.enqueue(new Callback<List<AgentData>>() {
//                    @Override
//                    public void onResponse(Call<List<AgentData>> call, retrofit2.Response<List<AgentData>> response) {
//                        if (response.body()==null) {
//                            Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
//                            return;
//                        }
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"status "+response.body(),Toast.LENGTH_LONG).show();
//                        Intent agentLogin=new Intent(AgentLoginActivity.this, MainActivity.class);
//                        startActivity(agentLogin);
//                        finish();
//                        sharedPreferenceConfig.writeAgentLoggedIn("agent registered");
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<AgentData>> call, Throwable t) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"statusugj ",Toast.LENGTH_LONG).show();
//                    }
//                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Intent agentSignUp=new Intent(AgentLoginActivity.this,MainActivity.class);
            startActivity(agentSignUp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent agentSignUp=new Intent(AgentLoginActivity.this,MainActivity.class);
        startActivity(agentSignUp);
        finish();
    }
}
