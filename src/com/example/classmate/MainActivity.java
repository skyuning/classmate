package com.example.classmate;

import com.example.xframe.annotation.ViewAnnotation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
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
        TabContentFactory tabContentFactory = new TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return new View(MainActivity.this);
            }
        };
        
        tabSpec = mTabHost.newTabSpec("classmate_list");
        tabSpec.setIndicator("同学们");
        tabSpec.setContent(tabContentFactory);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec("commemoration_list");
        tabSpec.setIndicator("纪念日");
        tabSpec.setContent(tabContentFactory);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec("news_list");
        tabSpec.setIndicator("新鲜事");
        tabSpec.setContent(tabContentFactory);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec("more");
        tabSpec.setIndicator("设置");
        tabSpec.setContent(tabContentFactory);
        mTabHost.addTab(tabSpec);
    }
    
    private class _OnTabChangeListener implements OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {
        }
    }
}
