package com.example.classmate.data;

import org.xframe.annotation.JSONUtils.JSONDict;

public class Holiday {
    @JSONDict(name = "date", defVal = "")
    public String date;

    @JSONDict(name = "description", defVal = "")
    public String desc;

    @JSONDict(name = "title", defVal = "")
    public String title;
}
