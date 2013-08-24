package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.GET)
public class UserInfoRequest extends BaseRequest {

    public UserInfoRequest(Context context) {
        super(context);
        addParam("action", "read");
    }

    @Override
    protected String buildUrl() {
        String url = Conf.HOST + Conf.PATH + "user";
        return url;
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        return super.handleResponse(response, content);
    }

}
