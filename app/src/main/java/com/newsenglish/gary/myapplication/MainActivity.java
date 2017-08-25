package com.newsenglish.gary.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.newsenglish.gary.myapplication.adapter.MyPagerAdapter;
import com.newsenglish.gary.myapplication.db.BbcNews;
import com.newsenglish.gary.myapplication.db.LocalEnglish;
import com.newsenglish.gary.myapplication.db.NewsWord;
import com.newsenglish.gary.myapplication.db.SixMin;
import com.newsenglish.gary.myapplication.preferences.Preferences;
import com.newsenglish.gary.myapplication.utils.HttpUtil;
import com.newsenglish.gary.myapplication.utils.Utils;


import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_BBCNEWS;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_LOCALENGLISH;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_NEWSWORD;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_SIXMIN;


public class MainActivity extends Activity implements View.OnClickListener {
    private TextView btnTxt1, btnTxt2, btnTxt3, btnTxt4;
    private TextView lineTxt1, lineTxt2, lineTxt3, lineTxt4;
    private TextView itemTitle, itemTime, itemReadcount;
    private ImageView itemPic;
    private TextView fault;
    private LinearLayout layoutItem;
    private RelativeLayout viewLayout;
    private Context context;
    private List<BbcNews> bbcnews;
    private List<LocalEnglish> localenglishs;
    private List<NewsWord> newswords;
    private List<SixMin> sixmins;
    private int currentpage = 0;
    private int j = 0;

    private ViewPager vp;
    private MyPagerAdapter adapter;
    private ArrayList<String> pics = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private RelativeLayout outOfVPlayout;
    private RelativeLayout rlayout;
    private RadioGroup radioGroup;
    private int currentImg = 100;
    private ImageView lastPic;
    private ImageView nextPic;

    private TextView unClick;

    private Handler handler = new Handler();
    private Runnable nextImg = new Runnable() {
        @Override
        public void run() {
            currentImg = currentImg + 1;
            vp.setCurrentItem(currentImg);
            clearRadioImgEnble();
            setCurrentRadioImgEnble(currentImg);
            handler.postDelayed(nextImg, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initView();
        touchEvent();
        onBtnAndLineClear();
        onBtnAndLineClick(btnTxt1, lineTxt1);
        showInfo(TAB_TYPE_SIXMIN);
        startPlayer();
        initGroupPoint();
    }

    private void startPlayer() {
        handler.postDelayed(nextImg, 4000);
    }

    private void initGroupPoint() {
        radioGroup.getChildAt(0).setEnabled(false);
    }

    private void setVP(ArrayList<String> pics, ArrayList<String> titles) {
        adapter = new MyPagerAdapter(context, pics, titles);
        vp.setAdapter(adapter);
        vp.setCurrentItem(currentImg);

    }

    private void showInfo(int type) {
        layoutItem.removeAllViews();
        pics.clear();
        titles.clear();
        switch (type) {
            case TAB_TYPE_SIXMIN: {
                sixmins = DataSupport.findAll(SixMin.class);
                if (sixmins.size() > 0) {
                    int bodylengh = sixmins.size() > 6 ? 6 : sixmins.size();
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
                        itemPic = view.findViewById(R.id.item_pic);
                        itemReadcount = view.findViewById(R.id.item_readcount);
                        itemTime = view.findViewById(R.id.item_time);
                        itemTitle = view.findViewById(R.id.item_title);
                        viewLayout = view.findViewById(R.id.view_layout);
                        itemTitle.setText(sixmins.get(j).getTitle());
                        itemTime.setText(sixmins.get(j).getTime());
                        itemReadcount.setText(sixmins.get(j).getReadCount());
                        Glide.with(context).load(sixmins.get(j).getPic()).into(itemPic);
                        viewLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                            }
                        });
                        layoutItem.addView(view);
                        pics.add(sixmins.get(j).getPic());
                        titles.add(sixmins.get(j).getTitle());
                    }
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
            case TAB_TYPE_LOCALENGLISH: {
                localenglishs = DataSupport.findAll(LocalEnglish.class);
                if (localenglishs.size() > 0) {
                    int bodylengh = sixmins.size() > 6 ? 6 : sixmins.size();
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
                        itemPic = view.findViewById(R.id.item_pic);
                        itemReadcount = view.findViewById(R.id.item_readcount);
                        itemTime = view.findViewById(R.id.item_time);
                        itemTitle = view.findViewById(R.id.item_title);
                        viewLayout = view.findViewById(R.id.view_layout);
                        itemTitle.setText(localenglishs.get(j).getTitle());
                        itemTime.setText(localenglishs.get(j).getTime());
                        itemReadcount.setText(localenglishs.get(j).getReadCount());
                        Glide.with(context).load(localenglishs.get(j).getPic()).into(itemPic);
                        viewLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                            }
                        });
                        layoutItem.addView(view);
                        pics.add(localenglishs.get(j).getPic());
                        titles.add(localenglishs.get(j).getTitle());
                    }
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
            case TAB_TYPE_BBCNEWS: {
                bbcnews = DataSupport.findAll(BbcNews.class);
                if (bbcnews.size() > 0) {
                    int bodylengh = sixmins.size() > 6 ? 6 : sixmins.size();
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
                        itemPic = view.findViewById(R.id.item_pic);
                        itemReadcount = view.findViewById(R.id.item_readcount);
                        itemTime = view.findViewById(R.id.item_time);
                        itemTitle = view.findViewById(R.id.item_title);
                        viewLayout = view.findViewById(R.id.view_layout);
                        itemTitle.setText(bbcnews.get(j).getTitle());
                        itemTime.setText(bbcnews.get(j).getTime());
                        itemReadcount.setText(bbcnews.get(j).getReadCount());
                        Glide.with(context).load(bbcnews.get(j).getPic()).into(itemPic);
                        viewLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                            }
                        });
                        layoutItem.addView(view);
                        pics.add(bbcnews.get(j).getPic());
                        titles.add(bbcnews.get(j).getTitle());
                    }
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
            case TAB_TYPE_NEWSWORD: {
                newswords = DataSupport.findAll(NewsWord.class);
                if (newswords.size() > 0) {
                    int bodylengh = sixmins.size() > 6 ? 6 : sixmins.size();
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
                        itemPic = view.findViewById(R.id.item_pic);
                        itemReadcount = view.findViewById(R.id.item_readcount);
                        itemTime = view.findViewById(R.id.item_time);
                        itemTitle = view.findViewById(R.id.item_title);
                        viewLayout = view.findViewById(R.id.view_layout);
                        itemTitle.setText(newswords.get(j).getTitle());
                        itemTime.setText(newswords.get(j).getTime());
                        itemReadcount.setText(newswords.get(j).getReadCount());
                        Glide.with(context).load(newswords.get(j).getPic()).into(itemPic);
                        viewLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                            }
                        });
                        layoutItem.addView(view);
                        pics.add(newswords.get(j).getPic());
                        titles.add(newswords.get(j).getTitle());
                    }
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
        }

    }


    private void queryFromServer(int maxid, final int type) {
        String url = "http://apps.iyuba.com/minutes/titleNewApi.jsp?maxid=" + maxid + "&format=xml&type=android";
        HttpUtil.sendOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //加载失败
                        fault.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //加载成功
                String info = response.body().string();
                boolean requestInfo = false;
                requestInfo = Utils.parseXmlWithPull(info, type);
                if (requestInfo == true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showInfo(type);
                        }
                    });
                }
            }
        });
    }

    /**
     * 设置点击事件
     */
    private void touchEvent() {
        btnTxt1.setOnClickListener(this);
        btnTxt2.setOnClickListener(this);
        btnTxt3.setOnClickListener(this);
        btnTxt4.setOnClickListener(this);

        //轮播上一张图和下一张图
        lastPic.setOnClickListener(this);
        nextPic.setOnClickListener(this);

        //防止viewpager滑动时用户点击img会卡住的问题
        unClick.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnTxt1 = findViewById(R.id.btn_txt1);
        btnTxt2 = findViewById(R.id.btn_txt2);
        btnTxt3 = findViewById(R.id.btn_txt3);
        btnTxt4 = findViewById(R.id.btn_txt4);
        lineTxt1 = findViewById(R.id.line_txt1);
        lineTxt2 = findViewById(R.id.line_txt2);
        lineTxt3 = findViewById(R.id.line_txt3);
        lineTxt4 = findViewById(R.id.line_txt4);
        fault = findViewById(R.id.text_fault);
        layoutItem = findViewById(R.id.item_layout);

        vp = findViewById(R.id.viewpager);
        outOfVPlayout = findViewById(R.id.outofvp_layout);
        rlayout = findViewById(R.id.radio_group_layout);
        radioGroup = findViewById(R.id.radio_group);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                getMetricsWidthAndHeight().get(0), getMetricsWidthAndHeight().get(0) * 5 / 9);
        outOfVPlayout.setLayoutParams(lp);

        lastPic = findViewById(R.id.last_img);
        nextPic = findViewById(R.id.next_img);

        unClick = findViewById(R.id.unclick);
    }

    /**
     * 清空按钮状态
     */
    private void onBtnAndLineClear() {
        btnTxt1.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnTxt2.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnTxt3.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnTxt4.setTextColor(getResources().getColor(R.color.colorPrimary));
        //lineTxt1.setBackground(getResources().getDrawable(R.color.colorAccent));
        lineTxt1.setBackgroundColor(Color.TRANSPARENT);
        lineTxt2.setBackgroundColor(Color.TRANSPARENT);
        lineTxt3.setBackgroundColor(Color.TRANSPARENT);
        lineTxt4.setBackgroundColor(Color.TRANSPARENT);
        btnTxt1.setClickable(true);
        btnTxt2.setClickable(true);
        btnTxt3.setClickable(true);
        btnTxt4.setClickable(true);
    }


    /**
     * 点击按钮状态
     */
    private void onBtnAndLineClick(TextView btn, TextView line) {
        btn.setTextColor(getResources().getColor(R.color.init_color));
        btn.setClickable(false);
        line.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void setCurrentRadioImgEnble(int currentchild) {
        if (radioGroup.getChildAt(currentchild % 4) != null)
            radioGroup.getChildAt(currentchild % 4).setEnabled(false);
    }
    private void clearRadioImgEnble(){
        if (radioGroup.getChildCount() == 4){
            for (int i = 0; i < 4;i++)
            radioGroup.getChildAt(i).setEnabled(true);
        }
    }
    /**
     * 点击事件的具体实现
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_txt1: {
                currentImg = 0;
                onBtnAndLineClear();
                onBtnAndLineClick(btnTxt1, lineTxt1);
                showInfo(TAB_TYPE_SIXMIN);

            }
            break;
            case R.id.btn_txt2: {
                currentImg = 0;
                onBtnAndLineClear();
                onBtnAndLineClick(btnTxt2, lineTxt2);
                showInfo(TAB_TYPE_LOCALENGLISH);
            }
            break;
            case R.id.btn_txt3: {
                currentImg = 0;
                onBtnAndLineClear();
                onBtnAndLineClick(btnTxt3, lineTxt3);
                showInfo(TAB_TYPE_BBCNEWS);
            }
            break;
            case R.id.btn_txt4: {
                currentImg = 0;
                onBtnAndLineClear();
                onBtnAndLineClick(btnTxt4, lineTxt4);
                showInfo(TAB_TYPE_NEWSWORD);
            }
            break;
            case R.id.last_img: {
                handler.removeCallbacks(nextImg);
                currentImg = currentImg - 1;
                clearRadioImgEnble();
                setCurrentRadioImgEnble(currentImg);
                vp.setCurrentItem(currentImg);
                handler.postDelayed(nextImg, 4000);
            }
            break;
            case R.id.next_img: {
                handler.removeCallbacks(nextImg);
                currentImg = currentImg + 1;
                clearRadioImgEnble();
                setCurrentRadioImgEnble(currentImg);
                vp.setCurrentItem(currentImg);
                handler.postDelayed(nextImg, 4000);
            }
            break;
            case R.id.unclick: {
                Intent intent = new Intent(this,WebActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlayer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(nextImg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(nextImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(nextImg);
        DataSupport.deleteAll(SixMin.class);
        DataSupport.deleteAll(NewsWord.class);
        DataSupport.deleteAll(BbcNews.class);
        DataSupport.deleteAll(LocalEnglish.class);
    }

    /**
     * 获取屏幕宽度和高度
     *
     * @return
     */
    public List<Integer> getMetricsWidthAndHeight() {
        List<Integer> list = new ArrayList<>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        list.add(dm.widthPixels);
        list.add(dm.heightPixels);
        return list;
    }
}
