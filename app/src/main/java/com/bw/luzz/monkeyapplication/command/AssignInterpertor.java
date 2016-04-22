package com.bw.luzz.monkeyapplication.command;

public class AssignInterpertor extends OperatorInterpretor{
	private static AssignInterpertor mEqualInterpretor;
    private AssignInterpertor(){}
    public static AssignInterpertor getInstance(){
        if(mEqualInterpretor==null){
            synchronized (AssignInterpertor.class){
                if(mEqualInterpretor==null){
                    mEqualInterpretor=new AssignInterpertor();
                }
            }
        }
        return mEqualInterpretor;
    }
	@Override
	protected String interprete(String leftResult, String rightResult) {
		// TODO Auto-generated method stub
		/*if(leftResult.equals(rightResult)){
			return "true";
		}else{
			return "false";
		}*/
		//RunResult.throwErro("理论上不会调到这个方法。理论上");
		leftResult=leftResult.trim();
		rightResult=rightResult.trim();
		//以后有了函数可以定义的时候再拓展这里
		DimInterpretor.getInstance().set(leftResult, rightResult);
		return null;
	}
}
