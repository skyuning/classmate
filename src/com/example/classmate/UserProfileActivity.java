package com.example.classmate;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.Utils;
import com.example.classmate.requests.UpdateUserRequest;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class UserProfileActivity extends Activity {

    private static final int REQUEST_CHOOSE_PHOTO = 11;

    @ViewInject(id = R.id.icon)
    private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CHOOSE_PHOTO)
                && (resultCode == Activity.RESULT_OK)) {
            String path = Utils.uri2Path(this, data.getData());
            File f = new File(path);
            Toast.makeText(this, "" + f.length(), Toast.LENGTH_LONG).show();
            try {
                XHttpClient.sendRequest(new UpdateUserRequest(path),
                        new XHttpCallbacks.DebugHttpCallback(this));
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "上传失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}