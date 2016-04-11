package com.bw.luzz.monkeyapplication.command;


/**
 * Created by Luzz on 2016/4/6.
 */
public class BananaThread {
    private String coms;
    public BananaThread setScript(String script){
        this.coms = script;
        return this;
    }
    public void run(){
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BananaRunner.execute(coms);
			}
		});
        thread.setName("bananarunner thread");
        thread.start();
    }

    private static BananaThread mBananaRunner;
    private BananaThread(){}
    public static BananaThread getInstance(){
        if(mBananaRunner==null){
            synchronized (BananaThread.class){
                if(mBananaRunner==null){
                    mBananaRunner=new BananaThread();
                }
            }
        }
        return mBananaRunner;
    }
    public static void main(String[] args){
    	BananaThread bn= BananaThread.getInstance();
    	String script="TracePrint:游戏kais \n"+
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
                "TracePrint:游戏结束";
    	bn.setScript(script);
    	bn.run();
    	
    }



}
