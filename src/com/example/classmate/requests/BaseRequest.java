package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.xframe.http.XHttpRequest;

import com.example.classmate.MainActivity;

import android.content.Context;
import android.content.Intent;

public abstract class BaseRequest extends XHttpRequest {
    
    private Context mContext;
    
    public BaseRequest(Context context) {
        mContext = context;
        String token = context.getSharedPreferences(
                "session", Context.MODE_PRIVATE).getString("token", "");
        addParam("token", token);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        JSONObject jo = new JSONObject(content);
        int status = jo.getInt("status");
        if (status == 0) {
            return jo.get("message");
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
