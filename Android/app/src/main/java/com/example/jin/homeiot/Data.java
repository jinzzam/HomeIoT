package com.example.jin.homeiot;

import java.io.Serializable;

public class Data{
	private static final long serialVersionUID = 1L;
	private static Data instance = new Data();
	private int temperature;
	private int waterLine;
	private boolean thief;
	private boolean btn;
	private boolean entry;
	
	public Data() {
		/*pass*/
	}
	
	public Data(int temperature, int waterLine, boolean thief, boolean btn) {
		this.temperature = temperature;
		this.waterLine = waterLine;
		this.thief = thief;
		this.btn = btn;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getWaterLine() {
		return waterLine;
	}

	public void setWaterLine(int waterLine) {
		this.waterLine = waterLine;
	}

	public boolean isThief() {
		return thief;
	}

	public void setThief(boolean thief) {
		this.thief = thief;
	}

	public boolean isBtn() {
		return btn;
	}

	public void setBtn(boolean btn) {
		this.btn = btn;
	}
	
	public boolean isEntry() {
		return entry;
	}

	public void setEntry(boolean entry) {
		this.entry = entry;
	}

	
	@Override
	public String toString() {
		return "Data [temperature=" + temperature + ", waterLine=" + waterLine + ", thief=" + thief + ", btn=" + btn
				+ ", entry=" + entry + "]";
	}

	public static Data getInstance() {
		return instance;
	}
}
