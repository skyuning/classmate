package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest;
import org.xframe.http.XHttpRequest.XHttpMethod;

@XHttpAttr(method = XHttpMethod.GET)
public class GetQQInfoRequest extends XHttpRequest {

    public GetQQInfoRequest(String appId, String accessToken, String openid) {
        addParam("oauth_consumer_key", appId);
        addParam("access_token", accessToken);
        addParam("openid", openid);
        addParam("format", "json");
    }

    @Override
    protected String buildUrl() {
        return "https://graph.qq.com/user/get_user_info";
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        return new JSONObject(content);
    }
}
