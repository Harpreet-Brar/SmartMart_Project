package com.smartmart.scanner;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

public class BarcodeFragment extends Fragment implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String name = BarcodeFragment.class.getSimpleName();
    private TextView ScanedText;
    private BarcodeReaderFragment barcodeBlock;

    public static BarcodeFragment newInstance() {
        Bundle args = new Bundle();
        BarcodeFragment fragment = new BarcodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barcode, container, false);
        ScanedText = view.findViewById(R.id.scan_detail);
        barcodeBlock = (BarcodeReaderFragment) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeBlock.setListener(this);

        return view;
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(name, "onScanned: " + barcode.displayValue);
        barcodeBlock.playBeep();
        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
        ScanedText.setText(barcode.displayValue);
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(name, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        Toast.makeText(getActivity(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(name, "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}