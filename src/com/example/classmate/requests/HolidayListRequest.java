package com.example.classmate.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xframe.annotation.JSONUtils;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest;
import org.xframe.http.XHttpRequest.XHttpMethod;

import android.content.Context;

import com.example.classmate.common.Conf;
import com.example.classmate.data.Holiday;

@XHttpAttr(method = XHttpMethod.GET)
public class HolidayListRequest extends BaseRequest {

    public HolidayListRequest(Context context, int page) {
        super(context);
        addParam("action", "list");
        addParam("page", page);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        List<Holiday> data = new ArrayList<Holiday>();
        JSONArray result = new JSONObject(content).getJSONArray("message");
        for (int i = 0; i < result.length(); i++) {
            Holiday holiday = new Holiday();
            JSONUtils.json2JavaObject(result.getJSONObject(i), holiday);
            data.add(holiday);
        }

        return data;
    }

    @Override
    protected String buildUrl() {
        String url = Conf.HOST + Conf.PATH + "holiday.jsp";
        return url;
    }

}
