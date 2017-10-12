package com.cmall.android;

public class PerformanceBean{
	
	private String time;
	private double pssMem;
	private double totalMem;
	private double heapMem;
	private double memRate;
	private double cpuRate;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getPssMem() {
		return pssMem;
	}
	public void setPssMem(double pssMem) {
		this.pssMem = pssMem;
	}
	public double getTotalMem() {
		return totalMem;
	}
	public void setTotalMem(double totalMem) {
		this.totalMem = totalMem;
	}
	public double getHeapMem() {
		return heapMem;
	}
	public void setHeapMem(double heapMem) {
		this.heapMem = heapMem;
	}
	public double getMemRate() {
		return memRate;
	}
	public void setMemRate(double memRate) {
		this.memRate = memRate;
	}
	public double getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(double cpuRate) {
		this.cpuRate = cpuRate;
	}

	
}
