package com.smartmart.scanner;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class googleMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps"));
        startActivity(intent);
    }
}
