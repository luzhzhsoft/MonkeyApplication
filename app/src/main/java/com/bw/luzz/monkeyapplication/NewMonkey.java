package com.bw.luzz.monkeyapplication;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luzz on 2016/3/9.
 */
public class NewMonkey extends AccessibilityService{
    private ArrayList<EventCommand> commands;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsondata=intent.getStringExtra("json");
            commands=CommendParse.jsonToEventCommands(jsondata);
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    };
    private AccessibilityNodeInfo nodeInfo;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(getApplicationContext(),"服务启动",Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter=new IntentFilter(MonkeyService.class.getName());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        EventCommand firstCommand=commands.get(0);

        if(firstCommand.getmEventType()==event.getEventType()&&event.getPackageName().equals(firstCommand.getmClassName())){
            nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null) {
                executeCommandByType(firstCommand);
            }
        }
    }
    private void executeCommandByType(EventCommand e){
        List<AccessibilityNodeInfo> list;
        switch (e.getmNodeType()){
            case EventCommand.FIND_TEXT:
                list= nodeInfo.findAccessibilityNodeInfosByText(e.getmTextValue());
                break;
            case EventCommand.FIND_RESURCEID:
                list=nodeInfo.findAccessibilityNodeInfosByViewId(e.getmTextValue());
                break;
            default:
                throw new  RuntimeException("找不到"+e.getmNodeType()+"节点类型的处理逻辑");
        }
        if(!list.isEmpty()){
            for (AccessibilityNodeInfo node : list) {
                node.performAction(e.getmAction());
            }
        }
    }
    @Override
    public void onInterrupt() {
        Toast.makeText(getApplicationContext(),"服务中断连接",Toast.LENGTH_SHORT).show();
    }
}
