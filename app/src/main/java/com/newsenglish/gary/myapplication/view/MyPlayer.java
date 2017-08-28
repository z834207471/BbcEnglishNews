package com.newsenglish.gary.myapplication.view;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gary on 2017/8/27.
 */

public class MyPlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer; // 媒体播放器
    private SeekBar seekBar; // 拖动条
    private TextView mNTime;
    private TextView mFTime;
    private List<String> havelist = new ArrayList<>();
    private boolean ishave = false;
    private Thread myThread ;
    // 初始化播放器
    public MyPlayer(SeekBar seekBar, TextView nTime, TextView fTime) {
        super();
        this.seekBar = seekBar;
        this.mNTime = nTime;
        this.mFTime = fTime;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 每一秒触发一次
        handler.postDelayed(run, 1000);
    }

    Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            SimpleDateFormat format = new SimpleDateFormat("mm:ss");
            mFTime.setText(format.format(duration));
            mNTime.setText(format.format(position));
            if (duration > 0) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                long pos = seekBar.getMax() * position / duration;
                seekBar.setProgress((int) pos);
            }
            handler.postDelayed(run, 1000);
        }
    };
    Runnable preparerun = new Runnable() {
        @Override
        public void run() {
            try {
                prepareVideo(havelist.get(havelist.size()-1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public void play() {
        mediaPlayer.start();
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare();
//
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(run);
        }
    }

    // 播放准备
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e("mediaPlayer", "onPrepared");
    }

    // 播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress = seekBar.getMax()
                * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.e(currentProgress + "% play", percent + " buffer");
    }

    private void prepareVideo(String remoteUrl) throws IOException {
        URL url = new URL(remoteUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) url
                .openConnection();
        httpConnection.setConnectTimeout(3000);
        httpConnection.setRequestProperty("RANGE", "bytes=" + 0 + "-");
        InputStream is = httpConnection.getInputStream();
        int videoTotalSize = httpConnection.getContentLength();
        if (videoTotalSize == -1) {
            return;
        }
        String name = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
        String localUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/VideoCache/" + File.separator + name;
        File cacheFile = new File(localUrl);
        if (!cacheFile.exists()) {
            cacheFile.getParentFile().mkdirs();
            cacheFile.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(cacheFile, "rws");
        raf.setLength(videoTotalSize);
        raf.seek(0);
        byte buf[] = new byte[10 * 1024];
        int size = 0;
        int videoCacheSize = 0;
        int buffercnt = 0;
        while ((size = is.read(buf)) != -1) {
            try {
                raf.write(buf, 0, size);
                videoCacheSize += size;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        is.close();
        raf.close();
    }


    private void playMusicBySDCar(String localUrl)
            throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(localUrl);
        mediaPlayer.prepare();

    }
}