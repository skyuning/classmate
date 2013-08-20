package com.example.classmate;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.Bundle;
import android.widget.TextView;
import com.example.classmate.common.BaseActivity;

public class ClassmateDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.text)
    private TextView mTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmate_detail);
        ViewAnnotation.bind(getWindow().getDecorView(), this);
        
        String classmateDetail = getIntent().getStringExtra("classmate_detail");
        String text = "";
        try {
            JSONObject jo = new JSONObject(classmateDetail);
            Iterator<?> keyIter = jo.keys();
            while (keyIter.hasNext()) {
                String key = (String) keyIter.next();
                text += key + ": " + jo.get(key) + "\n";
            }
            mTextView.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
}
