package com.newsenglish.gary.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;


import com.newsenglish.gary.myapplication.db.BbcNews;
import com.newsenglish.gary.myapplication.db.LocalEnglish;
import com.newsenglish.gary.myapplication.db.NewsWord;
import com.newsenglish.gary.myapplication.db.SixMin;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_BBCNEWS;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_LOCALENGLISH;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_NEWSWORD;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_SIXMIN;

/**
 * Created by Gary on 2017/8/24.
 */

public class Utils {

    //解析xml
    public static boolean parseXmlWithPull(String str, int type) {
        if (!str.isEmpty()) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(str));
                int eventType = xmlPullParser.getEventType();
                String title = "";
                String pic = "";
                String url = "";
                String time = "";
                String readcount = "";
                String mp3 = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if ("Title_cn".equals(nodeName)) {
                                title = xmlPullParser.nextText();
                            } else if ("Sound".equals(nodeName)) {
                                mp3 = xmlPullParser.nextText();
                            } else if ("Url".equals(nodeName)) {
                                url = xmlPullParser.nextText();
                            } else if ("Pic".equals(nodeName)) {
                                pic = xmlPullParser.nextText();
                            } else if ("CreatTime".equals(nodeName)) {
                                time = xmlPullParser.nextText();
                            } else if ("ReadCount".equals(nodeName)) {
                                readcount = xmlPullParser.nextText();
                            }

                        }
                        break;
                        case XmlPullParser.END_TAG: {
                            if ("Bbctitle".equals(nodeName)) {
                                switch (type) {
                                    case TAB_TYPE_SIXMIN: {
                                        SixMin six = new SixMin();
                                        six.setTitle(title);
                                        six.setUrl(url);
                                        six.setPic(pic);
                                        six.setTime(time);
                                        six.setReadCount(readcount);
                                        six.setMp3(mp3);
                                        six.save();
                                    }
                                    break;
                                    case TAB_TYPE_LOCALENGLISH: {
                                        LocalEnglish english = new LocalEnglish();
                                        english.setTitle(title);
                                        english.setUrl(url);
                                        english.setPic(pic);
                                        english.setTime(time);
                                        english.setReadCount(readcount);
                                        english.setMp3(mp3);
                                        english.save();
                                    }
                                    break;
                                    case TAB_TYPE_BBCNEWS: {
                                        BbcNews news = new BbcNews();
                                        news.setTitle(title);
                                        news.setUrl(url);
                                        news.setPic(pic);
                                        news.setTime(time);
                                        news.setReadCount(readcount);
                                        news.setMp3(mp3);
                                        news.save();
                                    }
                                    break;
                                    case TAB_TYPE_NEWSWORD: {
                                        NewsWord word = new NewsWord();
                                        word.setTitle(title);
                                        word.setUrl(url);
                                        word.setPic(pic);
                                        word.setTime(time);
                                        word.setReadCount(readcount);
                                        word.setReadCount(readcount);
                                        word.save();
                                    }
                                    break;
                                }

                            }
                        }
                        break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //分割日期字符
    public static String fenge(String str) {
        String[] arr = str.split("\\s+");
        return arr[0];
    }


    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isNetworkAvailable(Context activity) {
        //得到应用上下文
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）  notificationManager /alarmManager
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
