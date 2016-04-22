package com.bw.luzz.monkeyapplication.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Luzz on 2016/4/15.
 */
public class DimInterpretor extends CommandInterpretor implements IValue{
    private  Map<String,Refrence> mMap=new HashMap<>();
    //用来区分变量的生命周期。为0则为全局变量。
    
    private int scope=0;
    private int hasScope=0;
    //\bDim\b\s*\w\s*=\s*\w*
    @Override
    public String interprete(String command) {
    	command=command.trim();
    	if(command.trim().startsWith(KeyWorld.Dim)){
    		String cmd=command.trim().replaceFirst("\\b"+KeyWorld.Dim+"\\b","").trim();

           // String[] cmds=cmd.split(" ");
            String value=null;
            String left=cmd;
            if(cmd.contains("=")){
                String right=cmd.split("=")[1].trim();
                left=cmd.split("=")[0].trim();
                value=BananaRunner.execute(right);
            
            }
            mMap.put(left,new Refrence(scope,value));
            BananaRunner.addKey(left, getInstance());
            hasScope=hasScope<scope?scope:hasScope;
            return null;
    	}else{ 
    		//等以后再重构吧。╮(╯▽╰)╭
    		if(command.contains("==")){
    			String[] strs=command.split("==");
    			String left=strs[0].trim();
    			String right=strs[1];
    			left=get(left);
    			right=BananaRunner.execute(right).trim();
    			if(left.equals(right)){
                	return "true";
                }else{
                	return "false";
                }
    		}else if(command.contains("=")){
    			String[] strs=command.split("=");
    			String left=strs[0].trim();
    			String right=strs[1].trim();
    			set(left,right);
    			
    		}else if(command.contains("+")){
    			String[] strs=command.split("+");
    			String left=strs[0].trim();
    			String right=strs[1];
    			left=get(left);
    			right=BananaRunner.execute(right).trim();
    			return ""+(Integer.valueOf(left)+Integer.valueOf(right));
    		}
    		return command;
    	}
        
    }
    
    
    //提供修改临时变量的方法
    public void set(String key,String value){
        mMap.get(key).value=value;
    }
    public String get(String key){
       return mMap.get(key).value;
    }
    private DimInterpretor(){
        CellInterpretor.getInstance().addListener(new CellInterpretor.Listener() {
            @Override
            public void onCircleBegin() {
                scope++;
            }

            @Override
            public void onCircleEnd() {
                scope--;
                if(scope<0)RunResult.throwErro("内部错误，解析出错。请联系开发者");
                if(scope>0&&hasScope>=scope){
                    //局部变量去除。
                    Set<Map.Entry<String,Refrence>> entrySet=mMap.entrySet();
                    for(Map.Entry<String,Refrence> entry:entrySet){
                        if(entry.getValue().scope==scope){
                            mMap.remove(entry.getKey());
                            BananaRunner.removeKey(entry.getKey());
                        }
                    }
                    if(hasScope>0)
                    hasScope--;
                }
            }
        });
    }
    private static DimInterpretor mDimInterpretor;

    public static DimInterpretor getInstance() {
        if (mDimInterpretor == null) {
            synchronized (DimInterpretor.class) {
                if (mDimInterpretor == null) {
                    mDimInterpretor = new DimInterpretor();
                }
            }
        }
        return mDimInterpretor;
    }
    class Refrence{
        public int scope;
        public String value;
        public Refrence(int scope,String value){
            this.scope=scope;
            this.value=value;
        }
    }
	@Override
	public String getValue(String args) {
		// TODO Auto-generated method stub
		
		return this.get(args);
	}
}
