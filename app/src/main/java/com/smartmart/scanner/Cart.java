package com.smartmart.scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    ArrayAdapter adapter;
    ListView listView;
    ArrayList<String> itemlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);
        listView.setAdapter(adapter);

        Log.d("test", itemlist.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);
        return true;
    }

    public void addItems(String item){
        itemlist.add(item);
        Log.d("test", item);
        Log.d("test", itemlist.toString());

    }
}
