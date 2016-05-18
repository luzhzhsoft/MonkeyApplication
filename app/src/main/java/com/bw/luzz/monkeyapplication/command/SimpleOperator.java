package com.bw.luzz.monkeyapplication.command;

public class SimpleOperator extends OperatorInterpretor{

	@Override
	protected String interprete(String leftResult, String rightResult) throws InterruptedException {
		// TODO Auto-generated method stub
		RunResult.throwErro("使用这个方法，没有任何意义╮(╯▽╰)╭");
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
	    	//SimpleOperator.getInstance().interprete(str);
	    }
}
