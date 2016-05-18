package com.bw.luzz.monkeyapplication.command;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.bw.luzz.monkeyapplication.Util;

/**
 * Created by Luzz on 2016/5/5.
 */
public class StartActivityInterpretor extends CommandInterpretor{
    public static boolean DEBUG=false;
    Context mContext;

    /**
     * @param command
     * @return 返回null吧
     */
    @Override
    public String interprete(String command) throws InterruptedException {
        command=command.trim();
        command=command.replace(KeyWorld.StartActivity+":","").trim();
        if(DEBUG){
            System.out.print(command);
            return null;
        }
        if(mContext==null){
            RunResult.throwErro("context 未赋予");
        }
        Log.d("BananaService",""+command);
        
        Util.startAppByName(command,mContext);
        return null;
    }

    private static StartActivityInterpretor mStartActivityInterpretor;
        private StartActivityInterpretor(Context context){
            this.mContext=context;
        }
        public static StartActivityInterpretor getInstance(Context context){
            if(mStartActivityInterpretor==null){
                synchronized (StartActivityInterpretor.class){
                    if(mStartActivityInterpretor==null){
                        mStartActivityInterpretor=new StartActivityInterpretor(context);
                    }
                }
            }
            return mStartActivityInterpretor;
        }
}
