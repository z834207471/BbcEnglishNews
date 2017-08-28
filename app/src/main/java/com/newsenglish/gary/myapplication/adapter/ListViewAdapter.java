package com.newsenglish.gary.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newsenglish.gary.myapplication.R;
import com.newsenglish.gary.myapplication.db.BbcData;
import com.newsenglish.gary.myapplication.db.BbcNews;
import com.newsenglish.gary.myapplication.db.SixMin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 2017/8/27.
 */

public class ListViewAdapter extends BaseAdapter {
    List a = new ArrayList< BbcNews>();
    private Context context;
    private List<BbcData> datalist;
    public ListViewAdapter(Context context) {
        this.context = context;
    }
    public void setListView(List<BbcData> list){
        if (datalist == null){
            this.datalist = list;
        }
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        View view = null;
        bbcViewHolder viewholder= null;
        if (convertview == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
            viewholder = new bbcViewHolder();
            viewholder.pic = view.findViewById(R.id.item_pic);
            viewholder.title = view.findViewById(R.id.item_title);
            viewholder.time = view.findViewById(R.id.item_time);
            viewholder.readcount = view.findViewById(R.id.item_readcount);
            view.setTag(viewholder);
        }else {
            view = convertview;
            viewholder = (bbcViewHolder) view.getTag();
        }
        Glide.with(context).load(datalist.get(i).getPic()).into(viewholder.pic);
        viewholder.title.setText(datalist.get(i).getTitle());
        viewholder.time.setText(datalist.get(i).getTime());
        viewholder.readcount.setText(datalist.get(i).getReadCount());
        Log.e("grgrgr", "getViewHeight: "+view.getMeasuredHeight() );
        return view;
    }
    public class bbcViewHolder{
        ImageView pic;
        TextView title;
        TextView time;
        TextView readcount;
    }
}
