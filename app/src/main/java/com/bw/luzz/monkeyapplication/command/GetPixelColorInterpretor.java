package com.bw.luzz.monkeyapplication.command;

import android.content.Context;

import com.bw.luzz.monkeyapplication.BitmapUtil;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luzz on 2016/4/11.
 */
public class GetPixelColorInterpretor extends CommandInterpretor{
    private WeakReference<Context> weakReference;
    //GetPixelColor(100,200,-544009)
    //cmpColor\((\d+),(\d+),([-]?\d+)\)
    @Override
    public String interprete(String command) throws InterruptedException {
        String noBlankCommand=command.trim().replace(" ","");
        String rex="GetPixelColor\\((\\d+),(\\d+)\\)";
        Matcher matcher= Pattern.compile(rex).matcher(noBlankCommand);
        if(matcher.matches()){
            int x=Integer.valueOf(matcher.group(1));
            int y=Integer.valueOf(matcher.group(2));
            //System.out.print("x:"+x+y+":"+pixel);
            return ""+ BitmapUtil.getPixelColor(x,y,weakReference.get());
        }else {
            RunResult.throwErro("错误的格式："+command);
            return null;
        }
    }

    private static GetPixelColorInterpretor mGetPixelColorInterpretor=null;

    private GetPixelColorInterpretor(Context context) {
        weakReference=new WeakReference<Context>(context);
    }
    public static GetPixelColorInterpretor getInstance(Context context){
        if(mGetPixelColorInterpretor==null){
            synchronized (GetPixelColorInterpretor.class){
                if(mGetPixelColorInterpretor==null){
                    mGetPixelColorInterpretor=new GetPixelColorInterpretor(context);
                }
            }
        }
        return mGetPixelColorInterpretor;
    }
}
