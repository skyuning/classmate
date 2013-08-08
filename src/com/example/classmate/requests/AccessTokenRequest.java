package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpRequest;

public class AccessTokenRequest extends XHttpRequest {
    
    public AccessTokenRequest() {
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        return null;
    }

    @Override
    protected String buildUrl() {
        String url = "https://openmobile.qq.com/oauth2.0/m_authorize";
        return url;
    }

}
