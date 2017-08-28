package com.newsenglish.gary.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.newsenglish.gary.myapplication.adapter.ListViewAdapter;
import com.newsenglish.gary.myapplication.adapter.MyPagerAdapter;
import com.newsenglish.gary.myapplication.db.BbcNews;
import com.newsenglish.gary.myapplication.db.BbcData;
import com.newsenglish.gary.myapplication.db.LocalEnglish;
import com.newsenglish.gary.myapplication.db.NewsWord;
import com.newsenglish.gary.myapplication.db.SixMin;
import com.newsenglish.gary.myapplication.utils.HttpUtil;
import com.newsenglish.gary.myapplication.utils.Utils;


import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.newsenglish.gary.myapplication.preferences.Preferences.DEFAULT_DURATION;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_BBCNEWS;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_LOCALENGLISH;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_NEWSWORD;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_SIXMIN;


public class MainActivity extends Activity implements View.OnClickListener {
    //不解释
    private Context context;
    //导航栏
    private TextView btnTxt1, btnTxt2, btnTxt3, btnTxt4;
    private TextView lineTxt1, lineTxt2, lineTxt3, lineTxt4;
    //加载失败
    private TextView fault;
    //列表
    private ListView listView;
    private ListViewAdapter listadapter;
    private List<BbcNews> bbcnews;
    private List<LocalEnglish> localenglishs;
    private List<NewsWord> newswords;
    private List<SixMin> sixmins;
    private List<BbcData> dataList = new ArrayList<>();
    private int currentpage = 0; //当前页
    private int bodylengh = 0;  //展示长度
    private int j = 0;      //数据在数据库中的id
    private int currentType = 0;   //当前的news类型
    private int itemHeight = 0;    //item 的高度

    //设置轮播
    private ViewPager vp;
    private MyPagerAdapter adapter;
    private ArrayList<String> pics = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> mp3s = new ArrayList<>();
    private RelativeLayout outOfVPlayout;
    private RadioGroup radioGroup;
    private int currentImg = 100;  //当前页 为了防止用户向前翻  所以设置100
    private ImageView lastPic;  //上一张图
    private ImageView nextPic;  //下一张图

    private ImageView backImg;

    private TextView unClick;    //设置点击事件

    //上下页
    private TextView lastPage;
    private TextView nextPage;
    //点击的时间 设置单例
    private long click_time = 0;
    private ImageView bottomImg;
    private TextView home;
    private ProgressBar progress;

    private RelativeLayout mainLayout;
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
//        onBtnAndLineClear();
//        onBtnAndLineClick(btnTxt1, lineTxt1);
//        touchEvent();
//        startPlayer();
//        initGroupPoint();
//        showInfo(TAB_TYPE_SIXMIN);

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
        hideProgress();
        mainLayout.setVisibility(View.VISIBLE);

    }

    private void showInfo(int type) {
        pics.clear();
        titles.clear();
        dataList.clear();
        times.clear();
        switch (type) {
            case TAB_TYPE_SIXMIN: {
                currentType = TAB_TYPE_SIXMIN;
                sixmins = DataSupport.findAll(SixMin.class);
                if (sixmins.size() > 0) {
                    currentpage = makeSureCurrentPageIn(sixmins.size(), currentpage);
                    bodylengh = currentpage < (sixmins.size() / 6) ? 6 : (sixmins.size() % 6);
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        dataList.add(new BbcData(sixmins.get(j).getTitle(),sixmins.get(j).getPic(),
                                Utils.fenge(sixmins.get(j).getTime()),getResources().getText(R.string.read) + sixmins.get(j).getReadCount()));
                        pics.add(sixmins.get(j).getPic());
                        titles.add(sixmins.get(j).getTitle());
                        times.add(Utils.fenge(sixmins.get(j).getTime()));
                        mp3s.add(sixmins.get(j).getMp3());
                    }
                    setListView();
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
            case TAB_TYPE_LOCALENGLISH: {
                currentType = TAB_TYPE_LOCALENGLISH;
                localenglishs = DataSupport.findAll(LocalEnglish.class);
                if (localenglishs.size() > 0) {
                    currentpage = makeSureCurrentPageIn(localenglishs.size(), currentpage);
                    int bodylengh = currentpage < (localenglishs.size() / 6) ? 6 : (localenglishs.size() % 6);
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        dataList.add(new BbcData(localenglishs.get(j).getTitle(),localenglishs.get(j).getPic(),
                                Utils.fenge(localenglishs.get(j).getTime()),getResources().getText(R.string.read) + localenglishs.get(j).getReadCount()));
                        pics.add(localenglishs.get(j).getPic());
                        titles.add(localenglishs.get(j).getTitle());
                        times.add(Utils.fenge(localenglishs.get(j).getTime()));
                        mp3s.add(localenglishs.get(j).getMp3());
                    }
                    setListView();
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
            case TAB_TYPE_BBCNEWS: {
                currentType = TAB_TYPE_BBCNEWS;
                bbcnews = DataSupport.findAll(BbcNews.class);
                if (bbcnews.size() > 0) {
                    currentpage = makeSureCurrentPageIn(bbcnews.size(), currentpage);
                    int bodylengh = currentpage < (bbcnews.size() / 6) ? 6 : (bbcnews.size() % 6);
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        dataList.add(new BbcData(bbcnews.get(j).getTitle(),bbcnews.get(j).getPic(),
                                Utils.fenge(bbcnews.get(j).getTime()),getResources().getText(R.string.read) + bbcnews.get(j).getReadCount()));
                        pics.add(bbcnews.get(j).getPic());
                        titles.add(bbcnews.get(j).getTitle());
                        times.add(Utils.fenge(bbcnews.get(j).getTime()));
                        mp3s.add(bbcnews.get(j).getMp3());
                    }
                    setListView();
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
                    currentType = TAB_TYPE_NEWSWORD;
                    currentpage = makeSureCurrentPageIn(newswords.size(), currentpage);
                    int bodylengh = currentpage < (newswords.size() / 6) ? 6 : (newswords.size() % 6);
                    for (int i = 0; i < bodylengh; i++) {
                        j = i + currentpage * 6;
                        dataList.add(new BbcData(newswords.get(j).getTitle(),newswords.get(j).getPic(),
                                Utils.fenge(newswords.get(j).getTime()),getResources().getText(R.string.read) + newswords.get(j).getReadCount()));
                        pics.add(newswords.get(j).getPic());
                        titles.add(newswords.get(j).getTitle());
                        times.add(Utils.fenge(newswords.get(j).getTime()));
                        mp3s.add(newswords.get(j).getMp3());
                    }
                    setListView();
                    setVP(pics, titles);
                } else {
                    int maxid = type;
                    queryFromServer(maxid, type);
                }
            }
            break;
        }

    }

    private void setListView() {
        listadapter.notifyDataSetChanged();
        itemHeight = bodylengh * Utils.dip2px(context,90);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                getMetricsWidthAndHeight().get(0), itemHeight);
        listView.setLayoutParams(lp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("webtitle",titles.get(i));
                intent.putExtra("webpic",pics.get(i));
                intent.putExtra("webtime",times.get(i));
                intent.putExtra("webmp3",mp3s.get(i));
                intent.putExtra("webtype",currentType);
                startActivity(intent);
            }
        });
    }

    /**
     * 保证currentPage的范围
     */
    private int makeSureCurrentPageIn(int max, int nowpage) {
        if (nowpage < 0) {
            nowpage = 0;
            Toast.makeText(context, "已经是第一页了", Toast.LENGTH_SHORT).show();
        } else if (nowpage > max / 6) {
            Toast.makeText(context, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            nowpage = max / 6;
        } else {
            nowpage = nowpage;
        }
        return nowpage;
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

        //上下页的点击事件
        nextPage.setOnClickListener(this);
        lastPage.setOnClickListener(this);

        bottomImg.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        progress = findViewById(R.id.progress);
        showProgress();
        mainLayout = findViewById(R.id.main_layout);
        mainLayout.setVisibility(View.INVISIBLE);
        btnTxt1 = findViewById(R.id.btn_txt1);
        btnTxt2 = findViewById(R.id.btn_txt2);
        btnTxt3 = findViewById(R.id.btn_txt3);
        btnTxt4 = findViewById(R.id.btn_txt4);
        lineTxt1 = findViewById(R.id.line_txt1);
        lineTxt2 = findViewById(R.id.line_txt2);
        lineTxt3 = findViewById(R.id.line_txt3);
        lineTxt4 = findViewById(R.id.line_txt4);
        fault = findViewById(R.id.text_fault);

        vp = findViewById(R.id.viewpager);
        outOfVPlayout = findViewById(R.id.outofvp_layout);
        radioGroup = findViewById(R.id.radio_group);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                getMetricsWidthAndHeight().get(0), getMetricsWidthAndHeight().get(0) * 5 / 9);
        outOfVPlayout.setLayoutParams(lp);

        lastPic = findViewById(R.id.last_img);
        nextPic = findViewById(R.id.next_img);

        unClick = findViewById(R.id.unclick);

        lastPage = findViewById(R.id.last_page);
        nextPage = findViewById(R.id.next_page);

        bottomImg = findViewById(R.id.bottom_img);
        home = findViewById(R.id.home);
        home.setText("Home");

        listView = findViewById(R.id.list_view);
        listadapter = new ListViewAdapter(context, (ArrayList<BbcData>) dataList);
        listView.setAdapter(listadapter);

        backImg = findViewById(R.id.back_tomain);
        backImg.setVisibility(View.GONE);
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

    private void clearRadioImgEnble() {
        if (radioGroup.getChildCount() == 4) {
            for (int i = 0; i < 4; i++)
                radioGroup.getChildAt(i).setEnabled(true);
        }
    }

    private void showProgress(){

        progress.setVisibility(View.VISIBLE);

    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
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
                if (System.currentTimeMillis() - click_time > DEFAULT_DURATION) {
                    handler.removeCallbacks(nextImg);
                    currentImg = currentImg - 1;
                    clearRadioImgEnble();
                    setCurrentRadioImgEnble(currentImg);
                    vp.setCurrentItem(currentImg);
                    handler.postDelayed(nextImg, 4000);
                    click_time = System.currentTimeMillis();
                }
            }
            break;
            case R.id.next_img: {
                if (System.currentTimeMillis() - click_time > DEFAULT_DURATION) {
                    handler.removeCallbacks(nextImg);
                    currentImg = currentImg + 1;
                    clearRadioImgEnble();
                    setCurrentRadioImgEnble(currentImg);
                    vp.setCurrentItem(currentImg);
                    handler.postDelayed(nextImg, 4000);
                    click_time = System.currentTimeMillis();
                }
            }
            break;
            case R.id.unclick: {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("webtitle",titles.get(currentImg%4));
                intent.putExtra("webpic",pics.get(currentImg%4));
                intent.putExtra("webtitle",titles.get(currentImg%4));
                intent.putExtra("webtype",currentType);
                startActivity(intent);
            }
            break;
            case R.id.last_page: {
                currentpage--;
                showInfo(currentType);
            }
            break;
            case R.id.next_page: {
                currentpage++;
                showInfo(currentType);
            }
            break;
            case R.id.bottom_img: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.iyuba.bbcinone");
                intent.setData(content_url);
                startActivity(intent);
            }
            break;
            case R.id.home:
//                Intent intent= new Intent(context,AiyuActivity.class);
//                intent.putExtra("homeurl","http://m.iyuba.com/#");
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://m.iyuba.com/#");
                intent.setData(content_url);
                startActivity(intent);
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
