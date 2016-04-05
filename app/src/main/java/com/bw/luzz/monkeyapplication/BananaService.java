package com.bw.luzz.monkeyapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 *不使用accessiblity的脚本服务
 */
public class BananaService extends Service {
    //执行脚本
    public static final int RUN_SCRIPT=1;
    public static final String BANANA_SERVICE = "BananaService";
    private static final String SCRIPT="script";
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Messenger service;


    public BananaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("BananaServiceThread");
        mHandler = new BananaHandler(mHandlerThread.getLooper());
        service = new Messenger(mHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(BANANA_SERVICE,"同进程调用");
        return START_STICKY;
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  service.getBinder();
    }
    private class BananaHandler extends Handler{

        private String script;

        public BananaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RUN_SCRIPT:
                    script = msg.getData().getString(SCRIPT);
                    Log.d(BANANA_SERVICE,"接收到的脚本为："+ script);
                    this.removeMessages(RUN_SCRIPT);

                    break;
            }
            super.handleMessage(msg);
        }
    }

}
