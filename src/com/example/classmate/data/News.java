package com.example.classmate.data;

import java.util.List;

import org.xframe.annotation.JSONUtils.JSONDict;

public class News {
    @JSONDict(name = "newsphoto", defVal = "")
    public String photo;

    @JSONDict(name = "reviewlist", defVal = "[]", type = Review.class)
    public List<Review> reviewList;

    @JSONDict(name = "reviewnum", defVal = "0")
    public int reviewNum;

    public static class Review {
        @JSONDict(name = "cdate", defVal = "0000-00-00 00:00:00")
        public String date;
        
        @JSONDict(name = "cinfo", defVal = "0000-00-00 00:00:00")
        public String info;
        
        @JSONDict(name = "u_name", defVal = "0000-00-00 00:00:00")
        public String username;
    }
}
