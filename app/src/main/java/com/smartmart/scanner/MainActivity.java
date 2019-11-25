package com.smartmart.scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.smartmart.scanner.ui.account.AccountFragment;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener{
    private static final int REQUEST = 1208;
    public static TextView title;
    public static TextView detail;
    private Button Scanbutton;
    private Button plus;
    private Button minus;
    public static TextView countitem;

    private Button ScantohomeButton;
    private View view;
    private FragmentActivity myContext;
    static String reslt;
    private ConstraintLayout defaulttext;
    private ConstraintLayout iteminfo;
    private static String Iname;
    private static Double Iprice;
    Cart cart = new Cart();
    ArrayList<String> items = new ArrayList<>();
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

        //detail.setText(newList.get(1).toString());

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



    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeScannerActivity.getLaunchIntent(myContext, true, false);
        startActivityForResult(launchIntent, REQUEST);
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
        Request.Request(barcode.rawValue);
        title.setText(Iname);
        detail.setText(String.valueOf(Iprice));
        Log.d("aa", "onScanned: "+barcode.rawValue);
        if(Iname!=null) {
            Log.d("aa", "onScanned: "+Iname);
            defaulttext.setVisibility(View.INVISIBLE);
            iteminfo.setVisibility(View.VISIBLE);
            Scanbutton.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            Cart.addItems(Iname,Iprice);
            Iname=null;
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
                String nameitem = title.getText().toString();
                //cart.addItems(nameitem,Value);
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

    public static void recieve(String name,Double price){
        Iname = name;
        Iprice = price;
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

}



