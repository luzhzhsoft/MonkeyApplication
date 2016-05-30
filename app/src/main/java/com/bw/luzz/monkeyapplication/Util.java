package com.bw.luzz.monkeyapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public static void execCommand(String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("su");
        OutputStream outputStream=proc.getOutputStream();
        DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
        dataOutputStream.writeBytes(command);
        dataOutputStream.flush();
        dataOutputStream.close();
        outputStream.close();
        new Thread(()->{
            try {
                Thread.sleep(10000);
                proc.destroy();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
            /*if (proc.waitFor() != 0) {
                //System.err.println("exit value = " + proc.exitValue());
                Log.d("input","exitvalue:"+proc.exitValue());
            }*/
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                //stringBuffer.append(line+"-");
                Log.d("input",line);
            }
            Log.d("input","end############################################################################################################################################");
            in.close();
            //System.out.println(stringBuffer.toString());



    }
    /**
     * 使应用获得root权限
     * @param pkgCodePath 包路径
     *
     * @return
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;

        DataOutputStream os = null;
        try {
            String cmd="chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
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
