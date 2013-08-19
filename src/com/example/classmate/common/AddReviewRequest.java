package com.example.classmate.common;

import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import android.content.Context;

import com.example.classmate.requests.BaseRequest;

@XHttpAttr(method = XHttpMethod.GET)
public class AddReviewRequest extends BaseRequest {

    public AddReviewRequest(Context context, int newsId, String info) {
        super(context);
        addParam("action", "add");
        addParam("newsid", newsId);
        addParam("info", info);
    }

    @Override
    protected String buildUrl() {
        String url = Conf.HOST + Conf.PATH + "review";
        return url;
    }

}
