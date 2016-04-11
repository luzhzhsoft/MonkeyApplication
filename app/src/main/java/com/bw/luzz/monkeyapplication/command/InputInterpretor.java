package com.bw.luzz.monkeyapplication.command;


import android.util.Log;

import com.bw.luzz.monkeyapplication.BananaService;
import com.bw.luzz.monkeyapplication.MonkeyInput;

/**
 * Created by Luzz on 2016/4/6.
 */
public class InputInterpretor extends  CommandInterpretor{
    @Override
    public String interprete(String command) {
    	//System.out.println("input::"+command);
        command=command.trim();
        Log.d(BananaService.BANANA_SERVICE,command);
        MonkeyInput.sendCommands(command.split(" "));
        return null;
    }

    private static InputInterpretor mInputInterpretor;
    private InputInterpretor(){}
    public static InputInterpretor getInstance(){
        if(mInputInterpretor==null){
            synchronized (InputInterpretor.class){
                if(mInputInterpretor==null){
                    mInputInterpretor=new InputInterpretor();

                }
            }
        }
        return mInputInterpretor;
    }
}
