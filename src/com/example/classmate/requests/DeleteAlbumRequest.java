package com.example.classmate.requests;

import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.POST)
public class DeleteAlbumRequest extends BaseRequest {

    public DeleteAlbumRequest(Context context, int photoId) {
        super(context);
        addParam("action", "delete");
        addParam("photoid", photoId);
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "album";
    }

}
