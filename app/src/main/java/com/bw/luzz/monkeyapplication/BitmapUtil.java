package com.bw.luzz.monkeyapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.YuvImage;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
    public static Bitmap getSystemScreen(Activity context){

        WindowManager windowManager=context.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point outsize=new Point();
        display.getSize(outsize);

        Bitmap bitmap=Bitmap.createBitmap(outsize.x,outsize.y, Bitmap.Config.ARGB_8888);

        View decorView=context.getWindow().getDecorView();
        bitmap=getDrawingCache(decorView);

        return bitmap;
    }

    //获取屏幕截图并保存。
    public static void getSystemScreen(Activity activity, String filepath, String filename){
        WindowManager windowManager=activity.getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point outsize=new Point();
        display.getSize(outsize);

        Bitmap bitmap=Bitmap.createBitmap(outsize.x, outsize.y, Bitmap.Config.ARGB_8888);

        View decorView=activity.getWindow().getDecorView();
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

    /**
     * 需要施放bitmap 的内存。否则会内存泄漏
     * @param context
     * @return
     */
    public static Bitmap getSystemScreen(Context context){
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        Display mDisplay=windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        mDisplay.getRealMetrics(metrics);
        float[] dims={metrics.widthPixels,metrics.heightPixels};
        Bitmap bitmap=null;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN_MR2){
            try {
                Class<?> clzz=Surface.class;
                Method m=clzz.getDeclaredMethod("screenshot",new Class[]{int.class,int.class});
                bitmap=(Bitmap)m.invoke(null,(int)dims[0],(int)dims[1]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(bitmap==null){
                throw new RuntimeException("需要系统签名才能调用此方法");
            }
            return bitmap;
        }
        try {
            Class<?> clzz=Class.forName("android.view.SurfaceControl");
            Method m=clzz.getDeclaredMethod("screenshot",new Class[]{int.class,int.class});
            bitmap=(Bitmap)m.invoke(null,(int)dims[0],(int)dims[1]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(bitmap==null){
            throw new RuntimeException("需要系统签名才能调用此方法");
        }
        return bitmap;
    }

    //获取屏幕截图并保存
    public static void getSystemScreen(Context context, String filepath, String filename){
        Bitmap bitmap= getSystemScreen(context);

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
        String pathSufix=Environment.getExternalStorageDirectory().toString()+"/";
        Bitmap bitmap= BitmapFactory.decodeFile(pathSufix+imagePath);
        Bitmap bitmap2=BitmapFactory.decodeFile(pathSufix+otherImagePath);
        if(bitmap==null){
            throw new FileNotFoundException("未找到图片："+imagePath);
        }
        if(bitmap2==null){
            throw  new FileNotFoundException("未找到图片："+otherImagePath);
        }
        return bitmap.sameAs(bitmap2);
    }

    /**
     * @param pixels 比对的像素点数组
     * @param otherPixels 比对的像素点数组
     * @return true 相同。false 不同；
     */
    public static boolean isSameByPixel(int[] pixels,int[] otherPixels){
        if(pixels.length!=otherPixels.length){
            throw new IllegalArgumentException("错误的参数。像素点的个数不同");
        }
        for(int i=0;i<pixels.length;i++){
            if(pixels[i]!=otherPixels[i]){
                return false;
            }

        }
        return true;
    }

    /**
     * 通过比对相应位置的bitmap像素点确定是否相同
     * @param bitmap
     * @param other
     * @param points 像素点的位置
     * @return 是否相同
     */
    public static boolean isSameByPosition(Bitmap bitmap,Bitmap other,Point[] points){
        for(Point p:points){
            if(bitmap.getPixel(p.x,p.y)!=bitmap.getPixel(p.x,p.y))
                return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getScreenBitma(){
    }

    /**
     * 对比指点点的颜色
     * @param x
     * @param y
     * @param color int类型 10 进制
     * @param context
     * @return
     */
    public static boolean cmpColor(int x, int y, int color, Context context){
        int screenColor = getPixelColor(x, y, context);
        if(screenColor==color){
            return true;
        }
        return false;

    }

    public static int getPixelColor(int x, int y, Context context) {
        Bitmap bitmap=getSystemScreen(context);
        int screenColor=bitmap.getPixel(x,y);
        bitmap.recycle();
        return screenColor;
        //return x*1000+y;
    }


}
