package com.example.classmate.requests;

import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.GET)
public class ReadAlbumRequest extends BaseRequest {

    public ReadAlbumRequest(Context context) {
        super(context);
        addParam("action", "read");
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "album";
    }
}
