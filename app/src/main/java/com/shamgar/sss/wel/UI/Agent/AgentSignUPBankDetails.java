package com.shamgar.sss.wel.UI.Agent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shamgar.sss.wel.R;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

public class AgentSignUPBankDetails extends AppCompatActivity implements View.OnClickListener {

    private EditText agent_signup_AadharNum,agent_signup_Bank_name,agent_signup_Bank_Account_num,agent_signup_Bank_Ifsc_Code,agent_signup_ReEnterBank_Account_num;

    private Button agent_submit_bank_details;

    private SharedPreferenceConfig sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_sign_upbank_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promoter Sign Up");

        sharedPreference=new SharedPreferenceConfig(this);

        agent_signup_AadharNum=findViewById(R.id.agent_signup_AadharNum);
        agent_signup_Bank_name=findViewById(R.id.agent_signup_Bank_name);
        agent_signup_Bank_Account_num=findViewById(R.id.agent_signup_Bank_Account_num);
        agent_signup_Bank_Ifsc_Code=findViewById(R.id.agent_signup_Bank_Ifsc_Code);
        agent_signup_ReEnterBank_Account_num=findViewById(R.id.agent_signup_ReEnterBank_Account_num);

        agent_submit_bank_details=findViewById(R.id.agent_submit_bank_details);

        agent_submit_bank_details.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.agent_submit_bank_details){

            if (TextUtils.isEmpty(agent_signup_AadharNum.getText().toString())){
                agent_signup_AadharNum.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_name.getText().toString())) {
                agent_signup_Bank_name.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_Account_num.getText().toString())) {
                agent_signup_Bank_Account_num.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_Bank_Ifsc_Code.getText().toString())) {
                agent_signup_Bank_Ifsc_Code.setError("Field cannot be blank");
                return;
            }
            if (TextUtils.isEmpty(agent_signup_ReEnterBank_Account_num.getText().toString())) {
                agent_signup_ReEnterBank_Account_num.setError("Field cannot be blank");
                return;
            }
            if (!(agent_signup_Bank_Account_num.getText().toString().equals(agent_signup_ReEnterBank_Account_num.getText().toString()))){
                agent_signup_ReEnterBank_Account_num.setError("re enter the same Bank account number");
                return;
            }

            sharedPreference.writeAgentAadhar(agent_signup_AadharNum.getText().toString());
            sharedPreference.writeAgentBankName(agent_signup_Bank_name.getText().toString());
            sharedPreference.writeAgentBankNumber(agent_signup_ReEnterBank_Account_num.getText().toString());
            sharedPreference.writeAgentBankIfsc(agent_signup_Bank_Ifsc_Code.getText().toString());

            Log.e("Agent aadhar",agent_signup_AadharNum.getText().toString());
            Log.e("Agent bank",agent_signup_Bank_name.getText().toString());
            Log.e("Agent ac",agent_signup_ReEnterBank_Account_num.getText().toString());
            Log.e("Agent ifsc",agent_signup_Bank_Ifsc_Code.getText().toString());

            Intent bank=new Intent(AgentSignUPBankDetails.this,AgentSignUpImage.class);
            startActivity(bank);

        }

    }
}
