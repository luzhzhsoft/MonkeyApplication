package com.bw.luzz.monkeyapplication.command;

/**
 * Created by Luzz on 2016/4/6.
 */
public abstract class CommandInterpretor {
    public abstract String interprete(String command) throws InterruptedException;
}
