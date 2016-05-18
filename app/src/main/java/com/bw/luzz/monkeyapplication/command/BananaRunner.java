package com.bw.luzz.monkeyapplication.command;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Luzz on 2016/4/6.
 */
public class BananaRunner {
	private static Map<String,CommandInterpretor> keyworldMap=new HashMap<>();
	public static CommandInterpretor getInterpretor(String command) throws InterruptedException {
		if(Thread.interrupted()){
			Log.d("BananaService","interrupted");
			throw new InterruptedException("线程终止");
		}
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
		}else if(commandKey[0].startsWith(KeyWorld.Break)){
			return BaseValueInterpretor.getInstance();
		}else if(commandKey[0].startsWith(KeyWorld.Continue)){
			return BaseValueInterpretor.getInstance();
		}else if(commandKey[0].startsWith(KeyWorld.Dim)){
			return DimInterpretor.getInstance();
		}else if(commandKey[0].startsWith(KeyWorld.CmpColor)){
			return CmpColorInterpretor.getInstance(BananaThread.getInstance().getContext());
		}else if(commandKey[0].startsWith(KeyWorld.StartActivity)){
			return StartActivityInterpretor.getInstance(BananaThread.getInstance().getContext());
		}else if(commandKey[0].startsWith(KeyWorld.GetPixelColor)){
			return GetPixelColorInterpretor.getInstance(BananaThread.getInstance().getContext());
		}else if(keyworldMap.containsKey(commandKey[0])){
			return keyworldMap.get(commandKey[0]);
		}else if(commandKey[0].equals("true")||commandKey[0].equals("false")){
			return BaseValueInterpretor.getInstance();
		}
		else {
			Pattern pequail=Pattern.compile("[=+*/%]|(\\bmod\\b)");
			Pattern pmaohao=Pattern.compile("\"[\\w\\W]*?\"");
			//数字的正则
			Pattern digtal=Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
			Matcher matcher=pequail.matcher(cmd);
			Matcher mach2=pmaohao.matcher(cmd);
			Matcher mach3=digtal.matcher(cmd);
			if(matcher.find()){
				return SimpleOperator.getInstance();
			}else if(mach2.matches()||mach3.matches()){
				return BaseValueInterpretor.getInstance();
			}else{
				//throw new RuntimeException("不支持的语法"+cmd);
				RunResult.throwErro("不支持的语法:"+cmd);
				return null;
			}
		}
	}
	public static boolean judge(String condition) throws InterruptedException {
		String re=execute(condition);
		Boolean is=Boolean.valueOf(re);
		if(is.booleanValue()){
			return true;
		}else{
			return false;
		}

	}
	public static String execute(String command) throws InterruptedException {
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			return BananaRunner.getInterpretor(command).interprete(command);


	}





	public static void addKey(String name,CommandInterpretor type){
		keyworldMap.put(name,type);
	}
	public static void clearAllKeyWorld(){
		keyworldMap.clear();
	}
	public static void removeKey(String name){
		keyworldMap.remove(name);
	}
	public static boolean containKey(String key){
		return keyworldMap.containsKey(key);
	}
	public static CommandInterpretor get(String key){
		return keyworldMap.get(key);
	}
	public static void main(String[] args){
    	/*Pattern digtal=Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    	Matcher mach3=digtal.matcher("10");
    	System.out.println(""+mach3.matches());*/
	    	/*String str="If 14 = 10 + 4 \n"+
	    				"Then TracePrint:Hello \n"+
	    				"TracePrint:Hello \n"+
	    				"End";*/
		String str="Dim i=14 \n"+
				"i=13 \n"+
				"If i == 10 + 3 \n"+
				"Then TracePrint:20 \n"+
				"TracePrint:19 \n"+
				"End";
		String jsonData="Dim i=1 \n"+
				"While true\n" +
				"Delay 1000 \n"+
				"If i==1 \n" +
				"Then i=i+1 \n" +
				"ElseIf i==2 \n" +
				"Then i=i+1 \n" +
				"ElseIf i==3 \n" +
				"Then TracePrint:i \n"
				+ "Break \n" +
				"End\n" +
				"End \n";

			//BananaRunner.execute(jsonData);


	}

}
