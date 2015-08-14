package com.example.jerryyin.myhttpmethoddemo01.model;

/**
 * Created by JerryYin on 8/13/15.
 * 解析json数据中需要的元素
 */
public class Items {

    private String translation;
    private Basics basic;
    private String query;
    private int errorCode;
    private Webs web;


    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Basics getBasic() {
        return basic;
    }

    public void setBasic(Basics basic) {
        this.basic = basic;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Webs getWeb() {
        return web;
    }

    public void setWeb(Webs web) {
        this.web = web;
    }
}
