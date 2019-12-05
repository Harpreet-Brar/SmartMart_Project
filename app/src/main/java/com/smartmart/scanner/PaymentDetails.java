package com.smartmart.scanner;

//import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity implements View.OnClickListener {
    TextView txtthank,txtStatus;
    Button cont,finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        txtthank = (TextView) findViewById(R.id.txtId);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        cont = (Button) findViewById (R.id.continuebtn);
        finish = (Button) findViewById (R.id.Finish);
        cont.setOnClickListener (this);
        finish.setOnClickListener (this);
        Intent intent = getIntent();
        /*try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

            txtthank.setText("Thank You");
            txtStatus.setText("Payment Confirmed");

            Log.d("aa", "showDetails: Confirm");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continuebtn:
                Intent intent = new Intent (getApplicationContext (),BottomNav.class);
                startActivity (intent);
                break;
            case R.id.Finish:
                Intent i = new Intent (getApplicationContext (),Home.class);
                startActivity (i);
                break;


        }
    }
}
