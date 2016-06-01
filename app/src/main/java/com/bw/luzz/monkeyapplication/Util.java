package com.bw.luzz.monkeyapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Environment;
import android.support.annotation.RawRes;
import android.util.Log;

import com.bw.luzz.monkeyapplication.command.NoRootException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;

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
    public static void execCommand(String command) throws NoRootException {
        Runtime runtime = Runtime.getRuntime();
        try {
            final Process proc = runtime.exec("su");
            if(proc==null){
                throw new NoRootException();
            }
            OutputStream outputStream=proc.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(command);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();

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
        } catch (IOException e) {
            e.printStackTrace();
        }




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


 /*   public void copyResToSdcard(String name,Context context){//name为sd卡下制定的路径

        if(name==null||name.equals("")){

        }
        Field[] raw = R.raw.class.getFields();
        for (Field r : raw) {
            try {
                //     System.out.println("R.raw." + r.getName());
                int id=context.getResources().getIdentifier(r.getName(), "raw", context.getPackageName());
                if(!r.getName().equals("allapps")){
                    String path=name+"/"+r.getName()+".png";
                    BufferedOutputStream bufEcrivain = new BufferedOutputStream((new FileOutputStream(new File(path))));
                    BufferedInputStream VideoReader = new BufferedInputStream(context.getResources().openRawResource(id));
                    byte[] buff = new byte[20*1024];
                    int len;
                    while( (len = VideoReader.read(buff)) > 0 ){
                        bufEcrivain.write(buff,0,len);
                    }
                    bufEcrivain.flush();
                    bufEcrivain.close();
                    VideoReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/

    public static void copyRawToSdcard(String path,@RawRes int id,Context context){
        File file=new File(Environment.getExternalStorageDirectory().toString()+"/"+path);
        Log.d("Banb",""+file.toString());
        if(file.exists()){
            file.delete();
        }
        BufferedInputStream bufferedInputStream=null;
        BufferedOutputStream bufferedOutputStream=null;
        try {
            bufferedInputStream = new BufferedInputStream(context.getResources().openRawResource(id));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buff=new byte[20*1024];
            int len;
            while ((len= bufferedInputStream.read(buff))>0){
                bufferedOutputStream.write(buff,0,len);
            }
            bufferedOutputStream.flush();

            bufferedOutputStream.close();
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
