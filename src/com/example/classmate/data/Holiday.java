package com.example.classmate.data;

import java.io.Serializable;

import org.xframe.annotation.JSONUtils.JSONDict;

public class Holiday implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONDict(name = "category")
    public int category;

    @JSONDict(name = "date", defVal = "")
    public String date;

    @JSONDict(name = "description", defVal = "")
    public String desc;

    @JSONDict(name = "title", defVal = "")
    public String title;
}
