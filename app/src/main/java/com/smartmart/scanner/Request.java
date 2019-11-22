package com.smartmart.scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import com.smartmart.scanner.MainActivity;
import com.smartmart.scanner.Cart;
public class Request extends AppCompatActivity {
 public static ArrayList list;
 public static String price;
 public static Double p = 0.0;
 public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static void Request(String id){
            OkHttpClient client = new OkHttpClient();
            String url;
            url = "http://Capstone.braronline.wmdd.ca/info?ID="+id;
            list = new ArrayList();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Response response = client.newCall(request).execute();
                        String text = response.body().string();

                        JSONObject object = (JSONObject) new JSONTokener(text).nextValue();
                        name =(object.get("item_name").toString());
                        price = ("$"+object.get("item_price").toString());
                        p = (Double)object.get("item_price");
                        MainActivity.title.setText(name);
                        MainActivity.detail.setText(price);


                        Cart.addItems(name,p);


                    } catch (IOException | JSONException e) {

                    }
                }
            };

            thread.start();

        }

    }

