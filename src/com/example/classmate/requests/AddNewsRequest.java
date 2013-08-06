package com.example.classmate.requests;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.xframe.http.XHttpAttr;
import org.xframe.http.XHttpRequest;
import org.xframe.http.XHttpRequest.XHttpMethod;

import com.example.classmate.Conf;

@XHttpAttr(method = XHttpMethod.POST)
public class AddNewsRequest extends XHttpRequest {
    
    public AddNewsRequest(String photoPath, String info) throws UnsupportedEncodingException {
//        addMultipartString("action", "add");
        addMultipartString("info", info);
        addMultipartFile("photo", photoPath, "image/jpeg");
    }

    @Override
    public Object handleResponse(HttpResponse response, String content)
            throws Exception {
        System.err.println(content);
        return null;
    }

    @Override
    protected String buildUrl() {
        return Conf.HOST + "/Classmate/UploadServlet";
    }

}
