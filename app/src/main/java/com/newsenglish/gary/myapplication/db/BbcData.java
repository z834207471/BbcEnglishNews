package com.newsenglish.gary.myapplication.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Gary on 2017/8/27.
 */

public class BbcData {
    private String Title;
    private String Pic;
    private String Url;
    private String Time;
    private String ReadCount;
    public BbcData(){}
    public BbcData(String title, String pic, String time, String readcount) {
        this.Title = title;
        this.Time = time;
        this.ReadCount = readcount;
        this.Pic = pic;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getReadCount() {
        return ReadCount;
    }

    public void setReadCount(String readCount) {
        ReadCount = readCount;
    }
}
