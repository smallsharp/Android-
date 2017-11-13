package com.cmall.fortest;

import java.text.DecimalFormat;

public class Test {
	
	
	public static void main(String[] args) {
		
		
		DecimalFormat dFormat  = new DecimalFormat("0.00");
		
		double d = 0.1234;
		System.out.println(dFormat.format(d));
		
	}

}
