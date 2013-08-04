package com.example.classmate.data;

import org.xframe.annotation.JSONUtils.JSONDict;

public class Classmate {

    @JSONDict(name = "u_address", defVal = "")
    public String address;

    @JSONDict(name = "u_cellphone", defVal = "")
    public String phone;

    @JSONDict(name = "u_city", defVal = "")
    public String city;

    @JSONDict(name = "u_email", defVal = "")
    public String email;

    @JSONDict(name = "u_name", defVal = "")
    public String name;

    @JSONDict(name = "u_qq", defVal = "")
    public String qq;

    @JSONDict(name = "u_status", defVal = "")
    public String status;

    @JSONDict(name = "u_weibo", defVal = "")
    public String weibo;

    @JSONDict(name = "u_weixin", defVal = "")
    public String weixin;

    @JSONDict(name = "u_work", defVal = "")
    public String work;
}
