package com.newsenglish.gary.myapplication.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Gary on 2017/8/25.
 */

public class LocalEnglish extends DataSupport {
    private int id;
    private String Title;
    private String Pic;
    private String Url;
    private String Time;
    private String ReadCount;
    private String Mp3;

    public String getMp3() {
        return Mp3;
    }

    public void setMp3(String mp3) {
        Mp3 = mp3;
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
