package com.newsenglish.gary.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.newsenglish.gary.myapplication.adapter.ListViewAdapter;
import com.newsenglish.gary.myapplication.view.MyPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_BBCNEWS;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_LOCALENGLISH;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_NEWSWORD;
import static com.newsenglish.gary.myapplication.preferences.Preferences.TAB_TYPE_SIXMIN;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;

    private String webTitleStr = "";
    private String webPicurl = "";
    private String webTimeStr = "";
    private String webtypeStr = "";
    private int webType = 0;

    private ImageView webPic;
    private TextView webTitle;
    private TextView webTime;
    private TextView nowTime;
    private TextView fullTime;
    private ImageView backImg;
    private ImageView playBtn;
    private MyPlayer player; // 播放器
    private SeekBar musicProgress; // 音乐进度

    private String url ="";


    private TextView enAndCh;
    private TextView englishTxt;
    private TextView allTxt;
    private boolean isPlay = false;
    Handler handler = new Handler();
    private  LinearLayout webLayout;
    private ProgressBar progressBar;
//设置菜单
    private ListView weblistView;
    private ArrayAdapter adapter;
    private TextView webMenu;
    private LinearLayout webItemLayout;
    private boolean isOpen = false;
    private String[] webname = {"BBC六分钟英语","BBC地道英语","BBC新闻","BBC新闻词汇"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        context = WebActivity.this;
        initView();
        initEvent();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.post(playurlThread);
            }
        });
    }

    private void setView() {
        Glide.with(this).load(webPicurl).into(webPic);
        webTitle.setText(webTitleStr);
        switch (webType) {
            case TAB_TYPE_SIXMIN: {
                webtypeStr = "BBC六分钟英语";
            }
            break;
            case TAB_TYPE_LOCALENGLISH: {
                webtypeStr = "BBC地道英语";
            }
            break;
            case TAB_TYPE_BBCNEWS: {
                webtypeStr = "BBC新闻";
            }
            break;
            case TAB_TYPE_NEWSWORD: {
                webtypeStr = "BBC新闻词汇";
            }
            break;
        }
        webTime.setText(webtypeStr+"\t\t\t"+webTimeStr);
    }

    private void initEvent() {
        playBtn.setOnClickListener(this);
        enAndCh.setOnClickListener(this);
        englishTxt.setOnClickListener(this);
        allTxt.setOnClickListener(this);
        webMenu.setOnClickListener(this);
    }

    private void initView() {
        //获取数据
        webTitleStr = getIntent().getStringExtra("webtitle");
        webPicurl = getIntent().getStringExtra("webpic");
        webTimeStr = getIntent().getStringExtra("webtime");
        webType = getIntent().getIntExtra("webtype", 0);
        url = "http://staticvip.iyuba.com/sounds/minutes/"+getIntent().getStringExtra("webmp3");
        //初始化控件
        musicProgress = (SeekBar) findViewById(R.id.progressBar);
        webLayout = (LinearLayout) findViewById(R.id.web_layout);
        webLayout.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        playBtn = (ImageView) findViewById(R.id.play);
        webPic = (ImageView) findViewById(R.id.web_pic);
        webTitle = (TextView) findViewById(R.id.web_title);
        webTime = (TextView) findViewById(R.id.type_date);
        enAndCh = (TextView) findViewById(R.id.en_ch);
        selectTxt(enAndCh);
        englishTxt = (TextView) findViewById(R.id.en_txt);
        allTxt = (TextView) findViewById(R.id.all_txt);
        backImg = (ImageView) findViewById(R.id.back_tomain);
        backImg.setVisibility(View.VISIBLE);

        webMenu = (TextView) findViewById(R.id.home);
        webMenu.setText("Menu");

        weblistView = (ListView) findViewById(R.id.web_list);
        webItemLayout = (LinearLayout) findViewById(R.id.web_item_layout);
        adapter = new ArrayAdapter(context,R.layout.name_item,webname);
        weblistView.setAdapter(adapter);
        setView();

        progressBar.setVisibility(View.GONE);
        webLayout.setVisibility(View.VISIBLE);
        //初始化音乐播放器
        nowTime = (TextView) findViewById(R.id.nowtime);
        fullTime = (TextView) findViewById(R.id.fulltim);
        player = new MyPlayer(musicProgress,nowTime,fullTime);


    }
    Runnable playurlThread = new Runnable() {
        @Override
        public void run() {
            player.playUrl(url);
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play: {
                if (isPlay == false) {
                    playBtn.setImageResource(R.mipmap.pause);
                    player.play();
                    isPlay =true;
                } else {
                    playBtn.setImageResource(R.mipmap.play);
                    player.pause();
                    isPlay = false;
                }
            }
            break;
            case R.id.en_ch: {
                clearAllTxt();
                selectTxt(enAndCh);
            }break;
            case R.id.en_txt: {
                clearAllTxt();
                selectTxt(englishTxt);
            }break;
            case R.id.all_txt: {
                clearAllTxt();
                selectTxt(allTxt);
            }break;
            case R.id.back_tomain: {
                this.finish();
                backImg.setVisibility(View.GONE);
            }break;
            case R.id.home: {
                if (isOpen == false){
                    isOpen = true;
                    showList();
                }else {
                    isOpen = false;
                    hideList();
                }
            }break;
        }
    }
    private void clearAllTxt(){
        enAndCh.setBackgroundColor(getResources().getColor(R.color.unselect_color));
        englishTxt.setBackgroundColor(getResources().getColor(R.color.unselect_color));
        allTxt.setBackgroundColor(getResources().getColor(R.color.unselect_color));
    }
    private void selectTxt(TextView txt){
        txt.setBackgroundColor(getResources().getColor(R.color.select_color));
    }
    //展示菜单
    private void showList(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(webItemLayout, "translationY", 0.0F, weblistView.getDividerHeight()*1.0f)
                .setDuration(1000);
        anim.start();
    }

    //隐藏菜单
    private void hideList(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(webItemLayout, "rotationY", 0.0F, -(weblistView.getDividerHeight()*1.0f))
                .setDuration(1000);
        anim.start();
    }
    // 进度改变
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }

    }
    private void showProgress(){

        progressBar.setVisibility(View.VISIBLE);

    }
    private void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player = null;
            handler.removeCallbacks(playurlThread);
        }
    }

}
