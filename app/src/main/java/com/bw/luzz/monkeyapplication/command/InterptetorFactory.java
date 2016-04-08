package com.bw.luzz.monkeyapplication.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.TimeLimitExceededException;

/**
 * Created by Luzz on 2016/4/6.
 */
public class InterptetorFactory {
	
    public static CommandInterpretor getInterpretor(String command){
        String cmd=command.trim();
        
        String circul="((If)|(While)|(For)|(Switch))[\\w\\W]*End$";
        Matcher prm=Pattern.compile(circul).matcher(command);
        if((!prm.matches())&&cmd.contains("\n")){     	
        	return CellInterpretor.getInstance();    	
        }
        String[] commandKey=cmd.split(" ");
        if(commandKey[0].equals("tap")){
            return InputInterpretor.getInstance();
        } else if(commandKey[0].equals("If")){
            return IfInterpretor.getInstance();
        }else if(commandKey[0].equals("EndIf")){
            return EndIfInterpretor.getInstance();
        }else if(commandKey[0].equals("Then")){
            return ThenInterpretor.getInstance();
        }else if(commandKey[0].startsWith("TracePrint")){
            return TracePrintInterpreor.getInstance();
        }else if(commandKey[0].startsWith(KeyWorld.Delay)){
            return DelayInterpretor.getInstance();
        }else if(commandKey[0].startsWith(KeyWorld.While)){
            return WhileInterpretor.getInstance();
        }else if(commandKey[0].equals("true")||commandKey[0].equals("false")){
            return BaseValueInterpretor.getInstance();
        }else {
        	Pattern pequail=Pattern.compile("[=+*/%]|(\\bmod\\b)");
        	Pattern pmaohao=Pattern.compile("\"[\\w\\W]*?\"");
        	//ƥ������
        	Pattern digtal=Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        	Matcher matcher=pequail.matcher(cmd);
        	Matcher mach2=pmaohao.matcher(cmd);
        	Matcher mach3=digtal.matcher(cmd);
        	if(matcher.find()){
        		return SimpleOperator.getInstance();
        	}else if(mach2.matches()||mach3.matches()){
        		return BaseValueInterpretor.getInstance();
        	}else{
        		throw new RuntimeException("��֧�ֵ��﷨"+cmd);
        	}        	
        }
    }   
    public static boolean judge(String condition){
		String re=execute(condition);
		Boolean is=Boolean.valueOf(re);
		if(is.booleanValue()){
			return true;
		}else{
			return false;
		}
		
	}	
	public static String execute(String command){
		return InterptetorFactory.getInterpretor(command).interprete(command);
	}
    public static void main(String[] args){
    	/*Pattern digtal=Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    	Matcher mach3=digtal.matcher("10");
    	System.out.println(""+mach3.matches());*/
	    	String str="If 14 = 10 + 4 \n"+
	    				"Then TracePrint:Hello \n"+
	    				"TracePrint:Hello \n"+
	    				"End";
	    	InterptetorFactory.execute(str);
	    
    }

}
