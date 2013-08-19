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

import com.example.classmate.common.Conf;
import com.example.classmate.data.News;

@Deprecated
@XHttpAttr(method = XHttpMethod.GET)
public class NewsListRequest extends XHttpRequest {
    
    public NewsListRequest(int page) {
        addParam("action", "list");
        addParam("page", page);
    }

    @Override
    public Object handleResponse(HttpResponse response, String content) throws Exception {
        List<News> data = new ArrayList<News>();
        JSONArray result = (new JSONObject(content)).getJSONArray("message");
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
