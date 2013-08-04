package com.example.classmate.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.xframe.annotation.JSONUtils;
import org.xframe.http.XHttpRequest;

import com.example.classmate.Conf;
import com.example.classmate.data.Holiday;

public class HolidayListRequest extends XHttpRequest {
    
    public HolidayListRequest(int page) {
        addParam("action", "list");
        addParam("page", page);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        List<Holiday> data = new ArrayList<Holiday>();
        JSONArray result = new JSONArray(content);
        for (int i=0; i<result.length(); i++) {
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