package com.example.classmate.requests;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import android.content.Context;

import com.example.classmate.common.Conf;
import com.example.classmate.common.Test;

@XHttpAttr(method = XHttpMethod.POST)
public class AddNewsRequest extends BaseRequest {

    private String mInfo;

    public AddNewsRequest(Context context, String photoPath, String info)
            throws UnsupportedEncodingException {
        super(context);
        mInfo = info;
        addMultipartFile("photo", photoPath, "image/jpeg");
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        super.handleResponse(response, content);
        System.err.println(content);
        return null;
    }

    @Override
    protected String buildUrl() {
        String url = String.format(Conf.HOST + Conf.PATH
                + "news?action=add&token=%s&info=%s", Test.token, mInfo);
        return url;
    }
}
