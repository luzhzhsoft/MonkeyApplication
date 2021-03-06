package com.bw.luzz.monkeyapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.bw.luzz.monkeyapplication.command.BananaThread;

/**
 * 不使用accessiblity的脚本服务
 */
public class BananaService extends Service {
    //执行脚本
    public static final int RUN_SCRIPT = 1;
    public static final String BANANA_SERVICE = "BananaService";
    public static final String SCRIPT = "script";
    private Messenger service;
    private static Context context;

    public BananaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread mHandlerThread = new HandlerThread("BananaServiceThread");
        mHandlerThread.start();
        Handler mHandler = new BananaHandler(mHandlerThread.getLooper());
        service = new Messenger(mHandler);
        context=getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(BANANA_SERVICE, "同进程调用");
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return service.getBinder();
    }

    private static final class BananaHandler extends Handler  {


        public BananaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RUN_SCRIPT:
                    this.removeMessages(RUN_SCRIPT);
                    String script=msg.getData().getString(BananaService.SCRIPT);
                    Log.d(BANANA_SERVICE,script);
                    WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

                    Display mDisplay=windowManager.getDefaultDisplay();
                    DisplayMetrics metrics=new DisplayMetrics();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        mDisplay.getRealMetrics(metrics);
                    }
                    BitmapUtil.dims[0]=metrics.widthPixels;
                    BitmapUtil.dims[1]=metrics.heightPixels;
                    BananaThread
                            .getInstance()
                            .setScript(script)
                            .run();
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
