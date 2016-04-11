package com.bw.luzz.monkeyapplication.command;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������Ľ�������
 * @author Luzz
 *
 */
public abstract class OperatorInterpretor extends CommandInterpretor{
	private Stack<OperatorInterpretor> childs=new Stack<>();
	private final static String OperatorAdd="+";
	private final static String OperatorSubtract="-";
	private final static String OperatorMultiply="*";
	private final static String OperatorDivide="/";
	private final static String OperatorMod="\\bmod\\b";
	private final static String OperatorEqual="=";
	
	
	//private final static String PriorityFormLeft="([\\w\\W]*$)";
	private final static String PriorityMax="([+-])|(\\bmod\\b)";
	private final static String PriorityMid="[*/]";
	private final static String PriorityMin="=";
	Pattern min=Pattern.compile(PriorityMin);
	
	Pattern mid=Pattern.compile(PriorityMid);
	Pattern max=Pattern.compile(PriorityMax);
	/*private final static Matcher addMatcher=Pattern.compile(Oper)*/

	@Override
	public final String interprete(String command) {
		// TODO Auto-generated method stub
		//������ȼ����н���

		Matcher ma=min.matcher(command);
		Matcher mb=mid.matcher(command);
		Matcher mc=max.matcher(command);
		String priority="";
		
		int offset=1;
		
		if(ma.find()){
			//String[] coms=command.split(PriorityMin);
			priority=PriorityMin;
			if(ma.group(ma.groupCount()).equals("="))
				childs.push(EqualInterpretor.getInstance());		
			
		}else if(mb.find()){
			priority=PriorityMid;
		}else if(mc.find()){
			priority=PriorityMax;
			String op=mc.group(mc.groupCount()-1);
			if(op.equals("+")){
				childs.push(OperatorAddInterpretor.getInstance());
			}
		}else{
			RunResult.throwErro("未知错误在解析表达式时发生："+command);
		}
		String[] coms=command.split(priority);
		String right=coms[coms.length-1];
		String left=command.substring(0, command.length()-right.length()-offset);
		String leftResult=InterptetorFactory.execute(left);
		String rightResult=InterptetorFactory.execute(right);
		return childs.pop().interprete(leftResult,rightResult);
	}
	
	protected abstract String interprete(String leftResult,String rightResult);

}
