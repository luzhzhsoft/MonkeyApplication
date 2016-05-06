package com.bw.luzz.monkeyapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Luzz on 2016/3/24.
 */
public class Util {
    public static void execShell(String cmd){
        try {
            Process p=Runtime.getRuntime().exec("su");
            OutputStream outputStream=p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void takeScreenShot(String imagePath){
       if(imagePath.equals("")){
            imagePath= Environment.getExternalStorageDirectory()+ File.separator+"ScreenShot.png";

        }
        Log.d("imagepath",imagePath);
        execShell("su -c 'screencap " + imagePath);
    }


    /**
     * 使用包名加类型启动activity，没有写未找到的情况
     * @param pacagename
     * @param context
     */
    public static void startAppByName(String pacagename, Context context){
        Intent i=context.getPackageManager().getLaunchIntentForPackage(pacagename);
        if(i==null){
            Log.e("BananaService","根据包名未找到相应的activity"+pacagename);
        }else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            context.startActivity(i);
        }

    }
}
