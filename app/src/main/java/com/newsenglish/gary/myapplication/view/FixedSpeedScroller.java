package com.newsenglish.gary.myapplication.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import static com.newsenglish.gary.myapplication.preferences.Preferences.DEFAULT_DURATION;

/**
 * Created by Gary on 2017/8/25.
 */

public class FixedSpeedScroller extends Scroller {
    private int mDuration = DEFAULT_DURATION;
    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
