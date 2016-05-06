package com.bw.luzz.monkeyapplication;

import android.graphics.Color;
import android.provider.Settings;

import com.bw.luzz.monkeyapplication.command.BananaRunner;
import com.bw.luzz.monkeyapplication.command.CmpColorInterpretor;
import com.bw.luzz.monkeyapplication.command.IfInterpretor;

import org.junit.Test;

import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    /*@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }*/

    @Test
    public void testRunnerThread()throws Exception{
        String str="If 14 = 10 + 4 \n"+
                "Then TracePrint:Hello \n"+
                "TracePrint:Hello \n"+
                "End";
        BananaRunner.execute(str);
    }
    @Test
    public void testCmpColor(){
        CmpColorInterpretor.getInstance(null).interprete("CmpColor(100,300,-504199)");

    }
    @Test
    public void testIf(){
        String str="If 11=10 \n"+
                "Then TracePrint:15 \n"+
                "ElseIf false \n"+
                "Then  TracePrint:17\n"+
                "End";
        IfInterpretor.getInstance().interprete(str);

    }

    /**
     * X
     */
    @Test
    public void testCmpColorArgb(){
        int colorW= argb(0xff,100,100,100);

        int colorE=argb(0xff,89,89,89);

        int colorF=argb(0xff,100,100,100);

        int colorG=argb(0xff,102,102,102);

        System.out.print(BitmapUtil.cmpColorARGB(colorW,colorE,0.9f));
        System.out.print(BitmapUtil.cmpColorARGB(colorW,colorF,0.9f));

        System.out.print(BitmapUtil.cmpColorARGB(colorW,colorG,0.9f));

    }
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

}