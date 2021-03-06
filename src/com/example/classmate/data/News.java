package com.example.classmate.data;

import org.xframe.annotation.JSONUtils.JSONDict;

import com.example.classmate.data.Status.LoadStatus;

public class News {
    @JSONDict(name = "newsid", defVal = "-1")
    public int newsId;
    
    @JSONDict(name = "newsinfo", defVal = "")
    public String info;

    @JSONDict(name = "newsphoto", defVal = "")
    public String newsPhoto;
    
    public LoadStatus newsPhotoStatus = LoadStatus.UNLOADED;

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
        
        @JSONDict(name = "cinfo")
        public String info;
        
        @JSONDict(name = "u_name")
        public String username;
        
        @JSONDict(name = "u_photo")
        public String photo;
    }
}
