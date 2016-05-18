package com.bw.luzz.monkeyapplication.command;
//先检查然后执行
public abstract class CheckKeyInterpretor extends OperatorInterpretor{

	@Override
	protected final String interprete(String leftResult, String rightResult) throws InterruptedException {
		// TODO Auto-generated method stub
		leftResult=leftResult.trim();
		rightResult=rightResult.trim();
		leftResult=getValueCheckKey(leftResult);
		rightResult=getValueCheckKey(rightResult);
		
		
		return interpreteChecked(leftResult, rightResult);
	}
	
	protected abstract String interpreteChecked(String leftResult,String rightResult );
	private String getValueCheckKey(String key) throws InterruptedException {
		if(BananaRunner.containKey(key)){
			//(IValue)(BananaRunner.get(key));
			IValue valuecontains;
			if(BananaRunner.get(key) instanceof IValue){
				valuecontains=(IValue)BananaRunner.get(key);
				return valuecontains.getValue(key);
			}
			RunResult.throwErro("无法为指定的key赋值："+key);
		}
		
		return key;
	}
}
