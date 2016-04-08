package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/6.
 */
public class BaseValueInterpretor extends CommandInterpretor{
    @Override
    public String interprete(String command) {
        return command.trim();
    }
    private static BaseValueInterpretor mBaseValueInterpretor;
    private BaseValueInterpretor(){}
    public static BaseValueInterpretor getInstance(){
        if(mBaseValueInterpretor==null){
            synchronized (BaseValueInterpretor.class){
                if(mBaseValueInterpretor==null){
                    mBaseValueInterpretor=new BaseValueInterpretor();
                }
            }
        }
        return mBaseValueInterpretor;
    }
}
