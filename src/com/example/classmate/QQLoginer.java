package com.example.classmate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.classmate.common.Conf;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.tencent.tauth.http.Callback;

public class QQLoginer {

    public static final String QQ_KEY = "100298602";
    public static final String QQ_SCOPE = "get_user_info,get_info,add_t,add_pic_t";
    public static final String QQ_CALLBACK = "auth://tauth.qq.com/";

    private Activity mActivity;
    private String mAccessToken;
    private Callback mCallback;
    private AuthReceiver mAuthReceiver;

    public QQLoginer(Activity activity, Callback callback) {
        mActivity = activity;
        mCallback = callback;
        
        mAuthReceiver = new AuthReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TencentOpenHost.AUTH_BROADCAST);
        mActivity.registerReceiver(mAuthReceiver, filter);
    }
    
    public void unregister() {
        mActivity.unregisterReceiver(mAuthReceiver);
    }
    
    public String getAccessToken() {
        return mAccessToken;
    }
    
    protected void gotoLoginPage() {
        TencentOpenAPI2.logIn(mActivity, "", QQ_SCOPE, Conf.APPID, "_self",
                QQ_CALLBACK, null, null);
    }

    private static final String TAG = "AuthReceiver";

    public class AuthReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle exts = intent.getExtras();
            String raw = exts.getString("raw");
            mAccessToken = exts.getString(TencentOpenHost.ACCESS_TOKEN);
            String expiresIn = exts.getString(TencentOpenHost.EXPIRES_IN);
            String errorRet = exts.getString(TencentOpenHost.ERROR_RET);
            String errorDes = exts.getString(TencentOpenHost.ERROR_DES);
            Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s",
                    raw, mAccessToken, expiresIn));
            
            if (mAccessToken != null) {
                // 用access token 来获取open id
                TencentOpenAPI.openid(mAccessToken, mCallback);
            }
            
            if (errorRet != null) {
                Log.i(TAG, "获取access token失败" + "\n错误码: " + errorRet
                        + "\n错误信息: " + errorDes);
            }
        }
    }
}
