package com.bw.luzz.monkeyapplication;

/**
 * Created by Luzz on 2016/3/7.
 * 命令语法错误
 */
public class CommandSyntaxError extends RuntimeException{
    public CommandSyntaxError() {
    }

    public CommandSyntaxError(String detailMessage) {
        super(detailMessage);
    }
}
