package com.shamgar.sss.wel.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Status;
import com.shamgar.sss.wel.R;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerRegistrationActivity extends AppCompatActivity implements PaymentResultListener {

    private EditText editCustomerName,edtCustomerPhone,edtrefId;
    private Button btnCustomerReg;

    private ApiService apiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        getSupportActionBar().setTitle("Customer Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog=new ProgressDialog(this);
        btnCustomerReg=findViewById(R.id.btnCustomerReg);
        editCustomerName=findViewById(R.id.editCustomerName);
        edtCustomerPhone=findViewById(R.id.edtCustomerPhone);
        edtrefId=findViewById(R.id.edtrefId);

        btnCustomerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editCustomerName.getText().toString())){
                    editCustomerName.setError("Please enter your name");
                    return;
                }
                if (TextUtils.isEmpty(edtCustomerPhone.getText().toString())){
                    edtCustomerPhone.setError("Please enter your number");
                    return;
                }
                if (edtCustomerPhone.getText().length()==10){
                    Log.e("note","do your stuff");
                    startPayment();

                }
                else {
                    edtCustomerPhone.setError("Please enter Valid number");
                }
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
            options.put("amount",100*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "1welsupport@gmail.com");
            preFill.put("contact",edtCustomerPhone.getText().toString());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try{
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            progressDialog.setTitle("please wait");
            progressDialog.setMessage("storing details into records");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            apiService= APIUrl.getApiClient().create(ApiService.class);
            Call<Status> call=apiService.customerRegistration(editCustomerName.getText().toString(),
                    edtCustomerPhone.getText().toString(),
                    edtrefId.getText().toString());
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body()==null) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "some error was occured/please register again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Status :"+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Log.e("payment Exception",e.toString());
        }

    }

    @Override
    public void onPaymentError(int i, String razorpayPaymentID) {
        Toast.makeText(getApplicationContext(),"payment error"+razorpayPaymentID,Toast.LENGTH_SHORT).show();
    }
}
