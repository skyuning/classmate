package com.example.classmate;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity {

    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        TabSpec tabSpec;
        
        tabSpec = mTabHost.newTabSpec("classmate_list");
        tabSpec.setIndicator("同学列表");
        tabSpec.setContent(R.id.classmate_list_fragment);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec("notify_list");
        tabSpec.setIndicator("通知列表");
        tabSpec.setContent(R.id.notify_list_fragment);
        mTabHost.addTab(tabSpec);
    }
}
