package com.cmall.refer;

public class StringTest {

	public static void main(String[] args) {
		
		
		String string = "46 wlan0 0x0 10138 0 182222 140 3735 68 182222 140 0 0 0 0 3735 68 0 0 0 0 "
				+ "47 wlan0 0x0 10138 1 34758093 26188 953573 14335 34758093 26188 0 0 0 0 953573 14335 0 0 0 0";
		
		
		String reString = "46 wlan0 0x0 10138 0 182222 140 3735 68 182222 140 0 0 0 0 3735 68 0 0 0 0";
		
		
		
//		String rece1 = string.split("\n")[0].split(" ")[5];
//		String rece2 = string.split("\n")[1].split(" ")[5];
		
		String s = reString.split(" ")[5];
		
		System.out.println(s);
		
		
		
		
	}

}
