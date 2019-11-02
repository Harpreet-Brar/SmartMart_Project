package com.smartmart.scanner.ui.receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.smartmart.scanner.R;

public class ReceiptFragment extends Fragment {

    private ReceiptViewModel receiptViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        receiptViewModel =
                ViewModelProviders.of(this).get(ReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_receipt, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        receiptViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}