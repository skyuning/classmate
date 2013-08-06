package com.example.classmate.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.xframe.annotation.JSONUtils;
import org.xframe.http.XHttpRequest;

import com.example.classmate.Conf;
import com.example.classmate.data.News;

public class NewsListRequest extends XHttpRequest {
    
    public NewsListRequest(int page) {
        addParam("action", "list");
        addParam("page", page);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        List<News> data = new ArrayList<News>();
        JSONArray result = new JSONArray(content);
        for (int i=0; i<result.length(); i++) {
            News news = new News();
            JSONUtils.json2JavaObject(result.getJSONObject(i), news);
            data.add(news);
        }
        
        return data;
    }

    @Override
    protected String buildUrl() {
        String url = Conf.HOST + Conf.PATH + "news.jsp";
        return url;
    }

}