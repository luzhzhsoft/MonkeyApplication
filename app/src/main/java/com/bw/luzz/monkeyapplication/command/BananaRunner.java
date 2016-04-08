package com.bw.luzz.monkeyapplication.command;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created by Luzz on 2016/4/6.
 */
public class BananaRunner {
    private String coms;
    public void setScript(String script){
        coms = script;
    }
    public void run(){
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InterptetorFactory.execute(coms);
			}
		});
        thread.setName("bananarunner thread");
        thread.start();
    }

    private static BananaRunner mBananaRunner;
    private BananaRunner(){}
    public static BananaRunner getInstance(){
        if(mBananaRunner==null){
            synchronized (BananaRunner.class){
                if(mBananaRunner==null){
                    mBananaRunner=new BananaRunner();
                }
            }
        }
        return mBananaRunner;
    }
    public static void main(String[] args){
    	BananaRunner bn=BananaRunner.getInstance();
    	String script="TracePrint:��Ϸkais \n"+ 
    			"If  \"a\" = \"a\" \n" +
                "Then TracePrint: helloee \n" +
    			"Delay 6000 \n"+
    			"TracePrint:what \n"+
    			"End \n"+
                "If  \"a\" = \"b\" \n" +
                "Then TracePrint: secod \n"+
                "If dfs\n"+
                "End \n"+
                "End \n"+
                "TracePrint:��Ϸ����";
    	bn.setScript(script);
    	bn.run();
    	
    }



}
