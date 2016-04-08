package com.bw.luzz.monkeyapplication.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFac {
	public static void main(String[] args){
		String str="\"a\"";
		//Pattern p=Pattern.compile("=");
		Pattern p=Pattern.compile("\"[\\w\\W]*?\"");
		Matcher ma=p.matcher(str);		
		System.out.println(str);
		System.out.println(ma.matches());
	}
}
