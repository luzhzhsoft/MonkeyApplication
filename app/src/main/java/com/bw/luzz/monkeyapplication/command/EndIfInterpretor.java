package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/6.
 */
public class EndIfInterpretor extends CommandInterpretor{
    @Override
    public String interprete(String command) {
    	
        return null;
    }
    private static EndIfInterpretor mEndIfInterpretor;
    private EndIfInterpretor(){
    	RunResult.throwErro("执行了 EndIf。if循环写错了");
    }
    public static EndIfInterpretor getInstance(){
        if(mEndIfInterpretor==null){
            synchronized (EndIfInterpretor.class){
                if(mEndIfInterpretor==null){
                    mEndIfInterpretor=new EndIfInterpretor();                   
                }
            }
        }
        return mEndIfInterpretor;
    }
}
