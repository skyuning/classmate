package com.example.classmate.data;

import java.util.List;

import org.xframe.annotation.JSONUtils.JSONDict;

public class News {
    @JSONDict(name = "newsphoto", defVal = "")
    public String photo;

    @JSONDict(name = "reviewlist",
            defVal = "[{'comment': 'comment1'}, {'comment': 'comment2'}]",
            type = Review.class)
    public List<Review> reviewList;

    @JSONDict(name = "reviewnum", defVal = "0")
    public int reviewNum;

    public static class Review {
        @JSONDict(name = "comment", defVal = "no comment")
        public String comment;
    }
}
