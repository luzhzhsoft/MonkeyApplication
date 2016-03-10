package com.bw.luzz.monkeyapplication;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;

/**
 * Created by Luzz on 2016/3/9.
 */
public class CommandAdapter {
    private ArrayList<EventCommand> commands;

    public CommandAdapter(ArrayList<EventCommand> commands) {
        this.commands = commands;
    }
    public void executeCommands(AccessibilityEvent event){

    }

}
