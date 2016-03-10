package com.bw.luzz.monkeyapplication;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Luzz on 2016/2/29.
 */
public class EventCommand {
    private String mTextValue;
    private int mEventType;
    //节点类型 text resourcesid
    private String mNodeType;
    private int mAction;
    private String mClassName;
    /**
     * 命令状态
     * 成功，失败，错误，空闲，执行中
     */
    public static final int COMMAND_STATE_SUCCESS = 0;
    public static final int COMMAND_STATE_FAIL = 1;
    public static final int COMMAND_STATE_ERRO = 2;
    public static final int COMMAND_STATE_IDLE = 3;
    public static final int COMMAND_STATE_RUNNING = 4;
    private int mCommandState;
    public static final String WINDOW_STATE_CHANGED="window";
    public static final String FIND_TEXT="text";
    public static final String FIND_RESURCEID="id";
    public static final String ACTIN_CLICK="click";


    public EventCommand(String mClassName,String mEventType, String mNodeType, String mAction, String mTextValue) {
        setmEventType(mEventType);
        setmNodeType(mNodeType);
        setmAction(mAction);
        this.mTextValue=mTextValue;
        this.mClassName=mClassName;
        this.mCommandState=COMMAND_STATE_IDLE;
    }

    public int getmCommandState() {
        return mCommandState;
    }

    public void setmCommandState(int mCommandState) {
        this.mCommandState = mCommandState;
    }

    public String getmClassName() {
        return mClassName;
    }

    public void setmClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public int getmEventType() {
        return mEventType;
    }

    public void setmEventType(String mEventType) {
        if(mEventType.equals(WINDOW_STATE_CHANGED)){
            this.mEventType = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        }else {
            throw new RuntimeException("当前仅支持窗口状态改变window,不支持"+mEventType);
        }

    }

    public String getmNodeType() {
        return mNodeType;
    }

    public void setmNodeType(String mNodeType) {
        this.mNodeType=mNodeType;
    }

    public int getmAction() {
        return mAction;
    }

    public void setmAction(String mAction) {
        if(mAction.equals(ACTIN_CLICK)){
            this.mAction= AccessibilityNodeInfo.ACTION_CLICK;
        }else {
            throw new RuntimeException("当前仅支持click事件");
        }
    }

    public String getmTextValue() {
        return mTextValue;
    }

    public void setmTextValue(String mTextValue) {
        this.mTextValue = mTextValue;
    }
}
