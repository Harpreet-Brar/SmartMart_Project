package com.smartmart.scanner;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.smartmart.scanner.client.PAYPAL_CLIENT_ID;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    private static final  int payment_request_code = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    private ConstraintSet constraintSet = new ConstraintSet();

    private MenuItem menuItem;
    private MenuItem newmenuItem;
    public static ArrayList<String> itemlist = new ArrayList<>();
    RecyclerView cartlist;
    public static ArrayList<String> pricelist = new ArrayList<>();
    public static ArrayList<String> quantitylist = new ArrayList<>();
    public static ArrayList<String> editlist = new ArrayList<String>();
    static Double totalbill = 0.0;
    static Double totalgst = 0.0;
    static Double totalpst = 0.0;
    static Double totalgrand = 0.0;

    private ListView editView;
    private TextView gst,pst,total;


    protected void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(this);
        Button confirmButton = findViewById(R.id.confirmpay);
        confirmButton.setOnClickListener(this);
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        editView = findViewById (R.id.editviewList);
        editView.setVisibility (View.INVISIBLE);
        gst = findViewById (R.id.gstbtn);
        pst = findViewById (R.id.pstbtn);
        total = findViewById (R.id.totalbtn);

        totalgst = (totalbill/100)*5;
        totalpst = (totalbill/100)*7;
        totalgrand = (totalbill + totalgst + totalpst);
        gst.setText("GST  %5  :  "+ String.format ("%.2f",totalgst));
        pst.setText("PST  %7  :  "+ String.format ("%.2f",totalpst));
        total.setText("Total :  "+ String.format ("%.2f",totalgrand));
        cartlist = findViewById(R.id.cartlist);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemlist,pricelist,quantitylist);
        cartlist.setLayoutManager(new LinearLayoutManager (this));
        cartlist.setAdapter(recyclerAdapter);
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        payButton.setOnClickListener(view -> processPayment());
        confirmButton.setOnClickListener(view -> {
            ConstraintLayout constraint = findViewById(R.id.cartviewcart);
            constraintSet.clone(Cart.this, R.layout.activity_confirm);
            Transition transition = new ChangeBounds ();
            transition.setInterpolator(new AnticipateOvershootInterpolator (1.0f));
            transition.setDuration(1000);

            TransitionManager.beginDelayedTransition(constraint, transition);
            constraintSet.applyTo(constraint);
        });


    }

    @Override
    protected void onStart() {
        super.onStart ();
        final ArrayAdapter adapter = new ArrayAdapter (this,
                android.R.layout.simple_list_item_1, editlist);
        editView.setAdapter(adapter);

        editView.setOnItemClickListener((parent, view, position, id) -> {
            final String item = (String) parent.getItemAtPosition(position);
            view.animate().setDuration(1000).alpha(0)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            itemlist.remove (position);
                            totalbill -= Double.valueOf (pricelist.get (position));
                            pricelist.remove (position);
                            quantitylist.remove (position);
                            editlist.remove(item);
                            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemlist,pricelist,quantitylist);
                            cartlist.setLayoutManager(new LinearLayoutManager (getApplicationContext ()));
                            cartlist.setAdapter(recyclerAdapter);
                            adapter.notifyDataSetChanged();
                            view.setAlpha(1);
                            totalgst = (totalbill/100)*5;
                            totalpst = (totalbill/100)*7;
                            totalgrand = (totalbill + totalgst + totalpst);
                            gst.setText("GST  %5  :  "+ String.format ("%.2f",totalgst));
                            pst.setText("PST  %7  :  "+ String.format ("%.2f",totalpst));
                            total.setText("Total :  "+ String.format ("%.2f",totalgrand));
                            total.setText ("Total : " + String.format ("%f",totalbill));
                        }
                    });
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

            menuItem.setVisible(false);
            newmenuItem.setVisible(true);
            editView.setVisibility (View.VISIBLE);

        }
        else if(itemId == R.id.save)
        {

            editView.setVisibility (View.INVISIBLE);
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

    public void addItems(String item1, Double item2,Integer item3){
        itemlist.add(item1);
        pricelist.add (String.valueOf ((item2*item3)));
        quantitylist.add (item3.toString ());
        totalbill = totalbill+(item2*item3);
        editlist.add ("X");

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
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalgrand), "CAD",
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
                Log.d ("check", "onActivityResult: back-clicked");
            }
        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Log.d ("check", "onActivityResult: Extra invalid");


    }

    public void addReceipt() {
        String url = "http://Capstone.braronline.wmdd.ca/info?ID=";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d ("check", "onResponse: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }
}


