package com.bw.luzz.monkeyapplication;
import android.annotation.TargetApi;

import org.junit.Test;
/**
 * Created by Luzz on 2016/5/26.
 */
public class Test8 {
    @Test
    public  void test(){
        System.out.println( new test1().cal(15));
    }
    public class test1 implements Formula{

        @Override
        public double cal(int a) {
            return sqar(a);
        }
    }
    interface Formula{
        double cal(int a);
        @TargetApi(24)
        default double sqar(int n){
            return n*n;
        }
    }

}
