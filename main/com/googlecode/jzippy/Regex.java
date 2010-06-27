package com.googlecode.jzippy;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;

class Regex {

	private static final RECompiler COMPILER = new RECompiler();
	private final RE exp;
	
	public Regex(String exp){
		this.exp = new RE();
		this.exp.setProgram(COMPILER.compile(exp));
	}
	
	public boolean match(String s){
		return exp.match(s);
	}
	
	public int size(){
		return exp.getParenCount();
	}
	
	public String get(int i){
		return exp.getParen(i);
	}
}