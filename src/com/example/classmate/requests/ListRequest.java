package com.example.classmate.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.common.Conf;

import android.content.Context;

@XHttpAttr(method = XHttpMethod.GET)
public class ListRequest extends BaseRequest {

    private String mScript;
    private int mPage;
    
    public ListRequest(Context context, String script, int page) {
        super(context);
        String token = context.getSharedPreferences(
                "session", Context.MODE_PRIVATE).getString("token", "");
        addParam("token", token);
        addParam("action", "list");
        mScript = script;
    }
    
    public void setPage(int page) {
        mPage = page;
    }
    
    @Override
    protected String buildQueryString() throws ParseException, IOException {
        String queryString = super.buildQueryString();
        queryString += "&page=" + mPage;
        return queryString;
    }

    @Override
    protected String buildUrl() {
        String url = Conf.HOST + Conf.PATH  + mScript;
        return url;
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        JSONArray ja = (JSONArray) super.handleResponse(response, content);
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i=0; i<ja.length(); i++)
            list.add(ja.optJSONObject(i));
        return list;
    }
}
