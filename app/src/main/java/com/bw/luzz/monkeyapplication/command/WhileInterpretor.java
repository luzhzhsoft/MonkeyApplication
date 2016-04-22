package com.bw.luzz.monkeyapplication.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhileInterpretor extends CommandInterpretor{
	
	private static final String regex="(While[\\w\\W]*?\n)([\\w\\W]*?\\s*End$)";
	@Override
	public String interprete(String command) {
		// TODO Auto-generated method stub

		Pattern p=Pattern.compile(regex);
		Matcher matcher=p.matcher(command);
		if(!matcher.matches()){
			RunResult.throwErro("factory内部错误："+command);
			
		}
		
		
		String condition=matcher.group(1).replaceFirst("\\b"+KeyWorld.While+"\\b", "").trim();
		String body=matcher.group(2).replaceFirst("\\s*End$","");
		
		while(BananaRunner.judge(condition)){
			String result=BananaRunner.execute(body);
			if(result!=null&&result.equals(KeyWorld.Break)){
				break;
			}
			if(result!=null&&BananaRunner.execute(body).equals(KeyWorld.Continue)){
				continue;
			}
		}				
		return null;
	}
	

	
	private static WhileInterpretor mWhileInterpretor;
    private WhileInterpretor(){}
    public static WhileInterpretor getInstance(){
        if(mWhileInterpretor==null){
            synchronized (IfInterpretor.class){
                if(mWhileInterpretor==null){
                	mWhileInterpretor=new WhileInterpretor();
                }
            }
        }
        return mWhileInterpretor;
    }
	
    
    public static void main(String[] args){
    	 String test="While true\n"+
    			 		"TracePrint:4\n"+
    			 		"TracePrint:2\n"+
    			 		"Continue \n"+
    			 		"TracePrint:3\n"+
    			 		
    			 		"End";
    	 WhileInterpretor.getInstance().interprete(test);
    }
	
}
