package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/11.
 */
public class CmpColorInterpretor extends CommandInterpretor{
    @Override
    public String interprete(String command) {
        return null;
    }

    private static CmpColorInterpretor mCmpColorInterpretor=null;

    private CmpColorInterpretor() {
    }
    public static CmpColorInterpretor getInstance(){
        if(mCmpColorInterpretor==null){
            synchronized (CmpColorInterpretor.class){
                if(mCmpColorInterpretor==null){
                    mCmpColorInterpretor=new CmpColorInterpretor();
                }
            }
        }
        return mCmpColorInterpretor;
    }

}
