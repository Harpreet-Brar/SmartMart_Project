package com.smartmart.scanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.Request;

import static com.smartmart.scanner.client.PAYPAL_CLIENT_ID;
import static com.paypal.android.sdk.bm.S;
import static com.paypal.android.sdk.bm.s;


public class Cart extends AppCompatActivity implements View.OnClickListener {
    private static final  int payment_request_code = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    ArrayAdapter adapter;
    ListView listView;
    private MenuItem menuItem;
    private MenuItem newmenuItem;
    static ArrayList<String> itemlist = new ArrayList<>();

    protected void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listView = findViewById(R.id.list_view);
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(this);
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);
        listView.setAdapter(adapter);

        Log.d("test", itemlist.toString());
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String message = "You click fragment ";

        if(itemId == R.id.edit)
        {
//            message += "Search menu";

            menuItem.setVisible(false);
            newmenuItem.setVisible(true);

        }
        else if(itemId == R.id.save)
        {
//            message += "Search menu";

            newmenuItem.setVisible(false);
            menuItem.setVisible(true);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem = menu.findItem(R.id.edit);
        newmenuItem = menu.findItem(R.id.save);
        newmenuItem.setVisible(false);
        return true;
    }

    public void addItems(String item){
//        itemlist.add(item);
        Log.d("test", item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:
                Intent i = new Intent(getApplicationContext(), BottomNav.class);
                startActivity(i);
                break;

        }
    }

    //new code
    private void processPayment(){
        //amount =  edtamount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(10)), "USD",
                "pay now" ,PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,payment_request_code);


    }

    protected  void onActivityResult(int requestCode , int resultCode, Intent data){
        if(requestCode == payment_request_code){
            if (resultCode == RESULT_OK){
                PaymentConfirmation Confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (Confirmation != null){
                    try {
                        String paymentDetails = Confirmation.toJSONObject(). toString(4);

                        startActivity(new Intent(this,PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                //.putExtra("paymentAmount",amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                super.onActivityResult(requestCode,resultCode,data);
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"cancel",Toast.LENGTH_SHORT).show();
            }
        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this,"cancel",Toast.LENGTH_SHORT).show();

    }
}


