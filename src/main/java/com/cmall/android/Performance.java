package com.cmall.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Android 性能测试
 * 
 * @author cm
 */
public class Performance {
	
	// 获取应用内存使用情况
	private static final String PSS = "adb shell dumpsys meminfo ";
	// 获取设备总内存等信息
	private static final String TOTAL_MEM = "adb shell cat /proc/meminfo";
	// 获取应用CPU使用情况
	private static final String CPU_INFO = "adb shell top -n 1 -d 1 -s cpu |grep ";

	/**
	 * 应用已经使用的PSS内存
	 * 单位：MB
	 * @param packageName
	 * @return
	 */
	public static double getPssMemory(String packageName) {
		String meminfo = getResponse(PSS, packageName);
		String str = null;
		try {
			str = meminfo.substring(meminfo.indexOf("TOTAL", 0), meminfo.indexOf("Objects", 0));
		} catch (Exception e) {
			throw new RuntimeException("返回数据异常："+ str);
		}
		String arr[] = str.split(" ");
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			if (s.length() > 0) {
				list.add(s);
			}
		}
		return Double.valueOf(list.get(1)) / 1024; 		// KB 转化为 M
	}

	/**
	 * 获取连接设备的总内存
	 * 单位：M
	 * @return
	 */
	public static double getTotalMemory() {
		String meminfo = getResponse(TOTAL_MEM);
		// MemTotal: 3867416 kB
		String total = null;
		try {
			total = meminfo.substring(meminfo.indexOf("MemTotal:"), meminfo.indexOf("kB"));
		} catch (Exception e) {
			throw new RuntimeException("返回数据异常："+ total);
		}
		return Double.valueOf(total.split(":")[1].trim()) / 1024;
	}

	/**
	 * Java堆内存使用  Heap Alloc
	 * 单位:MB
	 * @param packageName
	 * @return
	 */
	public static double getHeapMemory(String packageName) {
		String meminfo = getResponse(PSS, packageName);
		// Dalvik Heap 16530 32816 0 0 32324 30305 2019
		String str = null;
		try {
			str = meminfo.substring(meminfo.indexOf("Dalvik Heap"), meminfo.indexOf("Dalvik Other"));
		} catch (Exception e) {
			throw new RuntimeException("返回数据异常："+ str);
		}
		String arr[] = str.split(" ");
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			if (s.length() > 0) {
				list.add(s);
			}
		}
		return Double.valueOf(list.get(7)) / 1024;
	}

	/**
	 * 当前应用占用cpu的比例(%)
	 * adb shell top -n 1 -s cpu |findstr com.meitu.wheecam 
	 * @param packageName
	 * @return
	 */
	public static double getCpuInfo(String packageName) {
		String str = getResponse(CPU_INFO, packageName);
		// 21823 0 0% S 116 1948508K 197952K fg u0_a59 com.meitu.wheecam
		return Double.valueOf(str.substring(str.indexOf("%") - 3, str.indexOf("%")).trim());
	}

	/**
	 * 获取应用的在设备中的userId
	 * @param packagename
	 * @return
	 * @throws Exception
	 */
	public static String getUid(String packagename) throws Exception {
		String res = getResponse("adb shell dumpsys package " + packagename + "|grep userId");
		return res.split("=")[1];
	}
	
	
	/**
	 * 存放指定应用的WIFI数据，包含接收的总数据，发送的总数据，和接发总和
	 * arr[0]:R;
	 * arr[1]:S;
	 * arr[2]:R+S;
	 * 单位：KB (获取的是字节，需要转化为KB)
	 * @param uid 应用在设备中的userId
	 * @return
	 */
	public static double[] getTotalWifiArr(String uid) {
		
		double received = 0;
		double total = 0;
		double send = 0;
		try {
			Process p = Runtime.getRuntime().exec("adb shell cat /proc/net/xt_qtaguid/stats |grep " + uid + " |grep wlan0");
			InputStream in = p.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			String str;
			while ((str = br.readLine()) != null) {
				received += Double.parseDouble(str.split(" ")[5]);
				send += Double.parseDouble(str.split(" ")[7]);
				str = br.readLine();
			}
			total = send + received;
		} catch (IOException e) {
			e.printStackTrace();
		}
		double[] wifiArr = {received/1024,send/1024,total/1024};
		return wifiArr;
	}

	/**
	 * 执行外部命令，获取返回值
	 * 
	 * @param cmd
	 * @param packageName
	 * @return
	 */
	private static String getResponse(String cmd, String packageName) {
		StringBuffer sb = new StringBuffer();
		try {
			Process pro = Runtime.getRuntime().exec(cmd + packageName);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			// 判断应用进程是否已经启动，或中途断开
			if (sb.toString().startsWith("No process found") ||sb.toString().equals("")) {
				throw new RuntimeException("没有检测到进程，请打开应用！！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 执行外部命令，获取返回值
	 * 
	 * @param cmd
	 * @return
	 */
	private static String getResponse(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		Process pro = null;
		StringBuffer sb = null;
		try {
			pro = runtime.exec(cmd);
			if (pro.waitFor() != 0) {
				throw new RuntimeException("没有检测到设备连接,请检查！");
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			sb = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return sb.toString(); 
	}

}
