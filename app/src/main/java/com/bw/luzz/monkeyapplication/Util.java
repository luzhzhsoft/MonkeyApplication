package com.bw.luzz.monkeyapplication;

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
}
