package com.bw.luzz.monkeyapplication;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import android.support.v4.content.LocalBroadcastManager;

import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonkeyService extends AccessibilityService {

    public static final String COMMAND_RESULT = "COMMAND_RESULT";
/*    public static final String DEBUG="test";*/
    private AccessibilityNodeInfo nodeInfo;
    private volatile String mCommandData;
    protected volatile List<EventCommand> commands=new ArrayList<>();
    protected  List<String> classNames=new ArrayList<>();
/*    protected volatile Iterator<EventCommand> iterator;
    private EventCommand lastEventCommand;
    private String lastClassName;
    private Thread timeThread;*/
/*    private int nameNumber=0;
    private long startTime=0;
    private volatile long endTime=0;*/
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData(intent.getStringExtra("json"));
            parseJson();
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            //timeRun();
        }
    };

    /*private void timeRun(){

         if(!(timeThread==null)&&!timeThread.isInterrupted())
            timeThread.interrupt();
        timeThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("eee", "interupt");
                }

            }
        });
        timeThread.start();
    }*/

    private Iterator<EventCommand> eventCommandIterator;

    public MonkeyService() {
        super();
        setData(" [" +
                "    {" +
                "      \"mClassName\": \"com.miui.home.launcher.Launcher\"," +
                "      \"mAction\": \"click\"," +
                "      \"mEventType\": \"window\"," +
                "      \"mNodeType\": \"text\"," +
                "      \"mTextValue\": \"设置\"" +
                "    }," +
                "    {" +
                "      \"mClassName\": \"com.tencent.mm.ui.LauncherUI\"," +
                "      \"mAction\": \"click\"," +
                "      \"mEventType\": \"window\"," +
                "      \"mNodeType\": \"text\"," +
                "      \"mTextValue\": \"订阅号\"" +
                "    }" +
                "  ]");
        parseJson();
    }


    private synchronized void setData(String command){
        this.mCommandData=command;
        //parseJson();
    }
    private void parseJson(){
        commands.clear();
        classNames.clear();
        //nameNumber=0;
        try {
            JSONArray jsonArray=new JSONArray(mCommandData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                EventCommand eventCommand=new EventCommand(jsonObject.getString("mClassName"),jsonObject.getString("mEventType"),jsonObject.getString("mNodeType"),jsonObject.getString("mAction"),jsonObject.getString("mTextValue"));
                commands.add(eventCommand);
                classNames.add(eventCommand.getmClassName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //lastEventCommand=commands.get(commands.size()-1);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        IntentFilter intentFilter=new IntentFilter(MonkeyService.class.getName());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Intent ib=new Intent(COMMAND_RESULT);
                LocalBroadcastManager.getInstance(MonkeyService.this).sendBroadcast(ib);
                Toast.makeText(getApplicationContext(),"执行超时",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        //Log.d(DEBUG,"classname::"+className);
        mhandler.removeMessages(1);
        if(className.equals("android.app.Dialog")){
            Message msg=Message.obtain();
            msg.what=1;
            mhandler.sendMessageDelayed(msg, 5000);
        }
        /*if(nameNumber<classNames.size()){
            String str=classNames.get(nameNumber);
            if(className.equals(str)){
                long tmep=startTime;

                startTime=System.currentTimeMillis();
                if(tmep!=0){
                    endTime=System.currentTimeMillis()-tmep;
                    if(endTime>3000){
                        //Log.d(DEBUG,"超时"+endTime);
                    }
                }else {
                    endTime=0;
                }

                nameNumber++;
            }
        }else if (nameNumber==classNames.size()){
            endTime=System.currentTimeMillis()-startTime;
            startTime=System.currentTimeMillis();
            if(endTime>3000){
                //Log.d(DEBUG,"超时"+endTime);
            }else {
               // Log.d(DEBUG,"脚本即将结束");
            }
        }*/
        //lastClassName=className;
        eventCommandIterator = commands.iterator();
        while (eventCommandIterator.hasNext()){
            EventCommand e= eventCommandIterator.next();
            executeCommand(eventType, className, e);
        }
    }

    private void executeCommand(int eventType, String className, EventCommand e) {
        if(eventType==e.getmEventType()&& className.equals(e.getmClassName())){
            nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null) {
                executeCommandByType(e);
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
            eventCommandIterator.remove();
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    public void onInterrupt() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
