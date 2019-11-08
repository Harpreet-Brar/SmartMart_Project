package com.smartmart.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.smartmart.scanner.Cart;
import com.smartmart.scanner_module.BarcodeScannerActivity;
import com.smartmart.scanner_module.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener {
    private static final int REQUEST = 1208;
    private TextView title;
    private TextView detail;
    private Button Scanbutton;
    private Button ScantohomeButton;
    private View view;
    private FragmentActivity myContext;
    static String reslt;
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
        Scanbutton.setVisibility(View.INVISIBLE);



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

            title.setText("On Activity Result");
            detail.setText(barcode.rawValue);
            reslt = barcode.rawValue;
        }

    }

    @Override
    public void onScanned(Barcode barcode) {
        title.setText("Barcode value from fragment");
        detail.setText(barcode.rawValue);
        Scanbutton.setVisibility(View.VISIBLE);
        Scanbutton.setOnClickListener(this);
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

   

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_button:
                count = count+ 1;
                cart.addItems(reslt.toString());
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

}



