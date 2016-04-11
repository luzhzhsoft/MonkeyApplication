package com.bw.luzz.monkeyapplication.command;

public class CellInterpretor extends CommandInterpretor{
	@Override
	public String interprete(String command) {
		// TODO Auto-generated method stub
		String[] coms=command.split("\n");
		//循环嵌套标志位
		int nestFlag=0;
		String commandUnity="";
		for(int i=0;i<coms.length;i++){
        	
        	String temp=coms[i].trim();
        	boolean isLackInfo=temp.endsWith(KeyWorld.IF)||temp.equals(KeyWorld.While)||temp.equals(KeyWorld.Switch)||temp.equals(KeyWorld.For);
        	
        	if(isLackInfo){
        		throw new RuntimeException("缺少条件");
        	}
        	if(temp.startsWith(KeyWorld.IF+KeyWorld.Blank)){
        		nestFlag++;
        	}else if(temp.startsWith(KeyWorld.Switch+KeyWorld.Blank)){
        		nestFlag++;
        	}else if(temp.startsWith(KeyWorld.While+KeyWorld.Blank)){
        		nestFlag++;
        	}else if(temp.startsWith(KeyWorld.For+KeyWorld.Blank)){
        		nestFlag++;
        	}else if(temp.equals(KeyWorld.End)){
        		nestFlag--;
        	}
        	if(!commandUnity.equals("")){
        		commandUnity=commandUnity+KeyWorld.NewLine;
        	}
        	commandUnity=commandUnity+temp; 
        	if(nestFlag<0) 
    			throw new RuntimeException("循环嵌套出错，是否多写了End，或者嵌套出错");
        	if(nestFlag==0){
        		
        	}else{
        		if(i==coms.length-1&&nestFlag>0){
        			throw new RuntimeException("循环无法结束，是否缺少End关键字？");
        		}
        		continue;
        	}
        	BananaRunner.execute(commandUnity);
            
            commandUnity="";
        }


		//先不支持返回
		return null;
	}
	  private static CellInterpretor mIfInterpretor;
	    private CellInterpretor(){}
	    public static CellInterpretor getInstance(){
	        if(mIfInterpretor==null){
	            synchronized (IfInterpretor.class){
	                if(mIfInterpretor==null){
	                    mIfInterpretor=new CellInterpretor();
	                }
	            }
	        }
	        return mIfInterpretor;
	    }
	

}
