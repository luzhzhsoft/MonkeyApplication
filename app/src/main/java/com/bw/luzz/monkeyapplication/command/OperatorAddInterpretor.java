package com.bw.luzz.monkeyapplication.command;

public class OperatorAddInterpretor extends OperatorInterpretor{
	private static OperatorAddInterpretor mIfInterpretor;
    private OperatorAddInterpretor(){}
    public static OperatorAddInterpretor getInstance(){
        if(mIfInterpretor==null){
            synchronized (IfInterpretor.class){
                if(mIfInterpretor==null){
                    mIfInterpretor=new OperatorAddInterpretor();
                }
            }
        }
        return mIfInterpretor;
    }
	@Override
	protected String interprete(String leftResult, String rightResult) {
		// TODO Auto-generated method stub
		int left=Integer.valueOf(leftResult);
		int right=Integer.valueOf(rightResult);
		int res=left+right;
		return res+"";
	}
    
    
}
