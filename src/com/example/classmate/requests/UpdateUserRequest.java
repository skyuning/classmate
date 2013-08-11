package com.example.classmate.requests;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

@XHttpAttr(method = XHttpMethod.POST)
public class UpdateUserRequest extends XHttpRequest {

    public UpdateUserRequest(String photoPath)
            throws UnsupportedEncodingException {
        addMultipartFile("photo", photoPath, "image/jpeg");
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        System.err.println(content);
        return null;
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "user.jsp?action=update&token=123457890&openid=" + Conf.APPID;
    }
}
