package com.bw.luzz.monkeyapplication.command;

import java.math.MathContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luzz on 2016/4/6.
 */
public class IfInterpretor extends CommandInterpretor{
    @Override
    public String interprete(String command) {
    	
    	// \bIf\b[\w\W]*?\bThen\b[\w\W]*?\bElseIF\b
    	//\bIf\b[\w\W]*?\bThen\b[\w\W]*?\bElse\b[\w\W]*\bEnd\b
    	Pattern pattern=Pattern.compile("(\\bIf\\b[\\w\\W]*?)(\\bThen\\b[\\w\\W]*?\\bElseIf\\b)");
    	Pattern patternElse=Pattern.compile("(\\bIf\\b[\\w\\W]*?)(\\bThen\\b[\\w\\W]*?)(\\bElse\\b[\\w\\W]*\\bEnd\\b)");
    	Pattern patternEnd=Pattern.compile("(\\bIf\\b[\\w\\W]*?)(\\bThen\\b[\\w\\W]*?)\\bEnd\\b");
    	Matcher matcher=pattern.matcher(command);
    	if(matcher.find()){   		
    		String cmIf=matcher.group(1).replace(KeyWorld.IF,"");    		
    		String cmThen=matcher.group(2).replace(KeyWorld.Then, "").replaceFirst("\\b"+KeyWorld.ElseIf+"\\b", "");
    		if (InterptetorFactory.getInterpretor(cmIf).interprete(cmIf).equals(KeyWorld.True)){
    			InterptetorFactory.getInterpretor(cmThen).interprete(cmThen);
        		return null;
            }else {
            	String cm=matcher.group();
            	String next=command.replaceFirst(cm,"");
            	next=KeyWorld.IF+" "+next;
            	this.interprete(next);
            }
    	}else{
    		String cmIf="";
    		String cmThen="";
    		String cmElse="";
    		Matcher matcher2=patternElse.matcher(command);
    		Matcher endMatcher=patternEnd.matcher(command);
    		if(!matcher2.matches()){
    			//RunResult.throwErro("if ѭ������");
    			if(endMatcher.matches()){
    				cmIf=endMatcher.group(1).replace(KeyWorld.IF,"");
    	    		 cmThen=endMatcher.group(2).replace(KeyWorld.Then, "");
    			}else{
    				RunResult.throwErro("执行 if 循环错误,缺少end？："+command);
    			}
    		}else{
    			cmIf=matcher2.group(1).replace(KeyWorld.IF,"");
       		 cmThen=matcher2.group(2).replace(KeyWorld.Then, "");
    			cmElse=matcher2.group(3).replaceFirst(KeyWorld.Else, "").replaceFirst("\\b"+KeyWorld.End+"\\b","");
    		}
    		 
    		 
    		if (InterptetorFactory.getInterpretor(cmIf).interprete(cmIf).equals(KeyWorld.True)){
    			InterptetorFactory.getInterpretor(cmThen).interprete(cmThen);
        		return null;
            }else {
            	InterptetorFactory.getInterpretor(cmElse).interprete(cmElse);
            }   		
    	}   	
        return null;
    }
    private static IfInterpretor mIfInterpretor;
    private IfInterpretor(){}
    public static IfInterpretor getInstance(){
        if(mIfInterpretor==null){
            synchronized (IfInterpretor.class){
                if(mIfInterpretor==null){
                    mIfInterpretor=new IfInterpretor();
                }
            }
        }
        return mIfInterpretor;
    }
    
    public static void main(String[] args){
    	String str="If 10=10 \n"+
    			"Then TracePrint:fdsadf \n"+
    			"ElseIf false \n"+
    			"Then TracePfds"
    			+ "fdsa"
    			+ "dfsgdsElseIfrint:iggyf \n"+
    			"Else TracePrint:luzzz\n"+
    			"End";
    	IfInterpretor.getInstance().interprete(str);
    }
}
