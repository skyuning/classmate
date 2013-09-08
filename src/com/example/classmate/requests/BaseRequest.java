package com.example.classmate.requests;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xframe.http.XHttpRequest;

import com.example.classmate.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public abstract class BaseRequest extends XHttpRequest {
    
    protected Context mContext;
    protected String mToken;
    protected String mOpenId;
    
    public BaseRequest(Context context) {
        mContext = context;
        SharedPreferences sp = context.getSharedPreferences(
                "session", Context.MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mOpenId = sp.getString("openid", "");
        addParam("token", mToken);
    }
    
    @Override
    public HttpUriRequest buildRequest() throws IOException {
        if (getAttr().method() != XHttpMethod.POST)
            return super.buildRequest();
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(mParams, "utf-8");
        HttpPost post = new HttpPost(buildUrl() + "?" + EntityUtils.toString(entity));
        if (null != mMultipartEntity)
            post.setEntity(mMultipartEntity);
        return post;
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        JSONObject jo = new JSONObject(content);
        int status = 0;
        if (jo.has("status"))
            jo.getInt("status");
        else
            jo.getInt("errCode");
        
        if (status == 0) {
            return jo.opt("message");
        } else if (status == -2) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("session_timeout", true);
            mContext.startActivity(intent);
            throw new Exception("invalid token");
        } else {
            throw new Exception("unknown status: " + status + "\n" + content);
        }
    }
}
