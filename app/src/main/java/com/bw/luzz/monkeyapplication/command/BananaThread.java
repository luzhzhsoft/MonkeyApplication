package com.bw.luzz.monkeyapplication.command;


import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Luzz on 2016/4/6.
 */
public class BananaThread {
    //开启下一个线程时，是否停止上一个线程。默认true
    private boolean ClearFlag = true;
    private String coms;
    private WeakReference<Context> sContext;
    private Thread thread = null;

    public BananaThread setScript(String script) {
        this.coms = script;
        return this;
    }

    public BananaThread setContext(Context context) {
        sContext = new WeakReference<Context>(context);

        return this;
    }

    public Context getContext() {

        Context context = sContext.get();
        if (context != null) {
            return context;
        } else {
            throw new RuntimeException("未设置context。或者已经被系统回收");
        }
    }

    public void run() {
        if (ClearFlag&&thread!=null) {
            stopThreadSafe();
        }
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                //需要在执行前把定义的变量清空
                BananaRunner.clearAllKeyWorld();
                try {
                    BananaRunner.execute(coms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.setName("bananarunner thread");
        thread.start();
    }

    private void stopThreadSafe() {
            thread.interrupt();
        Log.d("BananaService",">>>>>>>>>>>>>>>>>>>>>>>>>>"+thread.isInterrupted());

    }

    public BananaThread setClearFlag(boolean clearFlag) {
        ClearFlag = clearFlag;
        return this;
    }

    private static BananaThread mBananaRunner;

    private BananaThread() {
    }

    public static BananaThread getInstance() {
        if (mBananaRunner == null) {
            synchronized (BananaThread.class) {
                if (mBananaRunner == null) {
                    mBananaRunner = new BananaThread();
                }
            }
        }
        return mBananaRunner;
    }

    public static void main(String[] args) {
        BananaThread bn = BananaThread.getInstance();
        String script = "TracePrint:游戏kais \n" +
                "If  \"a\" = \"a\" \n" +
                "Then TracePrint: helloee \n" +
                "Delay 6000 \n" +
                "TracePrint:what \n" +
                "End \n" +
                "If  \"a\" = \"b\" \n" +
                "Then TracePrint: secod \n" +
                "If dfs\n" +
                "End \n" +
                "End \n" +
                "TracePrint:游戏结束";
        bn.setScript(script);
        bn.run();

    }

}
