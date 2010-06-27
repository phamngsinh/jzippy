package com.googlecode.jzippy;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;

/**
 * Simple wrapper for the Apache regex classes.
 * @author ben
 */
class Regex {

	private static final RECompiler COMPILER = new RECompiler();
	private final RE exp;
	
	/**
	 * Creates a new expression wrapper.
	 * @param exp The expression.
	 */
	public Regex(String exp){
		this.exp = new RE();
		this.exp.setProgram(COMPILER.compile(exp));
	}
	
	/**
	 * Checks if the string matches.
	 * @param s The string.
	 * @return Whether or not the string matched the expression.
	 */
	public boolean match(String s){
		return exp.match(s);
	}
	
	/**
	 * Gets the number of elements that the expression returned during matching.
	 */
	public int size(){
		return exp.getParenCount();
	}
	
	/**
	 * Gets an element.
	 * @param i The index.
	 * @return The element at the given index.
	 */
	public String get(int i){
		return exp.getParen(i);
	}
}