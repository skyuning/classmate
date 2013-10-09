package com.example.classmate.requests;

import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.GET)
public class AddHolidayRequest extends BaseRequest {

    public AddHolidayRequest(Context context, String title, String desc,
            String date) {
        super(context);
        addParam("action", "add");
        addParam("title", title);
        addParam("description", desc);
        addParam("date", date);
        addParam("category", "3");
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + Conf.PATH + "holiday";
    }

}
