package com.smartmart.scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Cart extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter adapter;
    ListView listView;
    private MenuItem menuItem;
    private MenuItem newmenuItem;
    static ArrayList<String> itemlist = new ArrayList<>();
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
            case R.id.scan_button:
                break;
            case R.id.cancelButton:

                Intent i = new Intent(getApplicationContext(), BottomNav.class);
                startActivity(i);
                break;

        }
    }
}

