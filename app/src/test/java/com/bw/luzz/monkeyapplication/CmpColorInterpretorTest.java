package com.bw.luzz.monkeyapplication;

import android.test.mock.MockContext;

import com.bw.luzz.monkeyapplication.command.BananaThread;
import com.bw.luzz.monkeyapplication.command.CmpColorInterpretor;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Luzz on 2016/4/26.
 */
public class CmpColorInterpretorTest {
    @Before
    public void testInit(){
        CmpColorInterpretor.isDebug=true;

    }


    @Test
    public void testCmpColor(){
        String txt="CmpColor(100,200,-100024,0.8)";
        CmpColorInterpretor.getInstance(null).interprete(txt);
    }

}
