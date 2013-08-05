package com.example.classmate;

import org.xframe.http.XHttpCallbacks.DefaultHttpCallback;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import com.example.classmate.requests.LoginRequest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        XHttpRequest loginRequest = new LoginRequest("1234567890", "1234567789");
        loginRequest.addParam("name", "林云");
        XHttpClient.sendRequest(loginRequest, new DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                if (result.data instanceof Boolean) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (result.data instanceof String) {
                    Toast.makeText(LoginActivity.this, "登陆失败\n" + result.data,
                            Toast.LENGTH_LONG).show();
                }
            };
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
