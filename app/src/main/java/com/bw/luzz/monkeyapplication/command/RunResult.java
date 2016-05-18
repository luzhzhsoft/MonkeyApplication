package com.bw.luzz.monkeyapplication.command;

import android.util.Log;

/**
 * �Խű������з��������ſ��ܸĳ���־
 * @author Luzz
 *
 */
public class RunResult {
	public static void throwErro(String message) throws InterruptedException {
		Log.e("BanaService",""+message);
		throw new InterruptedException();
	}
}
