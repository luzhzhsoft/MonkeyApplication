package com.bw.luzz.monkeyapplication.command;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.bw.luzz.monkeyapplication.BitmapUtil;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luzz on 2016/4/11.
 */
public class CmpColorInterpretor extends CommandInterpretor{
    public static boolean isDebug=false;
    private WeakReference<Context> weakReference;
    //cmpColor(100,200,-544009)
    //cmpColor\((\d+),(\d+),([-]?\d+)\)
    //cmpColor(100,200,-5400121,0.9)
    //
    @Override
    public String interprete(String command) {
        String noBlankCommand=command.trim().replace(" ","");
        //没有相似参数
        String rex="CmpColor\\((\\d+),(\\d+),([-]?\\d+)\\)";
        String rex2="CmpColor\\((\\d+),(\\d+),([-]?\\d+),(0\\.\\d)\\)";
        Matcher matcher=Pattern.compile(rex).matcher(noBlankCommand);
        Matcher matcher2=Pattern.compile(rex2).matcher(noBlankCommand);
        if(matcher.matches()){
            int x=Integer.valueOf(matcher.group(1));
            int y=Integer.valueOf(matcher.group(2));
            int pixel=Integer.valueOf(matcher.group(3));
            /*System.out.print("x:"+x+y+":"+pixel);
            return null;*/
            return ""+BitmapUtil.cmpColor(x,y,pixel,weakReference.get());
        }else if(matcher2.matches()){
            int x=Integer.valueOf(matcher2.group(1));
            int y=Integer.valueOf(matcher2.group(2));
            int pixel=Integer.valueOf(matcher2.group(3));
            float fraction=Float.valueOf(matcher2.group(4));
            if(isDebug){
                System.out.print(""+x+","+y+","+pixel+","+fraction);
                return null;
            }
            return ""+BitmapUtil.cmpColor(x,y,pixel,fraction,weakReference.get());
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
