package com.bw.luzz.monkeyapplication;
import android.test.mock.MockContext;

import com.bw.luzz.monkeyapplication.command.BananaRunner;
import com.bw.luzz.monkeyapplication.command.BananaThread;
import com.bw.luzz.monkeyapplication.command.StartActivityInterpretor;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Luzz on 2016/5/5.
 */
public class StartActivityInterpretorTest {

    @Before
    public  void  setDebug(){
        MockContext context=new MockContext();
        BananaThread.getInstance().setContext(context);
        StartActivityInterpretor.DEBUG=true;
    }

    @Test
    public  void testStartActiviy(){
        BananaRunner.execute("StartActivity:com.luzz.monkey \n");
    }
}
