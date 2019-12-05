package com.smartmart.scanner;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class StoreSearchActivity extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);

        Resources resources = getResources();
        TabHost tabHost = getTabHost();

        // Android tab
        Intent intentMaps = new Intent ().setClass(this, MapsViewActivity.class);
        TabSpec tabSpecMap = tabHost
                .newTabSpec("Map View")
                .setIndicator("")
                .setContent(intentMaps);
        tabHost.addTab(tabSpecMap);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(2);
    }

}