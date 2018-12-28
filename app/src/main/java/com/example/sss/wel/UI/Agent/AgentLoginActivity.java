package com.example.sss.wel.UI.Agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.wel.Api.APIUrl;
import com.example.sss.wel.Api.ApiService;
import com.example.sss.wel.Models.Status;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;
import com.example.sss.wel.Utils.SharedPreferenceConfig;

import retrofit2.Callback;
import retrofit2.Response;

public class AgentLoginActivity extends AppCompatActivity {
    private TextView txt_agent_signUp;
    private EditText agent_login_name,agent_login_pass;
    private Button agent_login_button;

    private ApiService apiService;
    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        txt_agent_signUp=findViewById(R.id.txt_agent_signUp);
        agent_login_name=findViewById(R.id.agent_login_name);
        agent_login_pass=findViewById(R.id.agent_login_pass);
        agent_login_button=findViewById(R.id.agent_login_button);

        sharedPreferenceConfig=new SharedPreferenceConfig(this);

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

                apiService=APIUrl.getApiClient().create(ApiService.class);
                retrofit2.Call<Status> call=apiService.agentLogin(
                        agent_login_name.getText().toString(),agent_login_pass.getText().toString());

                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
                        if (response.body()==null) {
                            Toast.makeText(getApplicationContext(),"responce error",Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(),"responce "+response.body().getMessage(),Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(),"status success "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                        Intent agentLogin=new Intent(AgentLoginActivity.this, MainActivity.class);
                        startActivity(agentLogin);
                        finish();
                        sharedPreferenceConfig.writeAgentLoggedIn("agent registered");
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Status> call, Throwable t) {

                    }
                });

            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(AgentLoginActivity.this, MainActivity.class);
        startActivity(back);
        finish();
    }
}
