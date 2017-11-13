package com.cmall.testcase;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.cmall.android.Performance;

public class TestCase {
	private Logger log = Logger.getLogger(TestCase.class);
	private static final String KEY_TOTAL_MEM = "total_mem";
	private static final String KEY_PSS = "pss";
	private static final String KEY_PSS_RATE = "pssRate";
	private static final String KEY_HEAP = "heap";
	private static final String KEY_CPU = "cpu";
	private static final String KEY_RECEIVED = "received"; // wifi
	private static final String KEY_SENDED = "sended"; // wifi
	private static final String KEY_TOTAL_FLOW = "total"; // wifi数据
	private static final String PKG_NATIVE_SDK = "com.taidu.andorid.example3d"; // 测试应用的packageName
	private static final String PKG_PLAY = "com.play.android";
	private static final String PKG_TUDE = "com.tude.android";
	private static final String PKG_RN = "com.usereactnative";
	private double lastR = 0; // 上次获取的WIFI数据
	private double lastS = 0;
	private double lastT = 0;

	/**
	 * 获取性能数据集合
	 * 
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Double> getMap(String pkgName) throws Exception {
		String uid = Performance.getUid(PKG_NATIVE_SDK);
		double totalMemrory = Performance.getTotalMemory();
		double pss = Performance.getPssMemory(pkgName);
		double heap = Performance.getHeapMemory(pkgName);
		double cpu = Performance.getCpuInfo(pkgName);
		double flow[] = Performance.getTotalWifiArr(uid);
		double received = flow[0];
		double sended = flow[1];
		double totalFlow = flow[2];
		Map<String, Double> map = new HashMap<>();
		map.put(KEY_TOTAL_MEM, totalMemrory);
		map.put(KEY_PSS, pss);
		map.put(KEY_PSS_RATE, pss/totalMemrory*100);
		map.put(KEY_HEAP, heap);
		map.put(KEY_CPU, cpu);
		map.put(KEY_RECEIVED, received-lastR);
		map.put(KEY_SENDED, sended-lastS);
		map.put(KEY_TOTAL_FLOW, totalFlow-lastT);
		lastR = flow[0];
		lastS = flow[1];
		lastT = flow[2];
		return map;
	}

//	@Test(description="写入文件操作")
	public void testWriteFile() throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		double totalMemrory = Performance.getTotalMemory();
		PrintWriter pw = new PrintWriter("E:/Android.txt","gbk");
		pw.println("设备的总内存(M)：" + df.format(totalMemrory));
		
		String title[] = { "PSS(M)","PSS(%)", "HEAP(M)", "CPU(%)", "IOR(KB)", "IOS(KB)" ,"IOT(KB)" };		//标题
		for (int i = 0; i < title.length; i++) {
			pw.print(title[i] + "| ");
		}
		pw.println();
		log.info("设备的总内存(M)：" + df.format(totalMemrory));		

		for (int i = 0; i < 200; i++) {
			Map<String, Double> map = getMap(PKG_NATIVE_SDK);
			pw.print(df.format(map.get(KEY_PSS))+ ",  ");
			pw.print(df.format(map.get(KEY_PSS_RATE))+ ",  ");
			pw.print(df.format(map.get(KEY_HEAP))+ ",  ");
			pw.print(df.format(map.get(KEY_CPU))+ ",  ");
			pw.print(df.format(map.get(KEY_RECEIVED)) +",  ");
			pw.print(df.format(map.get(KEY_SENDED))+ ",  ");
			pw.print(df.format(map.get(KEY_TOTAL_FLOW)));
			pw.println();
			pw.flush();
			Thread.sleep(1000);
		}
		pw.close();
	}
	
	@Test(description="Android专项测试")
	public void testPrint() throws Exception {
		print(PKG_RN);
	}
	
	public void print(String packageName) throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		double totalMemrory = Performance.getTotalMemory();
		log.info("Total:" + totalMemrory);
		log.info("pss(M) | heap(M) | cpu(%) | flow(KB)");
		String uid = Performance.getUid(packageName);
		lastT = Performance.getTotalWifiArr(uid)[2];
		for (int i=0; i<200; i++) {
			double pss = Performance.getPssMemory(packageName);
			double heap = Performance.getHeapMemory(packageName);
			double cpu = Performance.getCpuInfo(packageName);
			double flow[] = Performance.getTotalWifiArr(uid);
			double nowFlow = flow[2]-lastT;
			log.info("[P]:"+df.format(pss) + ", [H]:" + df.format(heap) + ", [C]:" + df.format(cpu) + ", [F]:" + df.format(nowFlow));
			lastT = flow[2];
		}
	}

}
