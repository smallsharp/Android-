package com.cmall.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 应用流量
 * @author cm
 *
 */
public class Flow {
	public static void main(String[] args) throws Exception {
		ArrayList<String> packages = getpackage();
		for (int i = 0; i < packages.size(); i++) {
			String uid = getuid(packages.get(i));
			System.out.println("包名为" + packages.get(i) + "的WIFI流量消耗为：" + getWifiFlow(uid) / 1024 + "KB");
			System.out.println("包名为" + packages.get(i) + "的GPRS流量消耗为：" + getGprsFlow(uid) / 1024 + "KB");
			System.out.println("......");
		}
		System.out.println("所有的应用流量消耗已获取完成");
	}

	/**
	 * 获取设备中的所有第三方应用
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getpackage() throws Exception {

		Process p = Runtime.getRuntime().exec("adb shell pm list packages -3");
		InputStream in = p.getInputStream();
		InputStreamReader ir = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ir);
		String str;
		ArrayList<String> list = new ArrayList<>();
		while ((str = br.readLine()) != null) {
			list.add(str.trim().split(":")[1]);
			str = br.readLine();

		}
		return list;
	}

	/**
	 * 获取应用的userId
	 * @param packagename
	 * @return
	 * @throws Exception
	 */
	public static String getuid(String packagename) throws Exception {
		Process p = Runtime.getRuntime().exec("adb shell dumpsys package " + packagename + " |grep userId");
		InputStream in = p.getInputStream();
		InputStreamReader ir = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ir);
		String uid = br.readLine().split("=")[1].split(" ")[0];
		return uid;
	}

	public static float getWifiFlow(String uid) throws IOException {
		Process p = Runtime.getRuntime().exec("adb shell cat /proc/net/xt_qtaguid/stats |grep " + uid + " |grep wlan0");
		InputStream in = p.getInputStream();
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		String str;
		float total = 0;
		while ((str = br.readLine()) != null) {
			total = total + Integer.parseInt(str.split(" ")[5]) + Integer.parseInt(str.split(" ")[7]);
			str = br.readLine();
		}
		return total;
	}

	public static float getGprsFlow(String uid) throws IOException {
		Process p = Runtime.getRuntime()
				.exec("adb shell cat /proc/net/xt_qtaguid/stats | grep " + uid + " |grep rmnet0");
		InputStream in = p.getInputStream();
		InputStreamReader ir = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ir);
		String str;
		float total = 0;
		while ((str = br.readLine()) != null) {
			total = total + Integer.parseInt(str.split(" ")[5]) + Integer.parseInt(str.split(" ")[7]);
			str = br.readLine();
		}
		return total;
	}

}
