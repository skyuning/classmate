package com.example.classmate.requests;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import android.content.Context;

import com.example.classmate.common.Conf;
import com.example.classmate.common.Test;

@XHttpAttr(method = XHttpMethod.POST)
public class UpdateUserRequest extends BaseRequest {

    public UpdateUserRequest(Context context, String openid) {
        super(context);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        System.err.println(content);
        return null;
    }

    @Override
    protected String buildUrl() {
        String token = mContext.getSharedPreferences(
                "session", Context.MODE_PRIVATE).getString("token", "");
        return Conf.HOST + Conf.PATH + "user.jsp?action=update"
                + String.format("&token=%s&openid=%s", token, Test.openid);
    }
}
