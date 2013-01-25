package com.hipits.manager;

import java.util.ArrayList;
import java.util.List;

import com.hipits.model.Point;

public class PointManager {
	
	private static PointManager pointManager;
	
	public static PointManager getInstance() {
		if (pointManager == null) {
			pointManager = new PointManager();
		}
		return pointManager;
	}
	
	public List<Point> getPoints() {
		Integer[] xPoints = new Integer[]{5, 50, 25}; 
		Integer[] yPoints = new Integer[]{10, 50, 25};
		Integer[] lengths = new Integer[]{10, 10, 15};
		
		List<Point> points = new ArrayList<Point>();
		
		for (int i = 0; i < yPoints.length; i++) {
			Point point = new Point(xPoints[i], yPoints[i], lengths[i]);
			points.add(calPoint(point));
		}
		return points;
	}
	
	public Point calPoint(Point point){
		int curwidth = ScreenManager.getInstance().getWidth();
		int curHeight = ScreenManager.getInstance().getHeight();
		
		float x = curwidth * (point.getX() / 100f);
		float y = curHeight * (point.getY() / 100f);
		float length = curwidth * (point.getLength() / 100f);
		
		point.setX((int)x);
		point.setY((int)y);
		point.setLength((int)length);
		
		return point;
	}
}