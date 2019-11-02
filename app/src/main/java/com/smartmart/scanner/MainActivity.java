package com.smartmart.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.smartmart.scanner.ui.home.HomeFragment;
import com.smartmart.scanner_module.BarcodeScannerActivity;
import com.smartmart.scanner_module.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener {
    private static final int REQUEST = 1208;
    private TextView title;
    private TextView detail;
    private Button Scanbutton;
    private Button ScantohomeButton;
    String reslt;
    Cart cart = new Cart();
    ArrayList<String> items = new ArrayList<>();
    Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.scan_title);
        detail = findViewById(R.id.scan_detail);
        Scanbutton = findViewById(R.id.scan_button);
        Scanbutton.setVisibility(View.INVISIBLE);



        addBarcodeReaderFragment();
    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }



    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeScannerActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);
        return true;
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.scan_button:
                count = count+ 1;
                cart.addItems(reslt);
                break;
        }
    }
}


