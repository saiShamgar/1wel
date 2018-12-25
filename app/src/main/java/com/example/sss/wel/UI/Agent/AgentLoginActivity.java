package com.example.sss.wel.UI.Agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sss.wel.R;
import com.example.sss.wel.UI.MainActivity;

public class AgentLoginActivity extends AppCompatActivity {
    private TextView txt_agent_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        txt_agent_signUp=findViewById(R.id.txt_agent_signUp);

        txt_agent_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agentSignUp=new Intent(AgentLoginActivity.this,AgentSignUpActivity.class);
                startActivity(agentSignUp);
                finish();
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
