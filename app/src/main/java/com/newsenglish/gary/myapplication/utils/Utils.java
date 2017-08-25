package com.newsenglish.gary.myapplication.utils;

import android.content.Context;
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
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if ("Title_cn".equals(nodeName)) {
                                title = xmlPullParser.nextText();
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

}
