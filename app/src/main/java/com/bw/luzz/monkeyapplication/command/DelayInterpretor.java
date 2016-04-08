package com.bw.luzz.monkeyapplication.command;

public class DelayInterpretor extends CommandInterpretor{

	@Override
	public String interprete(String command) {
		// TODO Auto-generated method stub
		command.trim();
		String[] coms=command.split(" ");
		if(!coms[0].trim().equals(KeyWorld.Delay)){
			RunResult.throwErro("�ڲ�����"+command);
			
		}
		if(coms.length>2){
			RunResult.throwErro("�﷨����Ӧ��ΪDelay time:"+command);
		}
		long timeDealy= Long.valueOf(coms[1].trim());
		
		try {
			Thread.sleep(timeDealy);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static DelayInterpretor mDelayInterpretor;
    private DelayInterpretor(){}
    public static DelayInterpretor getInstance(){
        if(mDelayInterpretor==null){
            synchronized (IfInterpretor.class){
                if(mDelayInterpretor==null){
                	mDelayInterpretor=new DelayInterpretor();
                }
            }
        }
        return mDelayInterpretor;
    }
	
}
