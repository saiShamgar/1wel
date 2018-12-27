package com.example.sss.wel.UI.Agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;
import com.example.sss.wel.Utils.SharedPreferenceConfig;

public class AgentProfileSettings extends AppCompatActivity {
    private Button agentLogout;
    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_settings);
        agentLogout=(Button)findViewById(R.id.agentLogout);

        sharedPreferenceConfig=new SharedPreferenceConfig(this);

        agentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceConfig.writeAgentLoggedIn("logout");
                Intent logout=new Intent(AgentProfileSettings.this,MainActivity.class);
                startActivity(logout);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent bqack=new Intent(AgentProfileSettings.this, MainActivity.class);
        bqack.putExtra("agent registered",true);
        startActivity(bqack);
        finish();
    }
}
