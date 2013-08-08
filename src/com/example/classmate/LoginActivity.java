package com.example.classmate;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // String scope = "get_simple_userinfo, add_topic";
        // mTencent = Tencent.createInstance(Conf.APPID, this);
        // mTencent.login(this, scope, this);
        // TencentOpenAPI2.logIn(this, "", scope, Conf.APPID, "_self",
        // "auth://tauth.qq.com/", null, null);
    };
}