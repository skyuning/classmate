package com.example.classmate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements
        OnCheckedChangeListener, OnClickListener {
    
    @ViewInject(id = R.id.radio_news)
    private RadioButton mRadioNews;

    @ViewInject(id = R.id.radio_holiday)
    private RadioButton mRadioHoliday;

    @ViewInject(id = R.id.radio_users)
    private RadioButton mRadioUsers;

    @ViewInject(id = R.id.radio_setting)
    private RadioButton mRadioSetting;
    
    public static class Tab {
        public String title;
        public Class<?> fragmentClazz;
        public int iconOnId;
        public int iconOffId;
    }

    private static List<Tab> mTabs;
    static {
        mTabs = new ArrayList<Tab>();
        Tab tab;

        tab = new Tab();
        tab.title = "新鲜事";
        tab.fragmentClazz = NewsesFragment.class;
        tab.iconOnId = R.drawable.tab_news_on;
        tab.iconOffId = R.drawable.tab_news_off;
        mTabs.add(tab);

        tab = new Tab();
        tab.title = "纪念日";
        tab.fragmentClazz = HolidaysFragment.class;
        tab.iconOnId = R.drawable.tab_holiday_on;
        tab.iconOffId = R.drawable.tab_holiday_off;
        mTabs.add(tab);

        tab = new Tab();
        tab.title = "通讯录";
        tab.fragmentClazz = UsersFragment.class;
        tab.iconOnId = R.drawable.tab_users_on;
        tab.iconOffId = R.drawable.tab_users_off;
        mTabs.add(tab);

        tab = new Tab();
        tab.title = "设置";
        tab.fragmentClazz = SettingFragment.class;
        tab.iconOnId = R.drawable.tab_setting_on;
        tab.iconOffId = R.drawable.tab_setting_off;
        mTabs.add(tab);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_frame);
        
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mRadioNews.setOnClickListener(this);
        mRadioHoliday.setOnClickListener(this);
        mRadioUsers.setOnClickListener(this);
        mRadioSetting.setOnClickListener(this);
        mRadioNews.setOnCheckedChangeListener(this);
        mRadioHoliday.setOnCheckedChangeListener(this);
        mRadioUsers.setOnCheckedChangeListener(this);
        mRadioSetting.setOnCheckedChangeListener(this);
        
        mRadioNews.setChecked(true);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getBooleanExtra("session_timeout", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        String fragmentName = (String) buttonView.getTag();
        Fragment fragment = fm.findFragmentByTag(fragmentName);
        if (fragment != null) {
            if (isChecked)
                ft.attach(fragment);
            else
                ft.detach(fragment);
        } else {
            if (isChecked) {
                try {
                    fragment = (Fragment) Class.forName(fragmentName)
                            .getConstructor().newInstance();
                    ft.add(R.id.content, fragment, fragmentName);
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
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
    }
}