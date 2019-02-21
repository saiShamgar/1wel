package com.shamgar.sss.wel.UI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shamgar.sss.wel.Api.APIUrl;
import com.shamgar.sss.wel.Api.ApiService;
import com.shamgar.sss.wel.Models.Status;
import com.shamgar.sss.wel.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerVerification extends AppCompatActivity {

    private EditText editCustomerId,edtProviderID,edtPurchaseAmount;
    private Button btnVerify;

    private ApiService apiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verification);

        getSupportActionBar().setTitle("Customer Verification");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(this);
        editCustomerId=findViewById(R.id.editCustomerId);
        edtProviderID=findViewById(R.id.edtProviderID);
        edtPurchaseAmount=findViewById(R.id.edtPurchaseAmount);
        btnVerify=findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editCustomerId.getText().toString())){
                    editCustomerId.setError("Field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edtProviderID.getText().toString())){
                    edtProviderID.setError("Field cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(edtPurchaseAmount.getText().toString())){
                    edtPurchaseAmount.setError("Field cannot be empty");
                    return;
                }

                progressDialog.setTitle("please wait");
                progressDialog.setMessage("verifying details..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                apiService= APIUrl.getApiClient().create(ApiService.class);
                Call<Status> call=apiService.customerVerification(
                        editCustomerId.getText().toString(),
                        edtProviderID.getText().toString(),
                        edtPurchaseAmount.getText().toString());
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
                    }
                });

            }
        });

    }
}
