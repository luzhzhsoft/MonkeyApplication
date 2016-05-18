package com.bw.luzz.monkeyapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by Luzz on 2016/5/17.
 */
public class BananaServiceConnection implements ServiceConnection {
    private Messenger bananaService;
    private Builder builder;
    private BananaServiceConnection(Builder builder) {
        this.builder=builder;
    }

    @Override
    public void onServiceConnected(ComponentName name,final IBinder service) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bananaService = new Messenger(service);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Intent i=new Intent(builder.context,BananaService.class);
        builder.context.bindService(i,this, Context.BIND_AUTO_CREATE);
    }


    public void sendMessage(String script){
        Message message=Message.obtain(null,BananaService.RUN_SCRIPT);
        Bundle data=new Bundle();
        data.putString(BananaService.SCRIPT,script);
        message.setData(data);
        try {
            bananaService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private Context context;
        private BananaServiceConnection mBananaServiceConnection;
        private Intent i;

        public Builder() {
        }

        public BananaServiceConnection buid(Context context){
            this.context=context;
            if(mBananaServiceConnection==null){
                synchronized (BananaServiceConnection.class){
                    if(mBananaServiceConnection==null){
                        mBananaServiceConnection=new BananaServiceConnection(this);

                    }
                }
            }

            if(i==null){
                synchronized (BananaServiceConnection.class){
                    if(i==null){
                        i=new Intent(context,BananaService.class);
                    }
                }
            }

            context.bindService(i, mBananaServiceConnection, Context.BIND_AUTO_CREATE);
            return mBananaServiceConnection;

        }

    }





}
