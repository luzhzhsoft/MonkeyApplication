package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/6.
 */
public class EqualInterpretor extends CheckKeyInterpretor{
	
    private static EqualInterpretor mEqualInterpretor;
    private EqualInterpretor(){}
    public static EqualInterpretor getInstance(){
        if(mEqualInterpretor==null){
            synchronized (EqualInterpretor.class){
                if(mEqualInterpretor==null){
                    mEqualInterpretor=new EqualInterpretor();
                }
            }
        }
        return mEqualInterpretor;
    }
	@Override
	protected String interpreteChecked(String leftResult, String rightResult) {
		// TODO Auto-generated method stub
		if(leftResult.equals(rightResult)){
			return "true";
		}else{
			return "false";
		}
	}
	
}
