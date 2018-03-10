package com.timeandtidestudio.try_currency;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    Context mCtx;
    RequestQueue reqQueue;

    EditText etFrom,etTo,etAmount;
    TextView tvResult;
    String from,to,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = this;
        reqQueue = Volley.newRequestQueue(mCtx);

        etFrom = findViewById(R.id.etFrom);
        etFrom.addTextChangedListener(this);
        etTo = findViewById(R.id.etTo);
        etTo.addTextChangedListener(this);
        etAmount = findViewById(R.id.etAmount);
        etAmount.addTextChangedListener(this);
        tvResult = findViewById(R.id.textView);

    }

    boolean validateForm(){
        boolean formValidity = false;

        from=etFrom.getText().toString();
        to=etTo.getText().toString();
        amount=etAmount.getText().toString();

        if(TextUtils.isEmpty(from)==false&&TextUtils.isEmpty(to)==false&&TextUtils.isEmpty(amount)==false){
            formValidity=true;
        }
        return formValidity;
    }

    void downloadAndSetRates(){
        JsonObjectRequest reqLatestRate = new JsonObjectRequest(
                Request.Method.GET,
                "https://data.fixer.io/api/latest?access_key=aa72f0c62808cc7542ab8a480f291c82&base="+from+"&symbols="+to,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double rates = response.getJSONObject("rates").getDouble(to);
                            Double dAmount = Double.parseDouble(amount);
                            Double result = dAmount*rates;
                            tvResult.setText(""+result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        if(validateForm()==true){
            reqQueue.add(reqLatestRate);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        downloadAndSetRates();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
