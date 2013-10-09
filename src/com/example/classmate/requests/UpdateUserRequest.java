package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import android.content.Context;

import com.example.classmate.common.Conf;

@XHttpAttr(method = XHttpMethod.POST)
public class UpdateUserRequest extends BaseRequest {

    public UpdateUserRequest(Context context) {
        super(context);
        addParam("action", "update");
        addParam("openid", mOpenId);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        System.err.println(content);
        return null;
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "user.jsp";
    }
}
