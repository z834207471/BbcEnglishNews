package com.newsenglish.gary.myapplication.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.newsenglish.gary.myapplication.R;
import com.newsenglish.gary.myapplication.WebActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2017/8/25.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<String> pics;
    private List<String> titles;
    private Context context;

    public MyPagerAdapter(Context context, ArrayList<String> pics, ArrayList<String> titles) {
        this.context = context;
        this.pics = pics;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newposition = position % 4;
        View view = LayoutInflater.from(context).inflate(R.layout.player_item, null);
        TextView playerTitle = view.findViewById(R.id.player_title);
        ImageView playerImg = view.findViewById(R.id.player_img);
        if (pics.size() > 0 && titles.size() > 0) {
            Glide.with(context).load(pics.get(newposition)).into(playerImg);
            playerTitle.setText(titles.get(newposition));
        }
        RelativeLayout rlayout = view.findViewById(R.id.player_layout);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
