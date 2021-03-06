package com.example.classmate.requests;

import java.io.UnsupportedEncodingException;

import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.POST)
public class AddAlbumRequest extends BaseRequest {

    public AddAlbumRequest(Context context, String path)
            throws UnsupportedEncodingException {
        super(context);
        addParam("action", "add");
        addMultipartImage("photo", path);
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "album";
    }

}
