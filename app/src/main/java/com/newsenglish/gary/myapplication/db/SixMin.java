package com.newsenglish.gary.myapplication.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Gary on 2017/8/25.
 */

public class SixMin extends DataSupport{
    private int id;
    private String Title;
    private String Pic;
    private String Url;
    private String Time;
    private String ReadCount;

    public String getReadCount() {
        return ReadCount;
    }

    public void setReadCount(String readCount) {
        ReadCount = readCount;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
