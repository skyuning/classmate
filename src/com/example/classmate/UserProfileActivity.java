package com.example.classmate;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks.DefaultHttpCallback;
import org.xframe.http.XHttpClient;
import org.xframe.utils.ImageUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.classmate.common.Conf;
import com.example.classmate.common.Test;
import com.example.classmate.common.Utils;
import com.example.classmate.requests.UpdateUserRequest;
import com.example.classmate.requests.UserInfoRequest;
import com.example.classmate.utils.ImageLoader;

public class UserProfileActivity extends Activity {

    private static final int REQUEST_CHOOSE_PHOTO = 11;

    private String mIconPath;
    @ViewInject(id = R.id.icon)
    private ImageView mIcon;
    
    @ViewInject(id = R.id.name)
    private EditText mNameView;
    
    @ViewInject(id = R.id.birth)
    private EditText mBirthView;
    
    @ViewInject(id = R.id.address)
    private EditText mAddressView;
    
    @ViewInject(id = R.id.phone)
    private EditText mPhoneView;
    
    @ViewInject(id = R.id.qq)
    private EditText mQQView;
    
    @ViewInject(id = R.id.email)
    private EditText mEmailView;
    
    @ViewInject(id = R.id.work)
    private EditText mWorkView;
    
    @ViewInject(id = R.id.ok)
    private Button mOKBtn;
    
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
        
        mOKBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onOKBtnClicked(v);
            }
        });
        
        asyncLoadProfile();
    }
    
    private void onOKBtnClicked(View v) {
        UpdateUserRequest request = new UpdateUserRequest(UserProfileActivity.this, Test.openid);
        request.addParam("name", mNameView.getText().toString());
        request.addParam("birth", mBirthView.getText().toString());
        request.addParam("cellphone", mPhoneView.getText().toString());
        request.addParam("address", mAddressView.getText().toString());
        if (mIconPath != null) {
            try {
                request.addMultipartImage("photo", mIconPath);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        XHttpClient.sendRequest(request, new DefaultHttpCallback());
    }
    
    private void asyncLoadProfile() {
        XHttpClient.sendRequest(new UserInfoRequest(this), new DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                super.onSuccess(result);
                JSONObject jo = (JSONObject) result.data;
                mNameView.setText(jo.optString("u_name"));
                mBirthView.setText(jo.optString("u_birthday"));
                mAddressView.setText(jo.optString("u_address"));
                mPhoneView.setText(jo.optString("u_cellphone"));
                mQQView.setText(jo.optString("u_qq"));
                mEmailView.setText(jo.optString("u_email"));
                mWorkView.setText(jo.optString("u_work"));
                if (! Utils.isEmptyString(jo.optString("u_photo"))) {
                    mIcon.setTag(Conf.IMAGE_ROOT + jo.optString("u_photo"));
                    ImageLoader.loadImage(UserProfileActivity.this, mIcon);
                    mIconPath = null;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh)
            asyncLoadProfile();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CHOOSE_PHOTO)
                && (resultCode == Activity.RESULT_OK)) {
            String path = Utils.uri2Path(this, data.getData());
            File f = new File(path);
            if (! f.exists())
                return;
            Bitmap bm = ImageUtils.getThumb(path, 200, 200);
            mIcon.setImageBitmap(bm);
            mIconPath = path;
        }
    }
}