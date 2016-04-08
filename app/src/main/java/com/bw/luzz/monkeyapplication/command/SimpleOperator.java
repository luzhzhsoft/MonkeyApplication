package com.bw.luzz.monkeyapplication.command;

public class SimpleOperator extends OperatorInterpretor{

	@Override
	protected String interprete(String leftResult, String rightResult) {
		// TODO Auto-generated method stub
		RunResult.throwErro("ʹ�����������û���κ�����r(�s���t)�q");
		return null;
	}
	  private static SimpleOperator mIfInterpretor;
	    private SimpleOperator(){}
	    public static SimpleOperator getInstance(){
	        if(mIfInterpretor==null){
	            synchronized (IfInterpretor.class){
	                if(mIfInterpretor==null){
	                    mIfInterpretor=new SimpleOperator();
	                }
	            }
	        }
	        return mIfInterpretor;
	    }
	    public static void main(String[] args){
	    	String str="If 12=10+2 \n"+
	    				"Then TracePrint:Hello \n"+
	    				"End";
	    	SimpleOperator.getInstance().interprete(str);
	    }
}
