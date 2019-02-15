package com.shamgar.sss.wel.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.shamgar.sss.wel.R;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Status;
import com.shamgar.sss.wel.UI.Agent.AgentLoginActivity;
import com.shamgar.sss.wel.Utils.SharedPreferenceConfig;

import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;


public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private Button paymentButton;
    private TextView txt_ord_id,txt_tot_cst;
    private Double cst;
    private SharedPreferences pref1;
    private String regnumber;
    private SharedPreferenceConfig sharedPreferenceConfig;
    private String loc,latitude,longitude,image,address,service_type,service_desp,refId;
    private ApiService apiService;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SignUp Payment");

        paymentButton=(Button)findViewById(R.id.paymentButton);
        txt_ord_id=(TextView) findViewById(R.id.orderid);
        txt_tot_cst=(TextView)findViewById(R.id.totalcost);

        sharedPreferenceConfig=new SharedPreferenceConfig(this);
        progressDialog=new ProgressDialog(this);

        cst=getIntent().getDoubleExtra("cost",125);
        regnumber=getIntent().getExtras().getString("phone");
        latitude=getIntent().getExtras().getString("latitude");
        longitude=getIntent().getExtras().getString("longitude");
        image=getIntent().getExtras().getString("image");
        address=getIntent().getExtras().getString("address");
        service_type=getIntent().getExtras().getString("service_type");
        service_desp=getIntent().getExtras().getString("service_descp");
        refId=getIntent().getExtras().getString("refId");

        sharedPreferenceConfig=new SharedPreferenceConfig(this);
        txt_ord_id.setText("XXXXXXXXXX104");
        txt_tot_cst.setText(cst.toString());
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());
        // Payment button created by you in XML layout
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                       startPayment();
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay");
            options.put("description", "Including all Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount",cst*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "1welsupport@gmail.com");
            preFill.put("contact",regnumber);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            progressDialog.setTitle("please wait");
            progressDialog.setMessage("storing details into records");
            progressDialog.setCanceledOnTouchOutside(false);
            String userType=getIntent().getExtras().getString("user_type");
            if (userType.contains("provider")){
                apiService= APIUrl.getApiClient().create(ApiService.class);
                progressDialog.show();
//
                            Log.e("email",sharedPreferenceConfig.readProviderName());
                            Log.e("phone",sharedPreferenceConfig.readProviderPhone());
                            Log.e("latitude",latitude);
                            Log.e("longitude",longitude);
                           // Log.e("ser", String.valueOf(serviceMain));
                           // Log.e("dfs", String.valueOf(serviceSub));
                            Log.e("gender", sharedPreferenceConfig.readProviderWebsite());
                            Log.e("addres",address);
                            Log.e("dob",sharedPreferenceConfig.readProviderDob());
                            Log.e("dob",sharedPreferenceConfig.readProviderGender());
                            Log.e("dob",service_desp);
                            Call<Status> call=apiService.ProviderRegistration(
                                    sharedPreferenceConfig.readProviderName(),
                                    sharedPreferenceConfig.readProviderPhone(),
                                    latitude,
                                    longitude,
                                    sharedPreferenceConfig.readProviderGender(),
                                    sharedPreferenceConfig.readProviderPic(),
                                    address,
                                    refId,
                                    sharedPreferenceConfig.readProviderDob(),
                                    sharedPreferenceConfig.readProviderWebsite(),
                                    service_type,
                                    service_desp);
                                 //   providerServiceDescription.getText().toString());
                            call.enqueue(new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                                    progressDialog.dismiss();
                                    if (response.body()==null) {
                                       Toast.makeText(getApplicationContext(), "some error was occured/please register again", Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent agentLogin = new Intent(PaymentActivity.this, MainActivity.class);
                                    startActivity(agentLogin);
                                    finish();
                                }
                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"registration Not Successful",Toast.LENGTH_LONG).show();
                                }
                            });

            }else if (userType.contains("agent")){
                progressDialog.show();
                Log.e("email",sharedPreferenceConfig.readAgentEmail());
                Log.e("phone",sharedPreferenceConfig.readAgentPhone());
                Log.e("latitude",latitude);
                Log.e("longitude",longitude);
                Log.e("pass",sharedPreferenceConfig.readAgentPassword());
                Log.e("aadhar",sharedPreferenceConfig.readAgentAadhar());
                Log.e("bank name",sharedPreferenceConfig.readAgentBankName());
                Log.e("bank num",sharedPreferenceConfig.readAgentBankNumber());
                Log.e("bank ifsc",sharedPreferenceConfig.readAgentBankIfsc());
                Log.e("gender",sharedPreferenceConfig.readAgentGender());
                Log.e("addres",address);
                Log.e("dob",sharedPreferenceConfig.readAgentDob());

                apiService=APIUrl.getApiClient().create(ApiService.class);
                Call<Status> call=apiService.agentRegistration(
                        sharedPreferenceConfig.readAgentEmail(),
                        sharedPreferenceConfig.readAgentPhone(),
                        latitude,
                        longitude,
                        sharedPreferenceConfig.readAgentPassword(),
                        sharedPreferenceConfig.readAgentAadhar(),
                        sharedPreferenceConfig.readAgentBankName(),
                        sharedPreferenceConfig.readAgentBankNumber(),
                        sharedPreferenceConfig.readAgentBankIfsc(),
                        "null",
                        sharedPreferenceConfig.readAgentGender(),
                        sharedPreferenceConfig.readAgentPic(),
                        address,
                        sharedPreferenceConfig.readAgentDob());
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                        progressDialog.dismiss();
                        if (response.body()==null){
                            Toast.makeText(getApplicationContext(),"agent not registered",Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(),"status "+response.body().getMessage(),Toast.LENGTH_LONG).show();
                        Intent agentLogin=new Intent(PaymentActivity.this, AgentLoginActivity.class);
                        startActivity(agentLogin);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        progressDialog.dismiss();
                      Toast.makeText(getApplicationContext(),"some error occurred",Toast.LENGTH_LONG).show();
                    }
                });


            }

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public static String orderedid()
    {
        int randomPin   =(int)(Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        return otp;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Intent agentSignUp=new Intent(PaymentActivity.this,MainActivity.class);
            startActivity(agentSignUp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent agentSignUp=new Intent(PaymentActivity.this,MainActivity.class);
        startActivity(agentSignUp);
        finish();
    }
}
