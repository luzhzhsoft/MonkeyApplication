package com.bw.luzz.monkeyapplication;

import java.io.DataOutputStream;
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
}
