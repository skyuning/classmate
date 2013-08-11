package com.example.classmate.data;

import org.xframe.annotation.JSONUtils.JSONDict;

public class News {
    @JSONDict(name = "newsinfo", defVal = "")
    public String info;

    @JSONDict(name = "newsphoto", defVal = "")
    public String newsPhoto;

    @JSONDict(name = "reviewnum", defVal = "0")
    public int reviewNum;

    @JSONDict(name = "uname", defVal = "")
    public int username;

    @JSONDict(name = "uphoto", defVal = "")
    public int userPhoto;

//    @JSONDict(name = "reviewlist", defVal = "[]", type = Review.class)
//    public List<Review> reviewList;

    public static class Review {
        @JSONDict(name = "cdate", defVal = "0000-00-00 00:00:00")
        public String date;
        
        @JSONDict(name = "cinfo", defVal = "0000-00-00 00:00:00")
        public String info;
        
        @JSONDict(name = "u_name", defVal = "0000-00-00 00:00:00")
        public String username;
    }
}
