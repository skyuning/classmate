package com.example.classmate;

import org.json.JSONException;
import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.Bundle;
import android.widget.TextView;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.utils.WindowAttr;

@WindowAttr(title = "同学详情")
public class UserDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.name)
    private TextView mNameView;
    
    @ViewInject(id = R.id.birth)
    private TextView mBirthView;
    
    @ViewInject(id = R.id.address)
    private TextView mAddressView;
    
    @ViewInject(id = R.id.phone)
    private TextView mPhoneView;
    
    @ViewInject(id = R.id.qq)
    private TextView mQQView;
    
    @ViewInject(id = R.id.email)
    private TextView mEmailView;
    
    @ViewInject(id = R.id.work)
    private TextView mWorkView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmate_detail);
        ViewAnnotation.bind(getWindow().getDecorView(), this);
        
        String classmateDetail = getIntent().getStringExtra("classmate_detail");
        JSONObject jo = null;
        try {
            jo = new JSONObject(classmateDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        mNameView.setText(jo.optString("u_name"));
        mBirthView.setText(jo.optString("u_birthday"));
        mAddressView.setText(jo.optString("u_address"));
        mPhoneView.setText(jo.optString("u_cellphone"));
        mQQView.setText(jo.optString("u_qq"));
        mEmailView.setText(jo.optString("u_email"));
        mWorkView.setText(jo.optString("u_work"));
    }
    
}
