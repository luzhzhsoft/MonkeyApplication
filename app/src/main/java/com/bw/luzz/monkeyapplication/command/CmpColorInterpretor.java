package com.bw.luzz.monkeyapplication.command;

import android.content.Context;

import com.bw.luzz.monkeyapplication.BitmapUtil;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luzz on 2016/4/11.
 */
public class CmpColorInterpretor extends CommandInterpretor{
    private WeakReference<Context> weakReference;
    //cmpColor(100,200,-544009)
    //cmpColor\((\d+),(\d+),([-]?\d+)\)
    @Override
    public String interprete(String command) {
        String noBlankCommand=command.trim().replace(" ","");
        String rex="CmpColor\\((\\d+),(\\d+),([-]?\\d+)\\)";
        Matcher matcher=Pattern.compile(rex).matcher(noBlankCommand);
        if(matcher.matches()){
            int x=Integer.valueOf(matcher.group(1));
            int y=Integer.valueOf(matcher.group(2));
            int pixel=Integer.valueOf(matcher.group(3));
            /*System.out.print("x:"+x+y+":"+pixel);
            return null;*/
            return ""+BitmapUtil.cmpColor(x,y,pixel,weakReference.get());
        }else {
            RunResult.throwErro("错误的格式："+command);
            return null;
        }
    }

    private static CmpColorInterpretor mCmpColorInterpretor=null;

    private CmpColorInterpretor(Context context) {
        weakReference=new WeakReference<Context>(context);
    }
    public static CmpColorInterpretor getInstance(Context context){
        if(mCmpColorInterpretor==null){
            synchronized (CmpColorInterpretor.class){
                if(mCmpColorInterpretor==null){
                    mCmpColorInterpretor=new CmpColorInterpretor(context);
                }
            }
        }
        return mCmpColorInterpretor;
    }

}
