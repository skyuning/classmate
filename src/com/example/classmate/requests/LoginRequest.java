package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.xframe.http.XHttpRequest;

import com.example.classmate.Conf;

public class LoginRequest extends XHttpRequest {
    
    public LoginRequest(String openid, String token) {
        addParam("action", "update");
        addParam("openid", openid);
        addParam("token", token);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        JSONObject jo = new JSONObject(content);
        if (jo.getInt("errCode") == 1)
            return true;
        else
            return jo.getString("errMsg");
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "user.jsp";
    }

}
