package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/6.
 * ����ʹ�á�����if��������
 */
public class ThenInterpretor extends CommandInterpretor{
    private boolean IsExecute=true;
    @Override
    public String interprete(String command) {
        if(IsExecute){
            CommandInterpretor CI=InterptetorFactory.getInterpretor(command);
            return CI.interprete(command.replace("Then",""));
        }else {
            return "gone";
        }
    }
    public void setIsExecute(boolean isExecute){
        this.IsExecute=isExecute;
    }
    private static ThenInterpretor mThenInterpretor;
    private ThenInterpretor(){
    	RunResult.throwErro("执行了Then，应该以if开始");
    }
    public static ThenInterpretor getInstance(){
        if(mThenInterpretor==null){
            synchronized (ThenInterpretor.class){
                if(mThenInterpretor==null){
                    mThenInterpretor=new ThenInterpretor();
                }
            }
        }
        return mThenInterpretor;
    }
}
