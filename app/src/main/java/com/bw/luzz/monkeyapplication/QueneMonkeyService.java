package com.bw.luzz.monkeyapplication;


import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

/**
 * Created by Luzz on 2016/3/1.
 * 顺序执行命令。只有当前一条命令完成，才会执行下一条
 */
public class QueneMonkeyService extends MonkeyService{

    private AccessibilityNodeInfo nodeInfo;
    private int count;
    private boolean isRepeat=false;

    public QueneMonkeyService(){
        super();
        this.count=0;
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        if(className.equals(commands.get((count+1>=commands.size())?(commands.size()-1):(count+1)).getmClassName())){
            count++;
        }
        if(count<commands.size()){
            EventCommand eventCommand=commands.get(count);

            executeCommand(eventType,className,eventCommand);
        }else{
            if(!isRepeat){
                stopSelf();
            }else {
                count=0;
                EventCommand eventCommand2=commands.get(count);

                executeCommand(eventType,className,eventCommand2);
            }
        }



    }
    private void executeCommand(int eventType, String className, EventCommand e) {
        if(eventType==e.getmEventType()&& className.equals(e.getmClassName())){
            nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null) {
                executeCommandByType(e);
            }else {
                if(count>0) count--;
            }
        }else {
            if(count>0) count--;
        }
    }

    private void executeCommandByType(EventCommand e){
        List<AccessibilityNodeInfo> list;
        switch (e.getmNodeType()){
            case EventCommand.FIND_TEXT:
                list= nodeInfo.findAccessibilityNodeInfosByText(e.getmTextValue());
                break;
            case EventCommand.FIND_RESURCEID:
                list= nodeInfo.findAccessibilityNodeInfosByViewId(e.getmTextValue());
                break;
            default:
                throw new  RuntimeException("找不到"+e.getmNodeType()+"节点类型的处理逻辑");
        }

        for (AccessibilityNodeInfo node : list) {
            node.performAction(e.getmAction());
        }

    }

}
