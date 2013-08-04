package com.example.classmate;

import java.lang.reflect.InvocationTargetException;

import org.xframe.annotation.ViewAnnotation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity {

    private TabHost mTabHost;

    private static final String[] tags = new String[] { "classmate_list",
            "commemoration_list", "news_list", "more" };
    private static final String[] titles = new String[] { "同学们", "纪念日", "新鲜事",
            "设置" };
    private static final Class<?>[] fragmentClasses = new Class<?>[] {
            ClassmateListFragment.class, HolidayListFragment.class,
            NewsListFragment.class, MoreFragment.class };

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

        mTabHost.setOnTabChangedListener(new _OnTabChangeListener());
        
        for (int i = 0; i < tags.length; i++) {
            tabSpec = mTabHost.newTabSpec(tags[i]);
            tabSpec.setIndicator(titles[i]);
            tabSpec.setContent(tabContentFactory);
            mTabHost.addTab(tabSpec);
        }
    }

    private class _OnTabChangeListener implements OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = null;

            for (int i = 0; i < tags.length; i++) {
                fragment = fm.findFragmentByTag(tags[i]);
                if (!tabId.equals(tags[i])) {
                    if (null != fragment)
                        ft.detach(fragment);
                } else {
                    if (fragment == null) {
                        try {
                            fragment = (Fragment) fragmentClasses[i]
                                    .getConstructor().newInstance();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        ft.add(android.R.id.tabcontent, fragment, tags[i]);
                    } else {
                        ft.attach(fragment);
                    }
                }
            }

            ft.commit();
        }
    }
}
