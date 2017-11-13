package com.cmall.fortest;

import java.io.File;
import org.apache.log4j.Logger;
import com.android.ddmlib.IDevice;
import com.cmall.android.DDMlib;
import com.cmall.android.Performance;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Testcase2 {
	
	private static Logger log = Logger.getLogger(Testcase2.class);
	
	public static void main(String[] args) {
		File file = new File("c:/abc.xls");
		writeToExcel(file, "第一个", 10, 1);
	}

	public static void writeToExcel(File file, String sheetName, int times, long intervals)  {
		DDMlib ddmlib = null;
		try {
			WritableWorkbook writeBook = Workbook.createWorkbook(file);
			WritableSheet writableSheet = writeBook.createSheet(sheetName, 1);

			ddmlib = DDMlib.getInstance();
			ddmlib.init();
			IDevice device = ddmlib.getDevice();

			writableSheet.addCell(new Label(0, 0, "应用包名"));
			writableSheet.addCell(new Label(1, 0, "com.play.android"));

			writableSheet.addCell(new Label(0, 1, "应用名称"));
			writableSheet.addCell(new Label(1, 1, "Play"));

			writableSheet.addCell(new Label(0, 2, "应用PID"));
			writableSheet.addCell(new Label(1, 2, "32200"));

			writableSheet.addCell(new Label(0, 3, "总内存(MB)"));
			writableSheet.addCell(new Label(1, 3, Performance.getTotalMemory() + ""));

			writableSheet.addCell(new Label(0, 4, "CPU型号"));
			writableSheet.addCell(new Label(1, 4, device.getProperty("ro.product.cpu.abilist")));

			writableSheet.addCell(new Label(0, 5, "Android系统版本"));
			writableSheet.addCell(new Label(1, 5, device.getProperty("ro.build.version.release")));

			writableSheet.addCell(new Label(0, 6, "手机型号"));
			writableSheet.addCell(new Label(1, 6, device.getProperty("ro.product.name")));

			String title[] = { "时间", "应用占用内存PSS(M)", "应用使用内存占比(%)", "应用cpu使用占比(%)" };
			for (int i = 0; i < title.length; i++) {
				writableSheet.addCell(new Label(i, 7, title[i]));
			}
			
			Performance performance = new Performance();
//			for (int i = 0; i < times; i++) {
//				List<String> appList = performance.getPerformance("com.tude.android");
//				log.info("第 " + i + "次获取性能数据成功");
//				for (int j = 0; j < appList.size(); j++) {
//					log.info(appList.get(j));
//				}
//				for (int x = 0; x < appList.size(); x++) {
//					// 第0列,第8,9…n行，开始追加数据
//					writableSheet.addCell(new Label(x, i + 8, appList.get(x)));
//				}
//				Thread.sleep(intervals);
//			}
			writeBook.write();
			writeBook.close();
			file.delete();
			ddmlib.finish();
		} catch (Exception e) {
			System.out.println("异常了！！！");
		}
		
	}

}
