package com.bw.luzz.monkeyapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Luzz on 2016/3/24.
 * 用来对bitmap进行操作的类
 */
public class BitmapUtil {
    //获取view缓存的bitmap
    public static Bitmap getDrawingCache(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap= view.getDrawingCache();
        /*view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();*/
        return bitmap;
    }
    //获取屏幕截图
    public static Bitmap getScreenBitmap(Activity context){
        WindowManager windowManager=context.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point outsize=new Point();
        display.getSize(outsize);

        Bitmap bitmap=Bitmap.createBitmap(outsize.x,outsize.y, Bitmap.Config.ARGB_8888);

        View decorView=context.getWindow().getDecorView();
        bitmap=getDrawingCache(decorView);

        return bitmap;
    }

    //获取屏幕截图并保存
    public static void getScreenBitmap(Activity context, String filepath, String filename){
        WindowManager windowManager=context.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point outsize=new Point();
        display.getSize(outsize);

        Bitmap bitmap=Bitmap.createBitmap(outsize.x, outsize.y, Bitmap.Config.ARGB_8888);

        View decorView=context.getWindow().getDecorView();
        bitmap=getDrawingCache(decorView);
        if(filepath.endsWith("/")){
            filepath=filepath.substring(0,filepath.length()-1);
        }
        if(filename.startsWith("/")){
            filename=filename.substring(1);
        }

        File path=new File(filepath);

        File file=new File(filepath+"/"+filename);
        if(!path.exists()) path.mkdir();

        try {
            if(!file.exists())file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file);
            if(fos!=null){
                bitmap.compress(Bitmap.CompressFormat.PNG,90,fos);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //对路径下的图片进行比对
    public static boolean isSame(String imagePath,String otherImagePath) throws FileNotFoundException{
        /*File file=new File(imagePath);
        File file2=new File(otherImagePath);*/

        Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
        Bitmap bitmap2=BitmapFactory.decodeFile(otherImagePath);
        if(bitmap==null){
            throw new FileNotFoundException("未找到图片："+imagePath);
        }
        if(bitmap2==null){
            throw  new FileNotFoundException("未找到图片："+otherImagePath);
        }
        return bitmap.sameAs(bitmap2);
    }




}
