package com.example.classmate;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.classmate.common.Test;
import com.example.classmate.requests.LoginRequest;

public class LoginActivity extends Activity implements OnClickListener {

    @ViewInject(id = R.id.login)
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        XHttpRequest request = new LoginRequest(Test.openid, Test.token);
        request.addParam("name", "ttt");
        System.err.println("token: " + Test.token);
        XHttpClient.sendRequest(request, new XHttpCallbacks.DebugHttpCallback(this) {
            @Override
            public void onSuccess(AHttpResult result) {
                super.onSuccess(result);
                SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
                sp.edit().putString("token", Test.token).commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFaild(AHttpResult result) {
                super.onFaild(result);
                Toast.makeText(LoginActivity.this, result.e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // String scope = "get_simple_userinfo, add_topic";
        // mTencent = Tencent.createInstance(Conf.APPID, this);
        // mTencent.login(this, scope, this);
        // TencentOpenAPI2.logIn(this, "", scope, Conf.APPID, "_self",
        // "auth://tauth.qq.com/", null, null);
    };
}