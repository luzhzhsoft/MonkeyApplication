package com.bw.luzz.monkeyapplication.command;



import android.util.Log;

/**
 * Created by Luzz on 2016/4/6.
 */
public class TracePrintInterpreor  extends CommandInterpretor{
    @Override
    public String interprete(String command) {
        //Log.d("BananaService",""+command);
        //System.out.print(""+command);
        String delTitle=command.replace("TracePrint:","").trim();
        String result=BananaRunner.execute(delTitle);
        if(result.isEmpty()){
            RunResult.throwErro("函数"+delTitle+"的返回值为空");
        }
        printResult(result);
        return null;
    }

    private void printResult(String result) {
        Log.d("BananaService","pirnt:"+result);
        //System.out.print(""+result);
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
