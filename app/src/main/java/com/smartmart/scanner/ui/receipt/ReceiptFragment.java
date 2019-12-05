package com.smartmart.scanner.ui.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smartmart.scanner.R;
import com.smartmart.scanner.ReceiptDetail;
import com.smartmart.scanner.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptFragment extends Fragment {
    public ListView recieptList;
    public ListView datelist;
    public static TextView test;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_receipt, container, false);
        recieptList = root.findViewById(R.id.recieptlist);
        datelist = root.findViewById(R.id.recieptdate);
        return root;

    }

    @Override
    public void onStart() {
        super.onStart ();
        loadReceipt ();
    }

    @Override
    public void onResume() {
        super.onResume ();


        recieptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                String dateid = (String) datelist.getItemAtPosition (position);

                Intent intent = new Intent (getContext (), ReceiptDetail.class);
                intent.putExtra ("name",name);
                intent.putExtra ("date",dateid);
                startActivity (intent);
            }
        });

        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String dateid = (String) parent.getItemAtPosition(position);
                String name = (String) recieptList.getItemAtPosition (position);
                Intent intent = new Intent (getContext (), ReceiptDetail.class);
                intent.putExtra ("name",name);
                intent.putExtra ("date",dateid);
                startActivity (intent);
            }
        });


    }

    public void loadReceipt(){

         ArrayList recptlist = new ArrayList ();
         ArrayList dtlist = new ArrayList ();
        String url = "http://Capstone.braronline.wmdd.ca/receiptlist";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (url, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    String name = response.getJSONObject(i).getString ("name");
                    String date = response.getJSONObject(i).getString ("date");
                    recptlist.add(name);

                    dtlist.add (date.toString ());

                } catch (JSONException e) {
                    Log.e("Volley", e.toString());
                    e.printStackTrace();
                }
            }
            final ArrayAdapter adapter = new ArrayAdapter (getContext (),
                    android.R.layout.simple_list_item_1, recptlist);
            recieptList.setAdapter(adapter);
            final ArrayAdapter adapter2 = new ArrayAdapter (getContext (),
                    android.R.layout.simple_list_item_1, dtlist);
            datelist.setAdapter(adapter2);
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext ());
        requestQueue.add(jsonArrayRequest);
    }

}