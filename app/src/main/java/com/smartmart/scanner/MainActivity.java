package com.smartmart.scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends Fragment implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener{
    private static final int REQUEST = 1208;
    public static TextView title;
    public static TextView detail;
    private Button Scanbutton;
    private Button plus;
    private Button minus;
    public static TextView countitem;

    private View view;
    private FragmentActivity myContext;
    static String reslt;
    private ConstraintLayout defaulttext;
    private ConstraintLayout iteminfo;
    public static String name;
    public static Double price;
    public static Integer quantity;
    Cart cart = new Cart();
    Integer count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_main, container, false);

        title = view.findViewById(R.id.scan_title);
        detail = view.findViewById(R.id.scan_detail);
        Scanbutton = view.findViewById(R.id.scan_button);
        countitem = view.findViewById(R.id.countitem);
        plus = view.findViewById(R.id.pluscount);
        minus = view.findViewById(R.id.minuscount);
        iteminfo = view.findViewById(R.id.selectitem);
        defaulttext = view.findViewById(R.id.defaultview);
        iteminfo.setVisibility(View.INVISIBLE);
        defaulttext.setVisibility(View.VISIBLE);
        Scanbutton.setOnClickListener(this);

        addBarcodeReaderFragment();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(myContext, "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.KEY_CAPTURED_BARCODE);

            reslt = barcode.rawValue;
        }

    }


    @Override
    public void onScanned(Barcode barcode) {
        Request(barcode.rawValue);
        Log.d("aa", "onScanned: "+barcode.rawValue);
            Scanbutton.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
        if(name != null){
            viewDetail ();
            defaulttext.setVisibility(View.INVISIBLE);
            iteminfo.setVisibility(View.VISIBLE);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setMessage("Invalid Item. Try Again!");
            alertDialog.show();

        }

    }


    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(myContext, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }



    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_button:
                quantity = Integer.valueOf (countitem.getText().toString());
                cart.addItems(name,price,quantity);
                title.setText("");
                defaulttext.setVisibility(View.VISIBLE);
                iteminfo.setVisibility(View.INVISIBLE);
                break;
            case R.id.pluscount:
                int count = Integer.parseInt(countitem.getText().toString());
                int xx = count+1;
                countitem.setText(Integer.toString(xx));
                break;
            case R.id.minuscount:
                int countnew = Integer.parseInt(countitem.getText().toString());
                int x = countnew+1;
                countitem.setText(x);
                break;

        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String message = "You click fragment ";

        if(itemId == R.id.cart)
        {
            Intent i = new Intent(getContext(), Cart.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    public void Request(String id){
        OkHttpClient client = new OkHttpClient();
        String url;
        url = "http://Capstone.braronline.wmdd.ca/info?ID="+id;
        ArrayList list = new ArrayList();
        //Log.d("aa", "Request: "+url);
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String text = response.body().string();

                    JSONObject object = (JSONObject) new JSONTokener (text).nextValue();
                    name =(object.get("item_name").toString());
                    price = (Double)object.get("item_price");
                    Log.d ("aa", "run: "+name);


                } catch (IOException | JSONException e) {
                    Log.d ("check", "run: "+e.toString ());

                }
            }

        };

        thread.start();

    }

    public void viewDetail(){

        Log.d ("aa", "viewDetail: "+ name +"is for"+price);
        title.setText(name);
        detail.setText(String.valueOf(price));
        iteminfo.setVisibility(View.VISIBLE);
    }



}



