package com.example.simulateradio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RadioService extends Service {
    private final static String TAG = "RadioService ";
    private static final int MAX_PROGRESS = 100;   //进度条最大值

    private int progress = 0;     //进度条的进度值
    private OnPlayProgressListener progressListener; //更新进度的回调接口
    private boolean isPlying = false;    //判断是否重复按下播放键


    public RadioService() {
    }

    public void setProgressListener(OnPlayProgressListener progressListener) {
        this.progressListener = progressListener;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service create!");
    }

    /**
     * 返回一个RadioBinder对象
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new RadioBinder();

    }

    /**
     * 模拟电台播放
     * 如果处于播放状态则提示正在播放
     * 反之则进行播放，每秒进行更新
     */
    public void startPlayRadio() {
        if (isPlying) {
            Toast.makeText(this, "电台正在播放中......", Toast.LENGTH_SHORT).show();
        } else {
            isPlying = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progress < MAX_PROGRESS && isPlying) {
                        progress++;
                        if (progressListener != null) {
                            progressListener.onProgress(progress);
                        }
                        Log.d(TAG, "电台正在播放中,进度==" + progress);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 模拟电台暂停
     */
    public void stopPlayRadio() {
        isPlying = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service onUnBind");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "Service destroy");
    }

    public class RadioBinder extends Binder {
        /**
         * 获取当前Service实例
         * @return
         */
        RadioService getService() {
            return RadioService.this;
        }
    }
}
