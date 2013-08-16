package com.example.classmate;

import org.xframe.annotation.ViewAnnotation;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

//    private TabHost mTabHost;

    private static final String[] tags = new String[] { "classmate_list",
            "commemoration_list", "news_list", "more" };
    private static final String[] titles = new String[] { "同学们", "纪念日", "新鲜事",
            "设置" };
    private static final Class<?>[] fragmentClasses = new Class<?>[] {
            ClassmateListFragment.class, HolidayListFragment.class,
            NewsListFragment.class, MoreFragment.class };

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int i = 0; i < tags.length; i++) {
            Tab tab = mActionBar.newTab().setText(titles[i]).setTabListener(
                            new TabListener<Fragment>(this, tags[i], fragmentClasses[i]));
            mActionBar.addTab(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        switch (mActionBar.getSelectedNavigationIndex()) {
        case 2:
            inflater.inflate(R.menu.news, menu);
            break;

        default:
            menu.clear();
            break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.publish_news:
            Intent intent = new Intent(this, PublishNewsActivity.class);
            startActivity(intent);
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TabListener<T extends Fragment> implements
            ActionBar.TabListener {

        private Fragment mFragment;
        private Class<?> mClazz;
        private String mTag;
        private Activity mActivity;

        public TabListener(Activity activity, String tag, Class<?> clazz) {
            mTag = tag;
            mClazz = clazz;
            mActivity = activity;
        }

        @Override
        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            if (null != mFragment)
                ft.detach(mFragment);
        }

        @Override
        public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            if (null == mFragment) {
                mFragment = Fragment.instantiate(mActivity, mClazz.getName());
                ft.add(android.R.id.tabcontent, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }
    };

    // mTabHost = (TabHost) findViewById(android.R.id.tabhost);
    // mTabHost.setup();
    //
    // TabSpec tabSpec;
    // TabContentFactory tabContentFactory = new TabContentFactory() {
    // @Override
    // public View createTabContent(String tag) {
    // return new View(MainActivity.this);
    // }
    // };
    //
    // mTabHost.setOnTabChangedListener(new _OnTabChangeListener());
    //
    // for (int i = 0; i < tags.length; i++) {
    // tabSpec = mTabHost.newTabSpec(tags[i]);
    // tabSpec.setIndicator(titles[i]);
    // tabSpec.setContent(tabContentFactory);
    // mTabHost.addTab(tabSpec);
    // }
    // }

    // private class _OnTabChangeListener implements OnTabChangeListener {
    // @Override
    // public void onTabChanged(String tabId) {
    // FragmentManager fm = getSupportFragmentManager();
    // FragmentTransaction ft = fm.beginTransaction();
    // Fragment fragment = null;
    //
    // for (int i = 0; i < tags.length; i++) {
    // fragment = fm.findFragmentByTag(tags[i]);
    // if (!tabId.equals(tags[i])) {
    // if (null != fragment)
    // ft.detach(fragment);
    // } else {
    // if (fragment == null) {
    // try {
    // fragment = (Fragment) fragmentClasses[i]
    // .getConstructor().newInstance();
    // } catch (IllegalArgumentException e) {
    // e.printStackTrace();
    // } catch (InstantiationException e) {
    // e.printStackTrace();
    // } catch (IllegalAccessException e) {
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // e.printStackTrace();
    // } catch (NoSuchMethodException e) {
    // e.printStackTrace();
    // }
    // ft.add(android.R.id.tabcontent, fragment, tags[i]);
    // } else {
    // ft.attach(fragment);
    // }
    // }
    // }
    //
    // ft.commit();
    // }
    // }
}