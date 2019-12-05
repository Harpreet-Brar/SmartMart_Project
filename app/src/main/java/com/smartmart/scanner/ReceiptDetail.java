package com.smartmart.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptDetail extends AppCompatActivity {
    TextView name, date, GST, PST, Total;
    ListView detailView,detailPrice;
    public static String id;
    ArrayList Rnamelist = new ArrayList ();
    ArrayList Rpricelist = new ArrayList ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_receipt_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById (R.id.name);
        date = findViewById (R.id.date);
        detailView = findViewById (R.id.detailview);
        detailPrice = findViewById (R.id.detailprice);
        GST = findViewById (R.id.GST);
        PST = findViewById (R.id.PST);
        Total = findViewById (R.id.Total);

        Intent intent = getIntent ();
        String newname = intent.getStringExtra ("name");
        String newdate = intent.getStringExtra ("date");

        name.setText (newname);
        date.setText (newdate);


        String url = "http://Capstone.braronline.wmdd.ca/receiptid?name='" + newname + "'&date='" + newdate + "'";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (url, response -> {
            for (int i = 0; i < response.length (); i++) {
                try {
                    id = response.getJSONObject (i).getString ("id");

                } catch (JSONException e) {
                    Log.e ("Volley", e.toString ());
                    e.printStackTrace ();
                }
            }
            fetch ();
        },
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e ("Volley", error.toString ());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (jsonArrayRequest);
    }

    protected void fetch(){
    //receipt detail call using fetch id from reciept table


    String newurl = "http://Capstone.braronline.wmdd.ca/selectedreceipt?id='"+id+"'";

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest (newurl, response -> {
            Double tot = 0.0;
            Log.d ("check", "fetch: "+response);
            for (int i = 0; i < response.length(); i++) {
                try {
                    String Rname = response.getJSONObject(i).getString ("item");
                    String Rprice = response.getJSONObject(i).getString ("price");


                    Rnamelist.add(Rname);
                    Rpricelist.add (Rprice);
                    tot = tot + Double.valueOf (Rprice);
                    Double totalgst = (tot/100)*5;
                    Double totalpst = (tot/100)*7;
                    Double totalbill = (tot + totalgst + totalpst);
                    GST.setText("GST  %5  :  "+ String.format ("%.2f",totalgst));
                    PST.setText("PST  %7  :  "+ String.format ("%.2f",totalpst));
                    Total.setText("Total :  "+ String.format ("%.2f",totalbill));

                } catch (JSONException e) {
                    Log.e("check", e.toString());
                    e.printStackTrace();
                }
            }
            final ArrayAdapter adapter = new ArrayAdapter (this,
                    android.R.layout.simple_list_item_1, Rnamelist);
            detailView.setAdapter(adapter);
            final ArrayAdapter adapter2 = new ArrayAdapter (this,
                    android.R.layout.simple_list_item_1, Rpricelist);
            detailPrice.setAdapter(adapter2);
        },
                error -> Log.e("Volley", error.toString()));
        RequestQueue reqdetail = Volley.newRequestQueue(this);
        reqdetail.add(jsonArrayReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}
