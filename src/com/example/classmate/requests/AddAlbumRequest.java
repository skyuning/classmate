package com.example.classmate.requests;

import java.io.UnsupportedEncodingException;

import com.example.classmate.common.Conf;

import android.content.Context;

public class AddAlbumRequest extends BaseRequest {

    public AddAlbumRequest(Context context, String path) throws UnsupportedEncodingException {
        super(context);
        addParam("action", "add");
        addMultipartImage("photo", path);
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "album";
    }

}
