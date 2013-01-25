package com.hipits.manager;

public class ScreenManager {
	
	private int width;
	private int height;
	private static ScreenManager screenManager;
	
	public ScreenManager() {
		super();
	}

	public static ScreenManager getInstance() {
		if (screenManager == null) {
			screenManager = new ScreenManager();
		}
		return screenManager;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
