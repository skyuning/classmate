package com.example.classmate;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.Conf;
import com.example.classmate.requests.GetQQInfoRequest;
import com.example.classmate.requests.LoginRequest;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;

public class LoginActivity extends BaseActivity implements OnClickListener {

    @ViewInject(id = R.id.title)
    private TextView mTitle;

    @ViewInject(id = R.id.login)
    private Button mLoginBtn;
    
    @ViewInject(id = R.id.progressBar1)
    private ProgressBar mProgressBar;

    private QQLoginer mQQLoginer;
    
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mLoginBtn.setOnClickListener(this);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQQLoginer != null)
            mQQLoginer.unregister();
    }

    @Override
    public void onClick(View v) {
        mQQLoginer = new QQLoginer(this, new LoginCallback());
        mQQLoginer.gotoLoginPage();
        
//        if (BuildConfig.DEBUG) {
//            String openid = "2047ADAE95201E43897DEC795A210CC2";
//            String token = "0B7BB7C8EC0B45CF9058739A56CDD001";
//            JSONObject userInfo = new JSONObject();
//            try {
//                userInfo.put("nickname", "雲");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            login(openid, token, userInfo);
//        }
    }

    private class LoginCallback implements Callback {
        @Override
        public void onCancel(int arg0) {
        }

        @Override
        public void onFail(int arg0, String arg1) {
        }

        @Override
        public void onSuccess(final Object obj) {
            final String openId = ((OpenId) obj).getOpenId();
            final String token = mQQLoginer.getAccessToken();
            System.err.println("token: " + token);

            final GetQQInfoRequest request = new GetQQInfoRequest(Conf.APPID, token, openId);
            final XHttpCallback callback = new XHttpCallbacks.DefaultHttpCallback() {
                @Override
                public void onSuccess(AHttpResult result) {
                    JSONObject infoJo = (JSONObject) result.data;
                    login(openId, token, infoJo);
                };
            };
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog = new ProgressDialog(LoginActivity.this);
                    mProgressDialog.setMessage("正在登录");
                    mProgressDialog.show();
                    XHttpClient.sendRequest(request, callback);
                }
            });
        };
    }

    private void login(final String openid, final String token, JSONObject userInfo) {
        XHttpRequest request = new LoginRequest(openid, token);
        request.addParam("name", userInfo.optString("nickname"));
        request.addParam("sex", userInfo.optString("gender"));
        
        XHttpClient.sendRequest(request, new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                super.onSuccess(result);
                
                mProgressDialog.dismiss();
                
                getSharedPreferences("session", MODE_PRIVATE).edit()
                .putString("token", token)
                .putString("openid", openid)
                .commit();
                
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFaild(AHttpResult result) {
                super.onFaild(result);
                Toast.makeText(LoginActivity.this,
                        result.e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

}