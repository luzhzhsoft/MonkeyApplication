package com.bw.luzz.monkeyapplication.command;



import android.util.Log;

/**
 * Created by Luzz on 2016/4/6.
 */
public class TracePrintInterpreor  extends CommandInterpretor{
    @Override
    public String interprete(String command) {
        //Log.d("BananaService",""+command);
        System.out.print(""+command);
        return null;
    }
    private static TracePrintInterpreor mTracePrintInterpreor;
    private TracePrintInterpreor(){}
    public static TracePrintInterpreor getInstance(){
        if(mTracePrintInterpreor==null){
            synchronized (TracePrintInterpreor.class){
                if(mTracePrintInterpreor==null){
                    mTracePrintInterpreor=new TracePrintInterpreor();                    
                }
            }
        }
        return mTracePrintInterpreor;
    }
}
